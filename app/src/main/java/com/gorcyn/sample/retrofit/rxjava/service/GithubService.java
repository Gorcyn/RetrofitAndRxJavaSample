package com.gorcyn.sample.retrofit.rxjava.service;

import com.gorcyn.sample.retrofit.rxjava.model.Repo;
import com.gorcyn.sample.retrofit.rxjava.model.User;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface GithubService {

    @GET("/users/{user}")
    Observable<User> getUser(@Path("user") String user);

    @GET("/users/{user}/repos")
    Observable<List<Repo>> getUserRepos(@Path("user") String user);
}
