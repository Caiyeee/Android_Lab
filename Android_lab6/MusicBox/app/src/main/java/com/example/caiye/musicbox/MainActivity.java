package com.example.caiye.musicbox;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private Button btn_play;
    private Button btn_quit;
    private Button btn_stop;
    private SeekBar seekBar;
    private TextView state;
    private TextView begin;
    private TextView length;
    private IBinder mBinder;
    private ServiceConnection conn;
    private Handler mHandler;
    private java.text.SimpleDateFormat time = new java.text.SimpleDateFormat("mm:ss");
    private static boolean hasPermission;


    @Override
    protected void onDestroy(){
        super.onDestroy();
        Intent intent = new Intent(MainActivity.this,MusicService.class);
        stopService(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions();//申请权限

        img = (ImageView) findViewById(R.id.img);
        btn_play = (Button) findViewById(R.id.play);
        btn_quit = (Button) findViewById(R.id.quit);
        btn_stop = (Button) findViewById(R.id.stop);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        state = (TextView) findViewById(R.id.state);
        begin = (TextView) findViewById(R.id.begin);
        length = (TextView) findViewById(R.id.length);
        state.setVisibility(View.INVISIBLE);

        //动画旋转
        final ObjectAnimator animation = ObjectAnimator.ofFloat(img,"rotation",0,360);
        animation.setDuration(30000);//旋转一圈的时间
        animation.setRepeatCount(-1);//设定无限循环
        animation.setInterpolator(new LinearInterpolator());//中间线性补充帧数

       //绑定服务
        conn= new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBinder = service;
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                conn = null;
            }
        };
        Intent musicIntent =  new Intent(MainActivity.this,MusicService.class);
        startService(musicIntent);
        bindService(musicIntent,conn, Context.BIND_AUTO_CREATE);

        //更新UI
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 123) {
                    if(mBinder != null) {
                        try {
                            Parcel reply = Parcel.obtain();
                            mBinder.transact(104, Parcel.obtain(), reply, 0);
                            int location = reply.readInt();
                            begin.setText(time.format(location));
                            seekBar.setProgress(location);
                            int max = reply.readInt();
                            length.setText(time.format(max));
                            seekBar.setMax((max));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        //创建子线程，在子线程中处理耗时工作
        Thread mThread = new Thread() {
            @Override
            public void run() {
                while (true){
                    try{
                        Thread.sleep(100);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    mHandler.obtainMessage(123).sendToTarget();
                }
            }
        };
        mThread.start();

        //播放/暂停按钮的点击事件
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_play.getText().equals("PLAY")){
                    btn_play.setText("PAUSED");
                    state.setText("Playing");
                    state.setVisibility(View.VISIBLE);
                    if(animation.isPaused())
                        animation.resume();
                    else
                        animation.start();
                } else{
                    btn_play.setText("PLAY");
                    state.setText("Paused");
                    animation.pause();
                }

                try{
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(101,Parcel.obtain(),reply,0);
                    begin.setText(time.format(reply.readInt()));
                }catch(RemoteException e){
                    e.printStackTrace();
                }
            }
        });
        //停止按钮的点击事件
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_play.setText("PLAY");
                state.setText("Stopped");
                animation.setCurrentPlayTime(0);
                animation.end();
                try{
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(102,Parcel.obtain(),reply,0);
                    begin.setText(time.format(reply.readInt()));
                }catch(RemoteException e){
                    e.printStackTrace();
                }
            }
        });
        //退出按钮的点击事件
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mBinder.transact(103,Parcel.obtain(),Parcel.obtain(),0);
                }catch(RemoteException e){
                    e.printStackTrace();
                }
                unbindService(conn);
                conn = null;
                try{
                    MainActivity.this.finish();
                    System.exit(0);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        //进度条监听事件
        seekBar.setEnabled(true);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    try{
                        Parcel data = Parcel.obtain();
                        data.writeInt(progress);
                        mBinder.transact(105,data,Parcel.obtain(),0);
                    }catch(RemoteException e){
                        e.printStackTrace();
                    }
                }
            }
            //开始拖动进度条
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            //结束拖动进度条
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }



    //申请用户权限
    public void verifyStoragePermissions(){
        int permission = ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            hasPermission = true;
        }
        else {
            System.exit(0);
        }
        return;
    }

    //按手机上返回键时会在后台运行
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
