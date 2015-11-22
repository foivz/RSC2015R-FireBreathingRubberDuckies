package com.fbrd.rsc2015.domain.interactor;

import android.util.Log;

import com.example.loginmodule.model.bus.ZET;
import com.example.loginmodule.util.ServiceUtil;
import com.fbrd.rsc2015.domain.model.event.DeathFailureEvent;
import com.fbrd.rsc2015.domain.model.event.DeathSuccessEvent;
import com.fbrd.rsc2015.domain.repository.RSCRepository;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by noxqs on 22.11.15..
 */
public class DeathInteractor {

    private RSCRepository.Api api;

    public DeathInteractor(RSCRepository.Api api) {
        this.api = api;
    }

    public void killYourself(String token, String nfc, long gameId){
        api.killYourself(ServiceUtil.formatToken(token), nfc, gameId)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        success -> {
                            ZET.post(new DeathSuccessEvent(true));
                        },
                        error -> {
                            ZET.post(new DeathFailureEvent(ServiceUtil.getStatusCode(error)));
                        }
                );

    }
}
