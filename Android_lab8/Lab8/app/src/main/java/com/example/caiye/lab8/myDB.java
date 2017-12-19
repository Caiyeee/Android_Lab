package com.example.caiye.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by caiye on 2017/12/18.
 */

public class myDB extends SQLiteOpenHelper {
    private static final String DB_name = "myDB.db";
    private static final String TABLE_name = "birthday";
    private static final int version = 1;

    public myDB(Context context){
        super(context,DB_name,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table if not exists " + TABLE_name + "(name text primary key, date text, gift text)";
        try{
            db.execSQL(CREATE_TABLE);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
