package com.example.loginmodule.model.response;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONObject;

/**
 * Created by david on 15.11.2015..
 */
public class FacebookEmptyCallback implements GraphRequest.GraphJSONObjectCallback{

    private static FacebookEmptyCallback emptyCallback;

    private FacebookEmptyCallback() {

    }

    public static FacebookEmptyCallback get() {
        if (emptyCallback == null)
            emptyCallback = new FacebookEmptyCallback();
        return emptyCallback;
    }

    @Override
    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
        // Do nothing
    }

}
