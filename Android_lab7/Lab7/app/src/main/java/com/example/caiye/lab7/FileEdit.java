package com.example.caiye.lab7;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by caiye on 2017/12/11.
 */

public class FileEdit extends AppCompatActivity{
    EditText name;
    EditText content;
    Button save;
    Button load;
    Button clear;
    Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_edit);

        name = (EditText)findViewById(R.id.name);
        content = (EditText) findViewById(R.id.content);
        save = (Button) findViewById(R.id.save);
        load = (Button) findViewById(R.id.load);
        clear = (Button) findViewById(R.id.clear);
        delete = (Button) findViewById(R.id.delete);

        //保存按钮
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = name.getText().toString();
                try(FileOutputStream fileOutputStream = openFileOutput(fileName,MODE_PRIVATE)){
                    String str = content.getText().toString();
                    fileOutputStream.write(str.getBytes());
                    Toast.makeText(FileEdit.this,"Save successfully", Toast.LENGTH_SHORT).show();
                }catch (IOException e){
                    Toast.makeText(FileEdit.this,"Fail to save", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //加载按钮
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = name.getText().toString();
                try(FileInputStream fileInputStream = openFileInput(fileName)){
                    byte[] contents = new byte[fileInputStream.available()];
                    fileInputStream.read(contents);
                    content.setText(new String(contents));
                    Toast.makeText(FileEdit.this,"Load successfully", Toast.LENGTH_SHORT).show();
                }catch(IOException e){
                    Toast.makeText(FileEdit.this,"Fail to load file", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //清除按钮
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setText("");
                Toast.makeText(FileEdit.this,"Clear successfully", Toast.LENGTH_SHORT).show();
            }
        });
        //删除按钮
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileName = name.getText().toString();
                boolean b = deleteFile(fileName);
                content.setText("");
                if(b)
                    Toast.makeText(FileEdit.this,"Delete successfully", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(FileEdit.this,"Failed to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
