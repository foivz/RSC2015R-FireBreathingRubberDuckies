package com.fbrd.rsc2015.app.di.module;

import com.fbrd.rsc2015.app.RSCApp;
import com.fbrd.rsc2015.domain.interactor.GetLocationInteractor;
import com.fbrd.rsc2015.domain.interactor.LocationInteractor;
import com.fbrd.rsc2015.domain.manager.LocationManager;
import com.fbrd.rsc2015.domain.manager.LocationManagerGoogle;
import com.fbrd.rsc2015.domain.repository.RSCPreferences;
import com.fbrd.rsc2015.domain.repository.RSCRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by noxqs on 21.11.15..
 */
@Module
public class ServiceModule {

    @Provides
    LocationInteractor provideNotificationsInteractor(RSCRepository.Api api, RSCPreferences preferences) {
        return new LocationInteractor(api, preferences);
    }

    @Provides
    LocationManager locationManager(GetLocationInteractor getLocationInteractor, GetLocationInteractor getLocationUpdateInteractor) {
        return new LocationManagerGoogle(RSCApp.getInstance(), getLocationInteractor, getLocationUpdateInteractor);
    }

    @Provides
    GetLocationInteractor getLocationInteractor() {
        return new GetLocationInteractor();
    }
}
