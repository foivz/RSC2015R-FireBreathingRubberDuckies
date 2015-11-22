package com.fbrd.rsc2015.domain.interactor;

import com.example.loginmodule.util.ServiceUtil;
import com.fbrd.rsc2015.domain.repository.RSCPreferences;
import com.fbrd.rsc2015.domain.repository.RSCRepository;

import android.location.Location;
import android.util.Log;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by noxqs on 21.11.15..
 */
public class LocationInteractor {

    private RSCRepository.Api api;
    private Location location;
    private String token;
    private RSCPreferences preferences;


    @Inject
    public LocationInteractor(RSCRepository.Api api, RSCPreferences preferences) {
        this.api = api;
        this.preferences = preferences;

        this.token = preferences.getToken();
    }


    public void postLocation(Location location) {
        api.pushLocation(ServiceUtil.formatToken(token), location.getLatitude(), location.getLongitude(), 2)
//                .flatMapIterable(LocationResponse::getData)
//                .map(data -> {
//                            LocationModel item = new LocationModel();
//                            item.setLatitude(data.getLatitude());
//                            item.setLongitude(data.getLongitude());
//                            item.setMapId(2); //TODO ovdje treba dohvatit map id -> dobijes ga kada te admin doda u neki tim ili game
//                            return item;
//                        }
//                )
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                    Log.i("DAM", "Success");
                }, error -> {
                    Log.e("DAM", "Error", error);
                });
    }
}
