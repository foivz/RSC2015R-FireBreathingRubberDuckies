package com.example.loginmodule.interactor.impl;

import com.example.loginmodule.interactor.TestEmailInteractor;
import com.example.loginmodule.service.app.AppServiceImpl;
import com.example.loginmodule.util.ServiceUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by noxqs on 14.11.15..
 */
public class TestEmailInteractorImpl implements TestEmailInteractor {

    private AppServiceImpl.AppService appService;

    public TestEmailInteractorImpl(AppServiceImpl.AppService appService) {
        this.appService = appService;
    }

    @Override
    public void testEmail(String email, Callback callback) {
        appService.testEmail(email)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onTestEmailSucces,
                        tee -> callback.onTestEmailError(ServiceUtil.getStatusCode(tee))
                );

    }
}
