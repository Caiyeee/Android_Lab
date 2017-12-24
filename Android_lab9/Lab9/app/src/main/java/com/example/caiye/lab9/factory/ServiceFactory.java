package com.example.caiye.lab9.factory;

import com.example.caiye.lab9.model.Github;
import com.example.caiye.lab9.model.Repos;

import java.util.List;
import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by caiye on 2017/12/23.
 */

public class ServiceFactory {
    //负责发起网络请求，维护网络连接
    private static OkHttpClient createOkHttp(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS) //连接超时
                .readTimeout(30,TimeUnit.SECONDS) //读超时
                .writeTimeout(10,TimeUnit.SECONDS) //写超时
                .build();
        return okHttpClient;
    }
    //将网络传输的数据转换为可用的model对象，提供简单的数据处理方式
    public static Retrofit createRetrofit(String baseurl){
        return new Retrofit.Builder()
                .baseUrl(baseurl) //设置访问服务端的baseUrl
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkHttp())
                .build();
    }
}
