package com.example.caiye.lab3;

//import android.support.v7.app.AlertController;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

//import static android.support.v7.app.AlertController.*;

public class MainActivity extends AppCompatActivity {

    ListView myListView;
    RecyclerView myRecyclerView;

    myListViewAdapter listViewAdapter;
    myRecyclerAdapter recyclerAdapter;

    List<Good> itemGoods;
    List<Good> carGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemGoods = new ArrayList<Good>();
        carGoods = new ArrayList<Good>();


        myListView = (ListView)findViewById(R.id.list_view);
        myRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        listViewAdapter = new myListViewAdapter(MainActivity.this,carGoods);
        recyclerAdapter = new myRecyclerAdapter(MainActivity.this,itemGoods);
        myListView.setAdapter(listViewAdapter);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //myRecyclerView.setAdapter(recyclerAdapter);
        //有动画的适配器
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(recyclerAdapter);
        animationAdapter.setDuration(700);
        myRecyclerView.setAdapter(animationAdapter);
        myRecyclerView.setItemAnimator(new OvershootInLeftAnimator());


        final String[] name = new String[]{"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis", "waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher", "Maltesers", "Lindt", "Borggreve"};
        final String[] price = new String[]{"¥ 5.00", "¥ 59.00", "¥ 79.00", "¥ 2399.00", "¥ 179.00", "¥ 14.00", "¥ 132.59", "¥ 141.43", "¥ 139.43", "¥ 28.90"};
        final String[] info = new String[]{"作者 Johanna Basford", "产地 德国", "产地 澳大利亚", "版本 8GB", "重量 2Kg", "产地 英国", "重量 300g", "重量 118g", "重量 249g", "重量 640g"};
        final int[] pic = new int[]{R.mipmap.enchatedforest,R.mipmap.arla,R.mipmap.devondale,R.mipmap.kindle,R.mipmap.waitrose,R.mipmap.mcvitie,R.mipmap.ferrero,R.mipmap.maltesers,R.mipmap.lindt,R.mipmap.borggreve};
        for(int i=0; i<name.length; i++)
            itemGoods.add(new Good(name[i],price[i],info[i],pic[i]));
        recyclerAdapter.notifyDataSetChanged();

        //购物车列表的点击事件
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                intent.putExtra("name",carGoods.get(position).getName());
                intent.putExtra("price",carGoods.get(position).getPrice());
                intent.putExtra("info",carGoods.get(position).getInfo());
                intent.putExtra("pic",carGoods.get(position).getPic());
                startActivityForResult(intent,0);//requestcode=0
            }
        });
        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("移除商品").setMessage("从购物车移除"+name[position]+"?")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"您选择了取消",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                carGoods.remove(position);
                                listViewAdapter.notifyDataSetChanged();
                            }
                        }).show();
                return false;
            }
        });

        //商品列表的点击事件
        recyclerAdapter.setOnItemClickListener(new myRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                intent.putExtra("name",itemGoods.get(position).getName());
                intent.putExtra("price",itemGoods.get(position).getPrice());
                intent.putExtra("info",itemGoods.get(position).getInfo());
                intent.putExtra("pic",itemGoods.get(position).getPic());
                intent.putExtra("pos",position);
                startActivityForResult(intent,0);
            }

            @Override
            public void onLongClick(int position) {
                Toast.makeText(getApplicationContext(),"移除第"+position+"个商品",Toast.LENGTH_SHORT).show();
                itemGoods.remove(position);
                recyclerAdapter.notifyItemRemoved(position);//有动画的删除
            }
        });

        //商品列表和购物车列表的跳转
        final LinearLayout carLayout = (LinearLayout)findViewById(R.id.car_layout);
        final LinearLayout homeLayout = (LinearLayout)findViewById(R.id.home_layout);
        carLayout.setVisibility(View.INVISIBLE);//购物车列表一开始设置为不可见
        final FloatingActionButton floatingCar = (FloatingActionButton)findViewById(R.id.float_car);
        floatingCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(carLayout.getVisibility()== View.INVISIBLE){
                    carLayout.setVisibility(View.VISIBLE);
                    homeLayout.setVisibility(View.INVISIBLE);
                    floatingCar.setImageResource(R.mipmap.mainpage);
                }
                else {
                    carLayout.setVisibility(View.INVISIBLE);
                    homeLayout.setVisibility(View.VISIBLE);
                    floatingCar.setImageResource(R.mipmap.shoplist);
                }
            }
        });
    }
    //更新购物车的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode==0 && resultCode==0){
            Bundle bud = intent.getExtras();
            String name = bud.getString("name");
            String price = bud.getString("price");
            String info = bud.getString("info");
            int pic = bud.getInt("pic");
            int count = bud.getInt("cnt",0);
            for(int i=0; i<count; i++)
                carGoods.add(new Good(name,price,info,pic));
            listViewAdapter.notifyDataSetChanged();
        }
    }
}
