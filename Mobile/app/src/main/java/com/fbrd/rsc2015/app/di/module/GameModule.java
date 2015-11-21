package com.fbrd.rsc2015.app.di.module;

import com.fbrd.rsc2015.ui.activity.GameActivity;
import com.fbrd.rsc2015.ui.fragment.MapFragment;
import com.fbrd.rsc2015.ui.fragment.NfcFragment;
import com.fbrd.rsc2015.ui.fragment.StatsFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by david on 21.11.2015..
 */
@Module(includes = {ApiModule.class})
public class GameModule {

    private GameActivity view;

    public GameModule(GameActivity activity) {
        this.view = activity;
    }

    @Provides
    GameActivity activity() {
        return view;
    }

    @Provides
    NfcFragment nfcFragment() {
        NfcFragment nfc = new NfcFragment();
        nfc.setLabel("NFC");
        return nfc;
    }

    @Provides
    MapFragment mapFragment() {
        MapFragment mapFragment = new MapFragment();
        mapFragment.setLabel("Map");
        return mapFragment;
    }

    @Provides
    StatsFragment statsFragment() {
        StatsFragment statsFragment = new StatsFragment();
        statsFragment.setLabel("Stats");
        return statsFragment;
    }

}
