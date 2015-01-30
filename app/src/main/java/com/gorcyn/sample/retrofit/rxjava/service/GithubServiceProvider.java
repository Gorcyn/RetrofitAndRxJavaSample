package com.gorcyn.sample.retrofit.rxjava.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class GithubServiceProvider {

    private static final String BASE_URL = "https://api.github.com";

    private static GithubService GITHUB_SERVICE;

    public static GithubService getInstance() {
        if (GITHUB_SERVICE == null) {

            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(1500, TimeUnit.MILLISECONDS);
            client.setWriteTimeout(1500, TimeUnit.MILLISECONDS);
            client.setReadTimeout(1500, TimeUnit.MILLISECONDS);

            Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setClient(new OkClient(client))
                .setConverter(new GsonConverter(gson))
                .build();

            GITHUB_SERVICE = restAdapter.create(GithubService.class);
        }
        return GITHUB_SERVICE;
    }
}
