package com.fbrd.rsc2015.domain.model.event;

/**
 * Created by noxqs on 21.11.15..
 */
public class LocationPostEvent {

    private int level;

    public LocationPostEvent(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
