package com.fbrd.rsc2015.app.di.component;

import com.fbrd.rsc2015.app.di.module.GameModule;
import com.fbrd.rsc2015.ui.activity.GameActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by david on 21.11.2015..
 */
@Component(modules = {GameModule.class})
@Singleton
public interface GameComponent {

    void inject(GameActivity activity);

}
