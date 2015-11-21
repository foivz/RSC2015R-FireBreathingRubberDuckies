package com.fbrd.rsc2015.ui.presenter;

import com.example.loginmodule.interactor.LoginInteractor;
import com.example.loginmodule.interactor.LoginInteractorFacebook;
import com.example.loginmodule.interactor.LoginInteractorGoogle;
import com.example.loginmodule.interactor.RegistrationInteractor;
import com.example.loginmodule.model.bus.ZET;
import com.example.loginmodule.model.event.LoginSuccessEvent;
import com.example.loginmodule.model.event.error.LoginErrorEvent;
import com.fbrd.rsc2015.ui.activity.LoginActivity;

import javax.inject.Inject;

import de.halfbit.tinybus.Subscribe;

/**
 * Created by david on 21.11.2015..
 */
public class LoginPresenter {

    private LoginActivity view;
    private LoginInteractor loginInteractor;
    private LoginInteractorFacebook loginInteractorFacebook;
    private LoginInteractorGoogle loginInteractorGoogle;

    @Inject
    public LoginPresenter(LoginActivity view, LoginInteractor loginInteractor, LoginInteractorFacebook loginInteractorFacebook, LoginInteractorGoogle loginInteractorGoogle) {
        this.view = view;
        this.loginInteractor = loginInteractor;
        this.loginInteractorFacebook = loginInteractorFacebook;
        this.loginInteractorGoogle = loginInteractorGoogle;
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

    }

    @Subscribe
    public void onLoginSuccess(LoginSuccessEvent event) {
        view.dismissLoading();
        view.proceed();
    }

    @Subscribe
    public void onLoginError(LoginErrorEvent errorEvent) {
        view.dismissLoading();
        view.showError("An error has occured");
    }

    public void onViewResume() {
        ZET.register(this);
    }

    public void onViewPause() {
        ZET.unregister(this);
    }
}
