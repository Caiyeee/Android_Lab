package com.example.caiye.lab7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    EditText psw;
    EditText confirm;
    Button ok;
    Button clear_psw;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        psw = (EditText)findViewById(R.id.psw);
        confirm = (EditText) findViewById(R.id.confirm);
        ok = (Button) findViewById(R.id.ok);
        clear_psw = (Button) findViewById(R.id.clear_psw);

        //ok按钮
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("password",MODE_PRIVATE);
                if(sharedPreferences.getString("password","").equals("")){// 还没注册过
                    if(psw.getText().toString().equals(""))
                        Toast.makeText(MainActivity.this,"password cannot be empty", Toast.LENGTH_SHORT).show();
                    else if(confirm.getText().toString().equals(""))
                        Toast.makeText(MainActivity.this,"please confirm your password", Toast.LENGTH_SHORT).show();
                    else if(!confirm.getText().toString().equals(psw.getText().toString()))
                        Toast.makeText(MainActivity.this,"passwords do not match", Toast.LENGTH_SHORT).show();
                    else if(confirm.getText().toString().equals(psw.getText().toString())){
                        SharedPreferences.Editor editor =  sharedPreferences.edit();
                        editor.putString("password",psw.getText().toString());
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this,FileEdit.class);
                        startActivity(intent); //页面跳转
                    }
                } else { //注册过了
                    if(!confirm.getText().toString().equals(sharedPreferences.getString("password","")))
                        Toast.makeText(MainActivity.this,"the password is false", Toast.LENGTH_SHORT).show();
                    else{
                        Intent intent = new Intent(MainActivity.this,FileEdit.class);
                        startActivity(intent); //页面跳转
                    }
                }
            }
        });
        //清除按钮
        clear_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psw.setText("");
                confirm.setText("");
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("password",MODE_PRIVATE);
        if(!sharedPreferences.getString("password","").equals("")){
            psw.setVisibility(View.INVISIBLE);
            confirm.setHint("Password");
        }
    }
}
