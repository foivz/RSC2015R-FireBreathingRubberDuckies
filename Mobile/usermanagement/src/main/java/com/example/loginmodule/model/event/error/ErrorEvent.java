package com.example.loginmodule.model.event.error;

/**
 * Created by david on 15.11.2015..
 */
public abstract class ErrorEvent {

    private int code;

    public ErrorEvent(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
