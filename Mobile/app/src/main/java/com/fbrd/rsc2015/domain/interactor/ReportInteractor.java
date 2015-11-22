package com.fbrd.rsc2015.domain.interactor;

import android.util.Log;

import com.example.loginmodule.model.bus.ZET;
import com.fbrd.rsc2015.domain.repository.RSCRepository;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by david on 22.11.2015..
 */
public class ReportInteractor {

    private RSCRepository.Api api;

    public ReportInteractor(RSCRepository.Api api) {
        this.api = api;
    }

    public void getSummary(long gameId) {
        api.getSummary(gameId)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ZET::post, error -> {
                    Log.e("DAM", "Error", error);
                });

    }
}
