package com.example.loginmodule.interactor;

import com.example.loginmodule.model.response.EmailTestResponse;

/**
 * Created by noxqs on 14.11.15..
 */
public interface TestEmailInteractor {

    void testEmail(String email, Callback callback);

    interface Callback{
        void onTestEmailSucces(EmailTestResponse emailTestResponse);
        void onTestEmailError(int code);
    }
}
