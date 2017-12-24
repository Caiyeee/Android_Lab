package com.example.caiye.lab9;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.caiye.lab9.adapter.CardAdapter;
import com.example.caiye.lab9.factory.ServiceFactory;
import com.example.caiye.lab9.model.Github;
import com.example.caiye.lab9.service.GithubService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static String BASE_URL = "https://api.github.com";
    private EditText input;
    private Button clear;
    private Button fetch;
    private List<Github> cards;
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.input);
        clear = (Button) findViewById(R.id.clear);
        fetch = (Button) findViewById(R.id.fetch);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        cards = new ArrayList<Github>();
        cardAdapter = new CardAdapter(MainActivity.this,cards);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setAdapter(cardAdapter);
        //有动画的适配器
        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(cardAdapter);
        animationAdapter.setDuration(700);
        recyclerView.setAdapter(animationAdapter);
        recyclerView.setItemAnimator(new OvershootInLeftAnimator());


        cardAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this,ReposActivity.class);
                intent.putExtra("login",cards.get(position).getLogin());
                startActivity(intent);
            }
            @Override
            public void onLongClick(int position) {
                cards.remove(position);
            //    cardAdapter.notifyItemRemoved(position);
                cardAdapter.notifyItemRemoved(position);
                if(position != cards.size()){
                    cardAdapter.notifyItemRangeChanged(position,cards.size()-position);
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cards != null){
                    cards.clear();
                    cardAdapter.notifyDataSetChanged();
                }
                input.setText("");
            }
        });

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Retrofit retrofit = ServiceFactory.createRetrofit(BASE_URL);
                GithubService service = retrofit.create(GithubService.class);
                String search = input.getText().toString();
                service.getUser(search) //获取observable对象
                        .subscribeOn(Schedulers.newThread()) //请求在新的线程中执行
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Github>(){
                            @Override
                            public void onCompleted() {//请求结束时调用的回调函数
                                System.out.print("完成传输");
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            @Override
                            public void onError(Throwable e) {//请求出现错误时的回调函数
                                Toast.makeText(MainActivity.this,e.hashCode()+"请确认你的搜索用户存在",Toast.LENGTH_SHORT).show();
                                Log.e("Github-Demo",e.getMessage());
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            @Override
                            public void onNext(Github github) {//每一次收到数据时的回调函数
                                if(github != null)
                                    cards.add(github);
                                cardAdapter.notifyDataSetChanged();
                            }
                        });
            }
        });
    }


}
