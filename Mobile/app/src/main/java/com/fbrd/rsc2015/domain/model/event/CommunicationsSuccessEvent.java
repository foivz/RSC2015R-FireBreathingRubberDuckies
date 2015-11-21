package com.fbrd.rsc2015.domain.model.event;

/**
 * Created by noxqs on 21.11.15..
 */
public class CommunicationsSuccessEvent {

    private String url;

    public CommunicationsSuccessEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
