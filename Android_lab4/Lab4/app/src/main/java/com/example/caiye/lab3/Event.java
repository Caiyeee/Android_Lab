package com.example.caiye.lab3;

/**
 * Created by caiye on 2017/11/2.
 */

public class Event {
    String name;
    String price;
    String info;
    int pic;

    Event(String name,String price,String info,int pic){
        this.name = name;
        this.price = price;
        this.info = info;
        this.pic = pic;
    }

    public String getName(){
        return name;
    }
    public String getPrice(){
        return price;
    }
    public String getInfo(){
        return info;
    }
    public int getPic(){ return pic;}
}
