package com.example.caiye.lab3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
/**
 * Created by caiye on 2017/10/25.
 */

public class myListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Good> good;
    public myListViewAdapter(Context context, List<Good> good) {
        this.context = context;
        this.good = good;
    }

    @Override
    public int getCount() {
        if(good == null)
            return 0;
        return good.size();
    }

    @Override
    public Object getItem(int position) {
        if(good == null)
            return null;
        return good.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        public TextView cir;
        public  TextView name;
        public  TextView price;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        //新声明一个View变量和ViewHolder变量
        View convertView;
        ViewHolder viewHolder;
        //当view为空时才加载布局，并且创建一个viewHolder，获得布局中的三个控件
        if(view == null){
            //返回inflate的方法加载布局，context这个参数需要使用这个adapter的activity传入
            convertView = LayoutInflater.from(context).inflate(R.layout.shopcar,null);
            viewHolder = new ViewHolder();
            viewHolder.cir = (TextView)convertView.findViewById(R.id.cir_list);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name_list);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price_list);
            convertView.setTag(viewHolder);//将处理好的viewHolder放入item中
        } else {//否则，让convertView等于view，然后从中取出viewHolder即可
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //从viewHolder中取出对应的对象，然后赋值给他们
        viewHolder.cir.setText(good.get(position).getName().substring(0,1).toUpperCase());
        viewHolder.name.setText(good.get(position).getName());
        viewHolder.price .setText(good.get(position).getPrice());
        return convertView;
    }
}
