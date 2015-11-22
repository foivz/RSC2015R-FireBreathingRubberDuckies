package com.fbrd.rsc2015.domain.interactor;

import com.example.loginmodule.model.bus.ZET;
import com.example.loginmodule.util.ServiceUtil;
import com.fbrd.rsc2015.domain.model.event.PairingSuccessEvent;
import com.fbrd.rsc2015.domain.repository.RSCPreferences;
import com.fbrd.rsc2015.domain.repository.RSCRepository;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by david on 22.11.2015..
 */
public class PairingInteractor {

    private RSCRepository.Api api;
    private RSCPreferences preferences;

    public PairingInteractor(RSCRepository.Api api, RSCPreferences preferences) {
        this.api = api;
        this.preferences = preferences;
    }

    public void pair(String nfc, long gameId) {
        api.pair(ServiceUtil.formatToken(preferences.getToken()), nfc, gameId)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> ZET.post(new PairingSuccessEvent()),
                        error -> ZET.post(new PairingErrorEvent(ServiceUtil.getStatusCode(error))));
    }
}
