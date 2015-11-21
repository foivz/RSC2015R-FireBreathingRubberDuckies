package com.example.loginmodule.model.event;

import com.example.loginmodule.model.entity.User;

/**
 * Created by david on 15.11.2015..
 */
public class LoginSuccessEvent {

    private User user;
    private String token;

    public LoginSuccessEvent(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

}
