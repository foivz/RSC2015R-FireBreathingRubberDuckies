package com.fbrd.rsc2015.app.di.module;

import com.fbrd.lightandroidgoodies.SpeechRecognizer;
import com.fbrd.rsc2015.domain.interactor.DeathInteractor;
import com.fbrd.rsc2015.domain.interactor.GameInteractor;
import com.fbrd.rsc2015.domain.interactor.PairingInteractor;
import com.fbrd.rsc2015.domain.interactor.ReportInteractor;
import com.fbrd.rsc2015.domain.manager.NFCManager;
import com.fbrd.rsc2015.domain.repository.RSCPreferences;
import com.fbrd.rsc2015.domain.repository.RSCRepository;
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

    @Provides
    NFCManager nfcManager() {
        return new NFCManager(view);
    }

    @Provides
    GameInteractor gameInteractor(RSCRepository.Api api) {
        return new GameInteractor(api);
    }

    @Provides
    PairingInteractor pairingInteractor(RSCRepository.Api api, RSCPreferences preferences) {
        return new PairingInteractor(api, preferences);
    }

    @Provides
    SpeechRecognizer recognizer() {
        return new SpeechRecognizer(view);
    }

    @Provides
    DeathInteractor deathInteractor(RSCRepository.Api api) {
        return new DeathInteractor(api);
    }

    @Provides
    ReportInteractor reportInteractor(RSCRepository.Api api){
        return new ReportInteractor(api);
    }
}
