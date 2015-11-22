package com.fbrd.rsc2015.ui.presenter;

import android.content.Context;

import com.example.loginmodule.interactor.RegistrationInteractor;
import com.example.loginmodule.model.bus.ZET;
import com.example.loginmodule.model.event.RegistrationSuccessEvent;
import com.fbrd.rsc2015.domain.gcm.RSCRegistar;
import com.fbrd.rsc2015.domain.repository.RSCPreferences;
import com.fbrd.rsc2015.ui.activity.RegistrationActivity;

import de.halfbit.tinybus.Subscribe;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by noxqs on 22.11.15..
 */
public class RegistrationPresenter {

    private RegistrationActivity view;
    private RegistrationInteractor interactor;
    private Context context;
    private RSCRegistar registar;
    private RSCPreferences preferences;

    public RegistrationPresenter(RegistrationActivity view, RegistrationInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    public void registerUser(String email, String username, String firstName, String lastname, String password){
        interactor.register(email, username, firstName, lastname, password, "fdf45t4eg4r4rgsd");
    }

    @Subscribe
    public void successfulRegister(RegistrationSuccessEvent event){
        preferences = new RSCPreferences(context);
        preferences.storeUserAsync(event.getUser(), event.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                    view.dismissLoading();
                    view.proceed();
                });

    }


    public void onViewResume() {
        ZET.register(this);
    }

    public void onViewPause() {
        ZET.unregister(this);
    }
}

