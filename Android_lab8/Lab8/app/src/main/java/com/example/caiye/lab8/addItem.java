package com.example.caiye.lab8;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by caiye on 2017/12/18.
 */

public class addItem extends AppCompatActivity {
    public Button add_btn;
    public EditText add_name;
    public EditText add_date;
    public EditText add_gift;
    public String table_name = "birthday";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        add_btn = (Button)findViewById(R.id.add_button);
        add_name = (EditText) findViewById(R.id.add_name);
        add_date = (EditText) findViewById(R.id.add_date);
        add_gift = (EditText) findViewById(R.id.add_gift);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(add_name.length()==0){
                    Toast.makeText(addItem.this,"名字不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    String name = add_name.getText().toString();
                    String date = add_date.getText().toString();
                    String gift = add_gift.getText().toString();

                    myDB db = new myDB(getBaseContext());
                    SQLiteDatabase sqlite = db.getWritableDatabase();
                    Cursor cursor = sqlite.query(table_name,null,"name=?",new String[]{name},null,null,null);
                    if(cursor.moveToNext()){//存在名字相同的条目
                        Toast.makeText(addItem.this,"该名字已被添加过",Toast.LENGTH_SHORT).show();
                    }else{ //插入条目
                        String sql = "insert into " + table_name + "(name,date,gift) values('" + name + "','" + date + "','" + gift + "')";
                        try{
                            sqlite.execSQL(sql);
                            sqlite.close();
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                        setResult(1,new Intent());
                        finish();
                    }
                }
            }
        });
    }



}
