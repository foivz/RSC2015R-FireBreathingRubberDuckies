package com.example.loginmodule.interactor.impl;

import com.example.loginmodule.interactor.LoginInteractorFacebook;
import com.example.loginmodule.model.bus.ZET;
import com.example.loginmodule.model.entity.User;
import com.example.loginmodule.model.event.LoginSuccessEvent;
import com.example.loginmodule.model.event.error.LoginErrorEvent;
import com.example.loginmodule.model.response.GetUserResponse;
import com.example.loginmodule.service.app.AppServiceImpl;
import com.example.loginmodule.util.ServiceUtil;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import android.app.Activity;

import java.util.Arrays;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by david on 15.11.2015..
 */
public class LoginInteractorFacebookImpl implements LoginInteractorFacebook {

    private Activity activity;
    private CallbackManager callbackManager;
    private AppServiceImpl.AppService appService;

    private String token;
    private String registrationId;

    public LoginInteractorFacebookImpl(Activity activity, CallbackManager callbackManager, AppServiceImpl.AppService appService) {
        this.activity = activity;
        this.callbackManager = callbackManager;
        this.appService = appService;
    }

    @Override
    public void login(String registrationId) {
        this.registrationId = registrationId;
        LoginManager.getInstance()
                .registerCallback(callbackManager, loginCallback);
        LoginManager.getInstance()
                .logInWithReadPermissions(activity, Arrays.asList("public_profile", "email", "user_photos"));
    }

    private void completeLogin(LoginResult result) {
        appService.loginFacebook(result.getAccessToken().getToken(), registrationId)
                .flatMap(lfr -> {
                    token = lfr.getToken();
                    return appService.getUser(ServiceUtil.formatToken(lfr.getToken()), lfr.getId());
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
        user.setImage(data.getAvatar());
        user.setEmail(data.getEmail());
        user.setUsername(data.getUsername());
        user.setToken(token);
        return user;
    }

    private FacebookCallback<LoginResult> loginCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult result) {
            completeLogin(result);
        }

        @Override
        public void onCancel() {
            ZET.post(new LoginErrorEvent(301));
        }

        @Override
        public void onError(FacebookException error) {
            ZET.post(new LoginErrorEvent(302));
        }
    };
}
