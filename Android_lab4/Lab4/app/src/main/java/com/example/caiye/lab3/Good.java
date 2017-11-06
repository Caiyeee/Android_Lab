package com.example.caiye.lab3;

/**
 * Created by caiye on 2017/10/24.
 */

public class Good {
    private String name;
    private String price;
    private String info;
    private int pic;

    public  Good(String name,String price,String info,int pic){
        this.name = name;
        this.info = info;
        this.price = price;
        this.pic = pic;
    }

    String getName() {
        return this.name;
    }
    String getPrice() {
        return this.price;
    }
    String getInfo() {return this.info; }
    int getPic() { return this.pic;}
}
