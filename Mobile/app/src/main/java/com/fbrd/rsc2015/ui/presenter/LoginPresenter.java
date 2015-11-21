package com.fbrd.rsc2015.ui.presenter;

import com.example.loginmodule.interactor.LoginInteractor;
import com.example.loginmodule.interactor.LoginInteractorFacebook;
import com.example.loginmodule.interactor.LoginInteractorGoogle;
import com.example.loginmodule.model.bus.ZET;
import com.example.loginmodule.model.event.LoginSuccessEvent;
import com.example.loginmodule.model.event.error.LoginErrorEvent;
import com.fbrd.rsc2015.domain.repository.RSCPreferences;
import com.fbrd.rsc2015.ui.activity.LoginActivity;

import javax.inject.Inject;

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

    public LoginPresenter(LoginActivity view, LoginInteractor loginInteractor, LoginInteractorFacebook loginInteractorFacebook, LoginInteractorGoogle loginInteractorGoogle, RSCPreferences preferences) {
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
        loginInteractorFacebook.login();
    }

    public void attemptSignInGoogle() {
        view.showLoading("Signing you in");
        loginInteractorGoogle.startLogin();
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
