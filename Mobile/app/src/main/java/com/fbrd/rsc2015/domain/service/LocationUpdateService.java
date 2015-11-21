package com.fbrd.rsc2015.domain.service;

import com.fbrd.rsc2015.domain.interactor.LocationInteractor;
import com.fbrd.rsc2015.domain.manager.LocationManager;
import com.fbrd.rsc2015.domain.model.event.LocationPostEvent;

import android.location.Location;

import javax.inject.Inject;

/**
 * Created by noxqs on 21.11.15..
 */
public class LocationUpdateService extends BaseService implements LocationManager.OnConnectionListener,
        LocationManager.OnLocationUpdateListener {

    @Inject
    LocationManager locationManager;

    @Inject
    LocationInteractor locationInteractor;

    private long lastTime;

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager.connect(this);
    }

    @Override
    public void onConnection(boolean success, int code) {
        if(success){
            locationManager.requestUpdates(this);
        }
    }

    @Override
    public void onLocationUpdate(Location location) {
        locationInteractor.postLocation(location);
    }

    public void onLocationPosted(LocationPostEvent locationPostEvent){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.stopUpdates();
    }
}
