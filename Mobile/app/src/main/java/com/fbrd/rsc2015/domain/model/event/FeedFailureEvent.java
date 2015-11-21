package com.fbrd.rsc2015.domain.model.event;

import com.example.loginmodule.model.event.error.ErrorEvent;

/**
 * Created by noxqs on 21.11.15..
 */
public class FeedFailureEvent extends ErrorEvent {

    public FeedFailureEvent(Throwable code) {
        super(code);
    }
}
