package com.fbrd.rsc2015.domain.model.event;

import com.example.loginmodule.model.event.error.ErrorEvent;

/**
 * Created by noxqs on 22.11.15..
 */
public class DeathFailureEvent extends ErrorEvent {
    public DeathFailureEvent(int code) {
        super(code);
    }
}
