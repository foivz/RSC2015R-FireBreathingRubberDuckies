package com.fbrd.rsc2015.app.di.module;

import com.example.loginmodule.service.app.AppServiceImpl;
import com.fbrd.rsc2015.app.RSCApp;
import com.fbrd.rsc2015.domain.repository.RSCPreferences;
import com.fbrd.rsc2015.domain.repository.RSCRepository;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by david on 21.11.2015..
 */
@Module
public class ApiModule {

    @Provides
    @Singleton
    @Named("production")
    String provideEndpoint() {
        return "http://firebreathingrubberduckies.azurewebsites.net/";
    }

    @Provides
    @Singleton
    AppServiceImpl provideServiceImpl(@Named("production") String endpoint) {
        return new AppServiceImpl(endpoint);
    }

    @Provides
    @Singleton
    AppServiceImpl.AppService provideService(AppServiceImpl serviceImpl) {
        return serviceImpl.getAppService();
    }

    @Provides
    @Singleton
    RSCRepository rscRepository(@Named("production") String endpoint) {
        return new RSCRepository(endpoint);
    }

    @Provides
    @Singleton
    RSCRepository.Api api(RSCRepository repository) {
        return repository.getAppService();
    }

    @Provides
    @Singleton
    RSCPreferences preferences() {
        return new RSCPreferences(RSCApp.getInstance());
    }
}
