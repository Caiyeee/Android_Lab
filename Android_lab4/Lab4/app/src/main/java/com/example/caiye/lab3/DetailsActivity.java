package com.example.caiye.lab3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.ForwardingListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

/**
 * Created by caiye on 2017/10/31.
 */

public class DetailsActivity extends AppCompatActivity {
    private TextView name;
    private TextView price;
    private TextView info;
    private ImageButton back;
    private ImageButton star;
    private ImageButton car;
    private ImageView pic;
    private ListView listView;
    private Intent intent;
    private int cnt = 0;
    private myBroadcastReceiver dynamicReceiver = new myBroadcastReceiver();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.detail);

        String [] selection = {"一键下单","分享商品","不感兴趣","查看更多商品促销信息"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DetailsActivity.this,R.layout.support_simple_spinner_dropdown_item,selection);
        listView = (ListView)findViewById(R.id.more);
        listView.setAdapter(arrayAdapter);

        intent = getIntent();
        final Bundle bundle = intent.getExtras();
        final int showPic = bundle.getInt("pic");
        final String showName = bundle.getString("name");
        final String showPrice = bundle.getString("price");
        final String showInfo = bundle.getString("info");
        setResult(0,intent);

        name = (TextView)findViewById(R.id.detailName);
        price = (TextView)findViewById(R.id.detailPrice);
        info = (TextView)findViewById(R.id.detailInfo);
        star = (ImageButton)findViewById(R.id.detailStar);
        back = (ImageButton)findViewById(R.id.detailBack);
        car = (ImageButton)findViewById(R.id.detailCar);
        pic = (ImageView)findViewById(R.id.detailPic);
        //商品信息更新
        name.setText(showName);
        price.setText(showPrice);
        info.setText(showInfo);
        pic.setImageResource(showPic);
        //返回按钮的监听
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsActivity.this.finish();
            }
        });
        //星星按钮的监听
        star.setTag("0");
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(star.getTag() == "0"){
                    star.setBackgroundResource(R.mipmap.full_star);
                    star.setTag("1");
                } else {
                    star.setBackgroundResource(R.mipmap.empty_star);
                    star.setTag("0");
                }
            }
        });
        //购物车按钮的监听
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailsActivity.this,"商品已添加到购物车",Toast.LENGTH_SHORT).show();
                //传递event事件
                EventBus.getDefault().post(new Event(showName,showPrice,showInfo,showPic));
                //注册动态广播
                IntentFilter dynamic_filter = new IntentFilter();
                dynamic_filter.addAction("com.example.caiye.lab3.dynamic");
                registerReceiver(dynamicReceiver,dynamic_filter);
                //发送动态广播
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("com.example.caiye.lab3.dynamic");
                broadcastIntent.putExtras(bundle);
                sendBroadcast(broadcastIntent);
            }
        });
    }
    //注销动态广播
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(dynamicReceiver);
    }
}
