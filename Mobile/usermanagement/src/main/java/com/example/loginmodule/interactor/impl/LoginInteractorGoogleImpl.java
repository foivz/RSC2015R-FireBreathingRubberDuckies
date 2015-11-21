package com.example.loginmodule.interactor.impl;

import com.example.loginmodule.model.bus.ZET;
import com.example.loginmodule.model.entity.User;
import com.example.loginmodule.model.event.LoginSuccessEvent;
import com.example.loginmodule.model.event.error.LoginErrorEvent;
import com.example.loginmodule.model.response.GetUserResponse;
import com.example.loginmodule.service.app.AppServiceImpl;
import com.example.loginmodule.util.GoogleApiUtil;
import com.example.loginmodule.util.ServiceUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import com.example.loginmodule.interactor.LoginInteractorGoogle;

import android.app.Activity;
import android.content.Intent;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by noxqs on 20.11.15..
 */
public class LoginInteractorGoogleImpl implements LoginInteractorGoogle {

    private Activity activity;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient client;
    private AppServiceImpl.AppService service;
    private String token;

    public LoginInteractorGoogleImpl(Activity activity, GoogleApiClient client, AppServiceImpl.AppService service) {
        this.activity = activity;
        this.client = client;
        this.service = service;
    }

    @Override
    public void startLogin() {
        GoogleApiUtil.startGoogleSignIn(client, activity);
    }

    @Override
    public void completeLogin(String googleToken) {
        service.loginGoogle(googleToken)
                .flatMap(lfr -> {
                    token = lfr.getToken();
                    return service.getUser(ServiceUtil.formatToken(lfr.getToken()), lfr.getId());
                })
                .map(ur -> toUser(ur, token))
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> ZET.post(new LoginSuccessEvent(user, token)),
                        error -> ZET.post(new LoginErrorEvent(ServiceUtil.getStatusCode(error)))
                );
    }

    private User toUser(GetUserResponse response, String token) {
        User user = new User();
        GetUserResponse.Data data = response.getData().get(0);
        user.setId(data.getId());
        user.setEmail(data.getEmail());
        user.setUsername(data.getUsername());
        user.setFirstName(data.getFirstName());
        user.setLastName(data.getLastName());
        user.setToken(token);
        user.setImage(data.getAvatar());
        return user;
    }
}
