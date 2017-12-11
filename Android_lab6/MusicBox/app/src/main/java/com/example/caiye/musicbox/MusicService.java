package com.example.caiye.musicbox;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

/**
 * Created by caiye on 2017/11/28.
 */

public class MusicService extends Service {
    @Nullable
    private MediaPlayer mp;
    private IBinder iBinder = new mBinder();

    public class mBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException{
            switch (code){
                case 101: //播放
                    if(mp.isPlaying())
                        mp.pause();
                    else{
                        mp.start();
                        reply.writeInt(mp.getCurrentPosition());
                    }
                    break;
                case 102: //停止
                    if(mp != null){
                        mp.pause();
                        mp.seekTo(0);
                    }
                    reply.writeInt(mp.getCurrentPosition());
                    break;
                case 103: //退出
                    mp.stop();
                    break;
                case 104: //获取当前播放位置
                    if(mp!=null){
                        reply.writeInt(mp.getCurrentPosition());
                        reply.writeInt(mp.getDuration());
                    }
                    break;
                case 105: //进度条拖动
                    mp.seekTo(data.readInt());
                    break;
            }
            return super.onTransact(code,data,reply,flags);
        }
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        try{
            mp.prepare();
        }catch (IOException e){
            e.printStackTrace();
        } catch (IllegalStateException e){
            e.printStackTrace();
        }
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        try{
            mp = new MediaPlayer();
        //    mp.setDataSource(Environment.getExternalStorageDirectory()+"/Download/melt.mp3"); //手机7.1
            mp.setDataSource(Environment.getExternalStorageDirectory()+"/Music/melt.mp3"); //手机4.4
        //    mp.setDataSource("/storage/0D08-3902/melt.mp3"); //虚拟机高版本
        //    mp.setDataSource("/data/melt.mp3");  //虚拟机低版本
            mp.prepare();
            mp.setLooping(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy(){
        mp.stop();
        mp.release();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }
}
