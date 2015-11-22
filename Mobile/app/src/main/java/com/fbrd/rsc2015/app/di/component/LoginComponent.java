package com.fbrd.rsc2015.app.di.component;

import com.fbrd.rsc2015.app.di.module.LoginModule;
import com.fbrd.rsc2015.ui.activity.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by david on 21.11.2015..
 */
@Component(modules = {LoginModule.class})
@Singleton
public interface LoginComponent {

    void inject(LoginActivity activity);

}
