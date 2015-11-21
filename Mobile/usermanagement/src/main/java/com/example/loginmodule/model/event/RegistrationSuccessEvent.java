package com.example.loginmodule.model.event;

import com.example.loginmodule.model.entity.User;

/**
 * Created by david on 15.11.2015..
 */
public class RegistrationSuccessEvent {

    private String token;
    private User user;

    public RegistrationSuccessEvent(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
