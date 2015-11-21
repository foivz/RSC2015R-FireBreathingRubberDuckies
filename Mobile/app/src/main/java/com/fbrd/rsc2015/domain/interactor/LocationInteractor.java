package com.fbrd.rsc2015.domain.interactor;

import com.fbrd.rsc2015.domain.repository.RSCRepository;

import android.location.Location;

import javax.inject.Inject;

/**
 * Created by noxqs on 21.11.15..
 */
public class LocationInteractor {

    private RSCRepository.Api api;
    private Location location;
    private String token;

    @Inject
    public LocationInteractor(RSCRepository.Api api) {
        this.api = api;
    }

    public void postLocation(Location location){

    }
}
