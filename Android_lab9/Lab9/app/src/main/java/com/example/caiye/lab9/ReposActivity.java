package com.example.caiye.lab9;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.caiye.lab9.factory.ServiceFactory;
import com.example.caiye.lab9.model.Github;
import com.example.caiye.lab9.model.Repos;
import com.example.caiye.lab9.service.ReposService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by caiye on 2017/12/23.
 */

public class ReposActivity extends AppCompatActivity {
    private static String BASE_URL = "https://api.github.com/";
    private List<Map<String,String>> reposes;
    private ProgressBar progressBar;
    private ListView listView;
    private String login;

    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        reposes = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        login = getIntent().getStringExtra("login");

        String[] attr = {"name","description","language"};
        int[] ids = {R.id.login,R.id.id,R.id.blog};
        final SimpleAdapter simpleAdapter = new SimpleAdapter(ReposActivity.this,reposes,R.layout.cardview,attr,ids);
        listView.setAdapter(simpleAdapter);

        Retrofit retrofit = ServiceFactory.createRetrofit(BASE_URL);
        ReposService service = retrofit.create(ReposService.class);
        service.getRepos(login)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repos>>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.INVISIBLE);
                        System.out.print("完成传输");
                    }
                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(ReposActivity.this,e.hashCode()+"请确认你的搜索用户存在",Toast.LENGTH_SHORT).show();
                        Log.e("Github-Demo",e.getMessage());
                    }
                    @Override
                    public void onNext(List<Repos> repositories) {
                        for(Repos repos : repositories){
                            Map<String,String> map = new HashMap<String,String>();
                            map.put("name",repos.getName());
                            map.put("description",repos.getDescription());
                            map.put("language",repos.getLanguage());
                            reposes.add(map);
                        }
                        simpleAdapter.notifyDataSetChanged();
                    }
                });
    }
}
