package com.fbrd.rsc2015.ui.presenter;

import com.example.loginmodule.interactor.LoginInteractor;
import com.example.loginmodule.interactor.LoginInteractorFacebook;
import com.example.loginmodule.interactor.LoginInteractorGoogle;
import com.example.loginmodule.model.bus.ZET;
import com.example.loginmodule.model.event.LoginSuccessEvent;
import com.example.loginmodule.model.event.error.LoginErrorEvent;
import com.fbrd.rsc2015.domain.gcm.RSCRegistar;
import com.fbrd.rsc2015.domain.repository.RSCPreferences;
import com.fbrd.rsc2015.ui.activity.LoginActivity;

import de.halfbit.tinybus.Subscribe;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by david on 21.11.2015..
 */
public class LoginPresenter {

    private LoginActivity view;
    private LoginInteractor loginInteractor;
    private LoginInteractorFacebook loginInteractorFacebook;
    private LoginInteractorGoogle loginInteractorGoogle;
    private RSCPreferences preferences;
    private RSCRegistar registar;

    public LoginPresenter(LoginActivity view, LoginInteractor loginInteractor, LoginInteractorFacebook loginInteractorFacebook, LoginInteractorGoogle loginInteractorGoogle, RSCPreferences preferences, RSCRegistar registar) {
        this.registar = registar;
        this.view = view;
        this.loginInteractor = loginInteractor;
        this.loginInteractorFacebook = loginInteractorFacebook;
        this.loginInteractorGoogle = loginInteractorGoogle;
        this.preferences = preferences;
    }

    public void attemptSignIn() {
        view.showLoading("Signing you in");
        String username = view.getUsername();
        String password = view.getPassword();
        loginInteractor.login(username, password);
    }

    public void attemptSignInFacebook() {
        view.showLoading("Signing you in");
        if (preferences.isRegistrationIdAvailable()) {
            loginInteractorFacebook.login(preferences.getRegistrationId());
        } else {
            registar.setOnGcmRegisteredListener((b, s) -> {
                preferences.saveRegistrationId(s);
                loginInteractorFacebook.login(s);
            });
            registar.setOnErrorListener(error -> {
                view.showError("An error has occured");
            });
            registar.registerInBackground("251380982976");
        }
    }

    public void attemptSignInGoogle() {
        view.showLoading("Signing you in");
        if (preferences.isRegistrationIdAvailable()) {
            loginInteractorGoogle.startLogin(preferences.getRegistrationId());
        } else {
            registar.setOnGcmRegisteredListener((b, s) -> {
                preferences.saveRegistrationId(s);
                loginInteractorGoogle.startLogin(s);
            });
            registar.setOnErrorListener(error -> {
                view.showError("An error has occured");
            });
            registar.registerInBackground("251380982976");
        }
    }

    public void completeSignInGoogle(String token) {
        loginInteractorGoogle.completeLogin(token);
    }

    @Subscribe
    public void onLoginSuccess(LoginSuccessEvent event) {
        preferences.storeUserAsync(event.getUser(), event.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                    view.dismissLoading();
                    view.proceed();
                });
    }

    @Subscribe
    public void onLoginError(LoginErrorEvent errorEvent) {
        view.dismissLoading();
//        view.showError("An error haš occured");
    }

    public void onViewResume() {
        ZET.register(this);
    }

    public void onViewPause() {
        ZET.unregister(this);
    }
}
