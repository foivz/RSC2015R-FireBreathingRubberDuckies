package com.fbrd.rsc2015.domain.repository;

import android.content.Context;

import com.dmacan.lightandroid.domain.util.Preferences;
import com.dmacan.lightandroid.domain.util.Serializator;
import com.example.loginmodule.model.entity.User;

import rx.Observable;

/**
 * Created by david on 21.11.2015..
 */
public class RSCPreferences extends Preferences {

    private static final String KEY_USER = "com.rsc.user";

    public RSCPreferences(Context context) {
        super(context);
    }

    public Observable<Boolean> storeUserAsync(User user, String token) {
        return Observable.defer(() -> Observable.just(storeUser(user, token)));
    }

    public Boolean storeUser(User user, String token) {
        storeToken(token);
        return preferences().edit().putString(KEY_USER, Serializator.serialize(user)).commit();
    }

    public Observable<User> loadUserAsync() {
        return Observable.defer(() -> Observable.just(loadUser()));
    }


    public User loadUser() {
        return Serializator.deserialize(preferences().getString(KEY_USER, null), User.class);
    }

    public Observable<Boolean> isUserLoggedIn() {
        return Observable.defer(() -> Observable.just(loadUser() != null));
    }
}
