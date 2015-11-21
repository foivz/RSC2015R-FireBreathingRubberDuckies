package com.fbrd.rsc2015.domain.service;

import com.fbrd.rsc2015.app.di.component.DaggerServiceComponent;
import com.fbrd.rsc2015.app.di.module.ServiceModule;
import com.fbrd.rsc2015.domain.interactor.LocationInteractor;
import com.fbrd.rsc2015.domain.manager.LocationManager;
import com.fbrd.rsc2015.domain.model.event.LocationPostEvent;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.inject.Inject;

/**
 * Created by noxqs on 21.11.15..
 */
public class LocationUpdateService extends Service implements LocationManager.OnConnectionListener,
        LocationManager.OnLocationUpdateListener {

    @Inject
    LocationManager locationManager;

    @Inject
    LocationInteractor locationInteractor;

    private long lastTime;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerServiceComponent.builder().serviceModule(new ServiceModule()).build().inject(this);
        locationManager.connect(this);
    }

    @Override
    public void onConnection(boolean success, int code) {
        Log.i("DAM", "On connection " + success);
        if (success) {
            locationManager.requestUpdates(this);
        }
    }

    @Override
    public void onLocationUpdate(Location location) {
        locationInteractor.postLocation(location);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.stopUpdates();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
