package com.example.caiye.lab9.service;

import com.example.caiye.lab9.model.Github;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by caiye on 2017/12/23.
 */

public interface GithubService{
    @GET("/users/{user}")
    Observable<Github> getUser(@Path("user") String user);
}
