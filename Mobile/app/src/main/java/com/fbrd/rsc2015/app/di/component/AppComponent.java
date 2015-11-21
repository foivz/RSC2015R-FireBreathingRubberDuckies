package com.fbrd.rsc2015.app.di.component;

import com.fbrd.rsc2015.app.RSCApp;
import com.fbrd.rsc2015.app.di.module.ApiModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by david on 21.11.2015..
 */
@Component(modules = {
        ApiModule.class
})
@Singleton
public interface AppComponent {

    void inject(RSCApp app);

}
