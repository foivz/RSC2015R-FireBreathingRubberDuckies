package com.fbrd.rsc2015.domain.interactor;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import android.location.Location;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by noxqs on 21.11.15..
 */
public class GetLocationInteractor {


    public void execute(Callback callback, GoogleApiClient client) {
        Observable.defer(() -> Observable.just(getAndroidLocation(client)))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(location -> {
                    if (location == null) {
                        callback.onGetLocationError();
                    } else {
                        callback.onGetLocationSuccess(location);
                    }
                });
    }

    private Location getAndroidLocation(GoogleApiClient client) {
        return LocationServices.FusedLocationApi.getLastLocation(client);
    }

    public interface Callback {
        void onGetLocationSuccess(Location location);

        void onGetLocationError();
    }

}
