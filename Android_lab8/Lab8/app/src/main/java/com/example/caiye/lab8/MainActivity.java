package com.example.caiye.lab8;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    Button add_btn;
    private String table_name = "birthday";
    public List<Map<String,String>> data = new ArrayList<Map<String,String>>();
    public ListView lv;
    public SimpleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_btn = (Button) findViewById(R.id.add);
        lv = (ListView) findViewById(R.id.list);

        //更新界面列表
        updateUI();

        //增加按钮的点击事件
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,addItem.class);
                startActivityForResult(intent,1);
            }
        });

        //长按条目可删除
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setMessage("是否删除？");
                dialog.setPositiveButton("是",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int i){
                        myDB db = new myDB(getBaseContext());
                        SQLiteDatabase sqlite = db.getWritableDatabase();
                        try{//删除条目
                            sqlite.execSQL("delete from "+table_name+" where name=?",new String[]{data.get(position).get("name")});
                            sqlite.close();
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                        data.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("否",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int i){ }
                });
                dialog.create().show();
                return false;
            }
        });

        //点击条目可编辑
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                View newview = factory.inflate(R.layout.dialog,null);
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                final TextView edit_name = (TextView) newview.findViewById(R.id.edit_name);
                final EditText edit_date = (EditText) newview.findViewById(R.id.edit_date);
                final EditText edit_gift = (EditText) newview.findViewById(R.id.edit_gift);
                TextView phone = (TextView) newview.findViewById(R.id.phone);

                System.out.println(edit_name);
                edit_name.setText(data.get(position).get("name"));
                edit_date.setText(data.get(position).get("date"));
                edit_gift.setText(data.get(position).get("gift"));

                //获取电话号码
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS},0);
                }
                //用getContentResolver方法读取联系人
                String number = "";
                Cursor cur = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,null,null,null);
                while (cur.moveToNext()){
                    if(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)).equals(edit_name.getText().toString())){
                        //判断该联系人的信息中，是否有电话号码
                        if(Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0){
                            //取出该联系人信息中的电话号码
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)), null,null);
                            while(phones.moveToNext()){
                                number += phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + "\n";
                            }
                            phones.close();
                        }
                    }
                }
                cur.close();
                //如果手机通讯录中没有对应的联系人则将号码设为无
                if(number.equals(""))
                    number = "无";
                phone.setText(number);

                //弹出对话框
                dialog.setView(newview);
                dialog.setTitle("(❁´︶`❁)生日快乐( •̀∀•́ )");
                dialog.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        myDB db = new myDB(getBaseContext());
                        SQLiteDatabase sqlite = db.getWritableDatabase();
                        try{
                            String whereClause = "name=?";
                            String[] whereArg = {String.valueOf(data.get(position).get("name"))};
                            ContentValues values = new ContentValues();
                            values.put("date",edit_date.getText().toString());
                            values.put("gift",edit_gift.getText().toString());
                            sqlite.update(table_name,values,whereClause,whereArg);
                            sqlite.close();

                        }catch(Exception e){
                            System.out.println(e.getMessage());
                        }
                        updateUI();
                    }
                });
                dialog.setNegativeButton("放弃修改",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){}
                });
                dialog.create().show();
            }
        });
    }


    //更新界面列表
    private void updateUI(){
        try{
            myDB db = new myDB(getBaseContext());
            SQLiteDatabase sqlite = db.getWritableDatabase();
            //查询数据
            Cursor cursor = sqlite.rawQuery("select * from "+table_name,null);
            data = new ArrayList<Map<String, String>>();
            if(cursor != null){
                while(cursor.moveToNext()){
                    Map<String,String> map = new HashMap<String,String>();
                    map.put("name",cursor.getString(0));
                    map.put("date",cursor.getString(1));
                    map.put("gift",cursor.getString(2));
                    data.add(map);
                }
                //列表适配器
                adapter = new SimpleAdapter(MainActivity.this,data,R.layout.item, new String[]{"name","date","gift"},
                        new int[]{R.id.item_name,R.id.item_date,R.id.item_gift});
                lv.setAdapter(adapter);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestcode,int resultcode, Intent intent){
        super.onActivityResult(requestcode,resultcode,intent);
        if(resultcode==1 && resultcode==1)
            updateUI();
    }
}
