package com.fbrd.rsc2015.domain.manager;

import android.location.Location;

/**
 * Created by noxqs on 21.11.15..
 */
public interface LocationManager {

    void connect(OnConnectionListener onConnectionListener);

    void requestUpdates(OnLocationUpdateListener onLocationUpdateListener);

    void stopUpdates();

    void getLocation(OnLocationListener listener);

    interface OnLocationUpdateListener {
        void onLocationUpdate(Location location);
    }

    interface OnConnectionListener {
        void onConnection(boolean success, int code);
    }

    interface OnLocationListener {
        void onLocation(Location location);
    }
}
