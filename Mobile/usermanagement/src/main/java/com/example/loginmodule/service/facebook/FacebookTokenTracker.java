package com.example.loginmodule.service.facebook;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;

/**
 * Created by david on 15.11.2015..
 */
public class FacebookTokenTracker extends AccessTokenTracker {

    @Override
    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
        AccessToken.setCurrentAccessToken(currentAccessToken);
    }
}