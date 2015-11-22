package com.fbrd.rsc2015.domain.interactor;

import com.example.loginmodule.model.event.error.ErrorEvent;

/**
 * Created by david on 22.11.2015..
 */
public class PairingErrorEvent extends ErrorEvent {
    public PairingErrorEvent(int code) {
        super(code);
    }
}
