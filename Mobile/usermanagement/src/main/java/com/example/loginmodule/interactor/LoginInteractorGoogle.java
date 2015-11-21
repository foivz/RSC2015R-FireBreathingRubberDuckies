package com.example.loginmodule.interactor;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by noxqs on 20.11.15..
 */
public interface LoginInteractorGoogle {

    void startLogin(String regId);

    void completeLogin(String token);

}
