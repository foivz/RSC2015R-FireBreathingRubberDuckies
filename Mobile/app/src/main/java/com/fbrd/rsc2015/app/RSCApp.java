package com.fbrd.rsc2015.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.fbrd.rsc2015.app.di.component.DaggerAppComponent;

/**
 * Created by david on 21.11.2015..
 */
public class RSCApp extends Application {

    private static RSCApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(this);
        setInstance(this);
        DaggerAppComponent.create().inject(this);
    }

    public static void setInstance(RSCApp instance) {
        RSCApp.instance = instance;
    }

    public static RSCApp getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
