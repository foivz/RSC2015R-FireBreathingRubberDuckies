package com.example.loginmodule.util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by david on 20.11.2015..
 */
public class GoogleApiUtil {

    private static final int GOOGLE_SIGN_IN = 9000;

    public static void startGoogleSignIn(GoogleApiClient client, Activity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(client);
        activity.startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    public static String googleSignInToken(int requestCode, int resultCode, Intent data) {
        Log.i("DAM", "RQ: " + requestCode);
        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                return result.getSignInAccount().getServerAuthCode();
            }
        }
        return null;
    }

}
