package com.fbrd.rsc2015.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.app.di.component.DaggerSplashComponent;
import com.fbrd.rsc2015.app.di.module.ApiModule;
import com.fbrd.rsc2015.domain.repository.RSCPreferences;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    @Inject
    RSCPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        DaggerSplashComponent.builder().apiModule(new ApiModule()).build().inject(this);
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
