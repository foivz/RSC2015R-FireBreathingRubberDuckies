package com.fbrd.rsc2015.domain.model.event;

/**
 * Created by david on 21.11.2015..
 */
public class GcmMessageEvent {

    private String action;
    private String data;
    private String message;

    public GcmMessageEvent(String action, String data, String message) {
        this.action = action;
        this.data = data;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getAction() {
        return action;
    }

    public String getData() {
        return data;
    }
}
