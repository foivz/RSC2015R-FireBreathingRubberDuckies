package com.fbrd.rsc2015.app.di.module;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import com.example.loginmodule.interactor.LoginInteractor;
import com.example.loginmodule.interactor.LoginInteractorFacebook;
import com.example.loginmodule.interactor.LoginInteractorGoogle;
import com.example.loginmodule.interactor.impl.LoginInteractorFacebookImpl;
import com.example.loginmodule.interactor.impl.LoginInteractorGoogleImpl;
import com.example.loginmodule.interactor.impl.LoginInteractorImpl;
import com.example.loginmodule.service.app.AppServiceImpl;
import com.facebook.CallbackManager;
import com.facebook.internal.CallbackManagerImpl;
import com.fbrd.rsc2015.app.RSCApp;
import com.fbrd.rsc2015.domain.gcm.RSCRegistar;
import com.fbrd.rsc2015.domain.repository.RSCPreferences;
import com.fbrd.rsc2015.ui.activity.LoginActivity;
import com.fbrd.rsc2015.ui.presenter.LoginPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by david on 21.11.2015..
 */
@Module(includes = ApiModule.class)
public class LoginModule {

    private LoginActivity loginActivity;
    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener;

    public LoginModule(LoginActivity loginActivity, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        this.loginActivity = loginActivity;
        this.onConnectionFailedListener = onConnectionFailedListener;
    }

    @Provides
    LoginActivity loginActivity() {
        return loginActivity;
    }

    @Provides
    LoginPresenter loginPresenter(LoginInteractor loginInteractor, LoginActivity view, LoginInteractorFacebook loginInteractorFacebook, LoginInteractorGoogle loginInteractorGoogle, RSCPreferences preferences, RSCRegistar registar) {
        return new LoginPresenter(view, loginInteractor, loginInteractorFacebook, loginInteractorGoogle, preferences, registar);
    }

    @Provides
    RSCRegistar registar() {
        return new RSCRegistar(RSCApp.getInstance());
    }

    @Provides
    LoginInteractor loginInteractor(AppServiceImpl.AppService service) {
        return new LoginInteractorImpl(service);
    }

    @Provides
    @Singleton
    CallbackManager callbackManager() {
        return new CallbackManagerImpl();
    }

    @Provides
    LoginInteractorFacebook loginInteractorFacebook(CallbackManager callbackManager, AppServiceImpl.AppService appService) {
        return new LoginInteractorFacebookImpl(loginActivity, callbackManager, appService);
    }

    @Provides
    GoogleApiClient googleApiClient(GoogleSignInOptions googleSignInOptions) {
        return new GoogleApiClient.Builder(RSCApp.getInstance())
                .enableAutoManage(loginActivity /* FragmentActivity */, onConnectionFailedListener /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    @Provides
    GoogleSignInOptions googleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode("251380982976-6dm321scj99sq9f09ug07vk2elteu2nd.apps.googleusercontent.com")
                .requestEmail()
                .build();
    }

    @Provides
    LoginInteractorGoogle loginInteractorGoogle(AppServiceImpl.AppService appService, GoogleApiClient client) {
        return new LoginInteractorGoogleImpl(loginActivity, client, appService);
    }


}
