package com.fbrd.rsc2015.domain.service;

import com.fbrd.rsc2015.app.di.component.DaggerServiceComponent;
import com.fbrd.rsc2015.app.di.module.ServiceModule;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by noxqs on 21.11.15..
 */
public class BaseService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerServiceComponent.builder()
                .serviceModule(new ServiceModule())
                .build()
                .inject(this);
    }
}
