package com.example.caiye.lab9.service;

import com.example.caiye.lab9.model.Repos;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by caiye on 2017/12/24.
 */

public interface ReposService {
    @GET("/users/{user}/repos")
    Observable<List<Repos> > getRepos(@Path("user") String user);
}
