package com.example.caiye.lab3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.detail);

        String [] selection = {"一键下单","分享商品","不感兴趣","查看更多商品促销信息"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DetailsActivity.this,R.layout.support_simple_spinner_dropdown_item,selection);
        listView = (ListView)findViewById(R.id.more);
        listView.setAdapter(arrayAdapter);

        intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String showName = intent.getStringExtra("name");
        final String showPrice = intent.getStringExtra("price");
        final String showInfo = intent.getStringExtra("info");
        final int showPic = bundle.getInt("pic");

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
                finish();
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
                cnt++;
                Intent newIntent = new Intent();
                newIntent.putExtra("cnt",cnt);
                newIntent.putExtra("name",showName);
                newIntent.putExtra("price",showPrice);
                newIntent.putExtra("info",showInfo);
                newIntent.putExtra("pic",showPic);
                setResult(0,newIntent);//resultcode=0
            }
        });
    }
}
