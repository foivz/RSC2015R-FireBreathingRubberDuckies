package com.fbrd.rsc2015.ui.presenter;

import com.fbrd.rsc2015.domain.repository.RSCPreferences;
import com.fbrd.rsc2015.ui.activity.MainActivity;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by david on 21.11.2015..
 */
public class MainPresenter {

    private MainActivity view;
    private RSCPreferences preferences;

    public MainPresenter(MainActivity view, RSCPreferences preferences) {
        this.view = view;
        this.preferences = preferences;
    }

    public void onViewCreate() {
        view.showLoading("Loading your feed");
        preferences.loadUserAsync()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    view.showAvatar(user.getImage());
                    view.showUsername(user.getUsername());
                });
    }

    public void onFeedFetched() {
        view.dismissLoading();
    }

    public void onFeedFetchError() {
        view.dismissLoading();
        view.showError("Couldn't fetch your feed");
    }

    public void logout(){
        preferences.clear();
        view.showLogin();
    }

}
