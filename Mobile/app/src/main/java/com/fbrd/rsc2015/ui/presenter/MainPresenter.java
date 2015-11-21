package com.fbrd.rsc2015.ui.presenter;

import com.example.loginmodule.model.bus.ZET;
import com.fbrd.rsc2015.domain.interactor.NotificationInteractor;
import com.fbrd.rsc2015.domain.model.event.FeedFailureEvent;
import com.fbrd.rsc2015.domain.model.event.FeedSuccessEvent;
import com.fbrd.rsc2015.domain.repository.RSCPreferences;
import com.fbrd.rsc2015.ui.activity.MainActivity;

import de.halfbit.tinybus.Subscribe;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by david on 21.11.2015..
 */
public class MainPresenter {

    private MainActivity view;
    private RSCPreferences preferences;
    private NotificationInteractor interactor;

    public MainPresenter(MainActivity view, RSCPreferences preferences, NotificationInteractor interactor) {
        this.view = view;
        this.preferences = preferences;
        this.interactor = interactor;
    }

    public void onViewCreate() {
        view.showLoading("Loading your feed");
        preferences.loadUserAsync()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    interactor.fetchNotifications(user.getToken());
                    view.showAvatar(user.getImage());
                    view.showUsername(user.getUsername());
                });
    }

    @Subscribe
    public void onFeedFetched(FeedSuccessEvent event) {
        view.dismissLoading();
        view.showFeed(event.getList());
    }


    @Subscribe
    public void onFeedFetchError(FeedFailureEvent event) {
        view.dismissLoading();
        view.showError("Couldn't fetch your feed");
    }

    public void logout() {
        preferences.clear();
        view.showLogin();
    }

    public void onViewResume() {
        ZET.register(this);
    }

    public void onViewPause() {
        ZET.unregister(this);
    }

}
