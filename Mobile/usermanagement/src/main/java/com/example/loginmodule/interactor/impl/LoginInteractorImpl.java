package com.example.loginmodule.interactor.impl;


import com.example.loginmodule.interactor.LoginInteractor;
import com.example.loginmodule.model.bus.ZET;
import com.example.loginmodule.model.entity.User;
import com.example.loginmodule.model.event.LoginSuccessEvent;
import com.example.loginmodule.model.event.error.LoginErrorEvent;
import com.example.loginmodule.model.response.GetUserResponse;
import com.example.loginmodule.service.app.AppServiceImpl;
import com.example.loginmodule.util.ServiceUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by noxqs on 14.11.15..
 */
public class LoginInteractorImpl implements LoginInteractor {

    public static final String GRANT_TYPE = "password";

    private AppServiceImpl.AppService appService;
    private String token;

    public LoginInteractorImpl(AppServiceImpl.AppService appService) {
        this.appService = appService;
    }

    @Override
    public void login(String username, String password) {
        appService.login(username, password, GRANT_TYPE)
                .flatMap(lr -> {
                    token = lr.getAccessToken();
                    return appService.getUser(ServiceUtil.formatToken(lr.getAccessToken()), lr.getUserId());
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
