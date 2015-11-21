package com.fbrd.rsc2015.app.di.component;

import com.fbrd.rsc2015.app.di.module.ApiModule;
import com.fbrd.rsc2015.app.di.module.ServiceModule;
import com.fbrd.rsc2015.domain.service.LocationUpdateService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by noxqs on 21.11.15..
 */
@Component(
        modules = {
                ApiModule.class,
                ServiceModule.class
        }
)
@Singleton
public interface ServiceComponent {
    void inject(LocationUpdateService service);
}
