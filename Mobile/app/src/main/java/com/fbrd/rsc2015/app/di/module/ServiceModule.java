package com.fbrd.rsc2015.app.di.module;

import com.fbrd.rsc2015.domain.interactor.LocationInteractor;
import com.fbrd.rsc2015.domain.interactor.NotificationInteractor;
import com.fbrd.rsc2015.domain.service.BaseService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by noxqs on 21.11.15..
 */
@Module
public class ServiceModule {

    @Provides
    LocationInteractor provideNotificationsInteractor(){
        return new LocationInteractor();
    }
}
