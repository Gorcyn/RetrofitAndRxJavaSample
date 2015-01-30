package com.gorcyn.sample.retrofit.rxjava.ui;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gorcyn.sample.retrofit.rxjava.R;
import com.gorcyn.sample.retrofit.rxjava.model.Repo;
import com.gorcyn.sample.retrofit.rxjava.rx.SimpleObserver;
import com.gorcyn.sample.retrofit.rxjava.service.GithubServiceProvider;

import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class MainActivity extends ActionBarActivity {

    final String USER_NAME = "Gorcyn";

    ListView repoListView;
    ArrayAdapter<Repo> repoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        repoListView = (ListView) findViewById(android.R.id.list);

        // Empty
        View reposListEmpty = findViewById(R.id.list_empty);
        repoListView.setEmptyView(reposListEmpty);

        repoListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new LinkedList<Repo>());
        repoListView.setAdapter(repoListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        GithubServiceProvider.getInstance().getUserRepos(USER_NAME)
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap(new Func1<List<Repo>, Observable<Repo>>() {
                @Override
                public Observable<Repo> call(List<Repo> repos) {
                    return Observable.from(repos);
                }
            })
            .filter(new Func1<Repo, Boolean>() {
                @Override
                public Boolean call(Repo repo) {
                    return repo.getName().contains("Sample");
                }
            })
            .toList()
            .subscribe(new SimpleObserver<List<Repo>>() {
                @Override
                public void onNext(List<Repo> repos) {
                    repoListAdapter.clear();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        repoListAdapter.addAll(repos);
                    } else {
                        for (Repo repo : repos) {
                            repoListAdapter.add(repo);
                        }
                    }
                    repoListAdapter.notifyDataSetChanged();
                }
                @Override
                public void onError(Throwable e) {
                    new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.failure)
                        .setMessage(e.getMessage())
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                }
            });
    }
}
