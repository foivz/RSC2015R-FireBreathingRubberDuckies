package com.fbrd.rsc2015.domain.manager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.domain.interactor.GetLocationInteractor;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

/**
 * Created by noxqs on 21.11.15..
 */
public class LocationManagerGoogle
        implements LocationManager, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "DAM_MANAGER_LOC";

    private GoogleApiClient googleApiClient;

    private LocationRequest request;

    private Context context;

    private GetLocationInteractor getLocationInteractor;

    private GetLocationInteractor getLocationUpdateInteractor;

    private OnLocationListener onLocationListener;

    private OnLocationUpdateListener onLocationUpdateListener;

    private OnConnectionListener onConnectionListener;

    public LocationManagerGoogle(Context context, GetLocationInteractor getLocationInteractor,
                                 GetLocationInteractor getLocationUpdateInteractor) {
        this.context = context;
        this.request = toRequest(context.getResources().getInteger(R.integer.location_refresh));
        this.getLocationInteractor = getLocationInteractor;
        this.getLocationUpdateInteractor = getLocationUpdateInteractor;
    }

    private static LocationRequest toRequest(int interval) {
        return new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(interval)
                .setInterval(interval)
                .setFastestInterval(interval);
    }

    private void initApiClient(Context context) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Log.i(TAG, "Api client built");
    }

    @Override
    public void connect(OnConnectionListener onConnectionListener) {
        this.onConnectionListener = onConnectionListener;
        initApiClient(context);
        googleApiClient.connect();
    }

    @Override
    public void requestUpdates(OnLocationUpdateListener onLocationUpdateListener) {
        this.onLocationUpdateListener = onLocationUpdateListener;
        LocationServices
                .FusedLocationApi
                .requestLocationUpdates(googleApiClient, request, this, Looper.myLooper());
    }

    @Override
    public void stopUpdates() {
        LocationServices
                .FusedLocationApi
                .removeLocationUpdates(googleApiClient, this);
    }

    @Override
    public void getLocation(OnLocationListener listener) {
        getLocationInteractor.execute(getLocationCallback, googleApiClient);
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        Log.i(TAG, "Location changed");
        getLocationUpdateInteractor.execute(getUpdatedLocationCallback, googleApiClient);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connected");
        if (onConnectionListener != null) {
            onConnectionListener.onConnection(true, 200);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        if (onConnectionListener != null) {
            onConnectionListener.onConnection(false, 400);
        }
    }

    private GetLocationInteractor.Callback getLocationCallback = new GetLocationInteractor.Callback() {
        @Override
        public void onGetLocationSuccess(Location location) {
            onLocationListener.onLocation(location);
        }

        @Override
        public void onGetLocationError() {
            Log.e(TAG, "OnGetLocationError1 [get location]");
        }
    };

    private GetLocationInteractor.Callback getUpdatedLocationCallback = new GetLocationInteractor.Callback() {
        @Override
        public void onGetLocationSuccess(Location location) {
            onLocationUpdateListener.onLocationUpdate(location);
        }

        @Override
        public void onGetLocationError() {
            Log.e(TAG, "OnGetLocationError2 [update]");
        }
    };

    public Location getLastKnownLocation() {
        return LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed");
        if (onConnectionListener != null) {
            onConnectionListener.onConnection(false, 500);
        }
    }
}
