package com.fbrd.rsc2015.app.di.component;

import com.fbrd.rsc2015.app.di.module.MainModule;
import com.fbrd.rsc2015.ui.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by david on 21.11.2015..
 */
@Component(modules = {MainModule.class})
@Singleton
public interface MainComponent {

    void inject(MainActivity activity);

}
