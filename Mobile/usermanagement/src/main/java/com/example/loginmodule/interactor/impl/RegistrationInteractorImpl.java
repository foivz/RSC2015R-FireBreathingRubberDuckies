package com.example.loginmodule.interactor.impl;

import android.util.Log;

import com.dmacan.lightandroidaws.FileUploader;
import com.example.loginmodule.interactor.RegistrationInteractor;
import com.example.loginmodule.model.bus.ZET;
import com.example.loginmodule.model.entity.User;
import com.example.loginmodule.model.event.RegistrationSuccessEvent;
import com.example.loginmodule.model.event.error.RegistrationErrorEvent;
import com.example.loginmodule.model.response.GetUserResponse;
import com.example.loginmodule.service.app.AppServiceImpl;
import com.example.loginmodule.util.ServiceUtil;

import java.io.File;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by noxqs on 14.11.15..
 */
public class RegistrationInteractorImpl implements RegistrationInteractor {

    private AppServiceImpl.AppService appService;
    private FileUploader fileUploader;
    private String token;

    public RegistrationInteractorImpl(AppServiceImpl.AppService appService) {
        this.appService = appService;
}

    @Override
    public void register(String email, String username, String password, String firstName, String lastName, String registrationId) {
        appService.register(email, username, password, firstName, lastName, registrationId)
                .flatMap(rr -> appService.login(username, password, "password"))
                .flatMap(lr -> {
                    token = lr.getAccessToken();
                    return appService.getUser(ServiceUtil.formatToken(token), lr.getUserId());
                })
                .map(ur -> toUser(ur, token))
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> ZET.post(new RegistrationSuccessEvent(user, token)),
                        re -> ZET.post(new RegistrationErrorEvent(ServiceUtil.getStatusCode(re)))
                );

    }

    @Override
    public void register(String email, String username, String password, String firstName, String lastName, String registrationId, File avatar) {
        if (avatar == null) {
            register(email, username, password, firstName, lastName, registrationId);
        }
        Observable.defer(() -> Observable.just(fileUploader.uploadFile(avatar)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(image -> {
                    Log.i("DAM", "Image: " + image);
                    appService.register(email, username, password, firstName, lastName, registrationId)
                            .flatMap(rr -> appService.login(username, password, "password"))
                            .flatMap(lr -> {
                                token = lr.getAccessToken();
                                return appService.updateUser(ServiceUtil.formatToken(token), lr.getUserId(), lr.getUserId(), email, username, firstName, lastName, image);
                            })
                            .map(ur -> toUser(ur, token))
                            .unsubscribeOn(Schedulers.io())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    user -> ZET.post(new RegistrationSuccessEvent(user, token)),
                                    re -> ZET.post(new RegistrationErrorEvent(ServiceUtil.getStatusCode(re)))
                            );
                });
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
