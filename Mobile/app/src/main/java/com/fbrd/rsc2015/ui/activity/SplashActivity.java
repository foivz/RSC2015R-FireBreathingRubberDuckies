package com.fbrd.rsc2015.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dmacan.lightandroid.ui.custom.view.SexyWebView;
import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.app.di.component.DaggerSplashComponent;
import com.fbrd.rsc2015.app.di.module.ApiModule;
import com.fbrd.rsc2015.domain.repository.RSCPreferences;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    @Inject
    RSCPreferences preferences;
    @Bind(R.id.splashView)
    SexyWebView sexyWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        DaggerSplashComponent.builder().apiModule(new ApiModule()).build().inject(this);
//        sexyWebView.load("http://95.85.26.58:6767/2dbbbb50-9066-11e5-b38a-5b7a2be8896c");
        splash();
    }

    private void splash() {
        preferences.isUserLoggedIn()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(getResources().getInteger(R.integer.splash_time), TimeUnit.MILLISECONDS)
                .subscribe(result -> {
                    finish();
                    if (result) {
                        startActivity(new Intent(this, MainActivity.class));
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                    }
                });
    }
}
