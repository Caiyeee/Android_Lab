package com.example.caiye.lab2;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //点击图片弹出对话框
        ImageView mImage = (ImageView) findViewById(R.id.image);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("上传头像");
                String[] options = {"拍摄","从相册选择"};
                builder.setItems(options,selectListener);
                builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this,"您选择了[取消]",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }

            final DialogInterface.OnClickListener selectListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch (i){
                        case 0:
                            Toast.makeText(MainActivity.this,"您选择了[拍摄]",Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            Toast.makeText(MainActivity.this,"您选择了[从相册上传]",Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                }
            };
        });


        //获取根布局
        final View rootView = ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
        //radioButton的点击事件
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.occasion);
        final RadioButton student = (RadioButton)findViewById(R.id.student);
        final RadioButton teacher = (RadioButton)findViewById(R.id.teacher);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (RadioGroup group, int checkedId) {
                String message = "";
                if(checkedId == student.getId())
                    message = "您选择了学生";
                else if(checkedId == teacher.getId())
                    message = "您选择了教职工";

                Snackbar.make(rootView,message,Snackbar.LENGTH_SHORT)
                        .setAction("确定",new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                Toast.makeText(MainActivity.this,"Snackbar 的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .setDuration(3000)
                        .show();
            }
        });


        //登陆按钮点击事件
        Button login = (Button)findViewById(R.id.signin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout numberTextLayout = (TextInputLayout) findViewById(R.id.sid);
                TextInputLayout passTextLayout = (TextInputLayout) findViewById(R.id.password);
                String number = numberTextLayout.getEditText().getText().toString();
                String password = passTextLayout.getEditText().getText().toString();
                //检查学号
                if(TextUtils.isEmpty(number)){
                    numberTextLayout.setErrorEnabled(true);
                    numberTextLayout.setError("学号不能为空");
                }
                else if(TextUtils.isEmpty(password)){
                    //numberTextLayout.setError("");
                    numberTextLayout.setErrorEnabled(false);
                    passTextLayout.setErrorEnabled(true);
                    passTextLayout.setError("密码不能为空");
                }
                else {
                    //passTextLayout.setError("");
                    //numberTextLayout.setError("");
                    numberTextLayout.setErrorEnabled(false);
                    passTextLayout.setErrorEnabled(false);

                    String message;
                    if(number.equals("123456") && password.equals("6666"))
                        message = "登陆成功";
                    else
                        message = "学号或密码错误";
                    Snackbar.make(rootView,message,Snackbar.LENGTH_SHORT)
                            .setAction("确定",new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    Toast.makeText(MainActivity.this,"Snackbar 的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(3000)
                            .show();
                }
            }
        });


        //点击注册按钮
        Button register = (Button)findViewById(R.id.regist);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                int checkedId = radioGroup.getCheckedRadioButtonId();
                if(checkedId == student.getId())
                    message = "学生注册功能尚未启用";
                else if(checkedId == teacher.getId())
                    message = "教职工注册功能尚未启用";

                Snackbar.make(rootView,message,Snackbar.LENGTH_SHORT)
                        .setAction("确定",new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                Toast.makeText(MainActivity.this,"Snackbar 的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .setDuration(3000)
                        .show();
            }
        });

    }

}
