package com.fbrd.rsc2015.domain.model.request;

import org.joda.time.DateTime;

/**
 * Created by noxqs on 22.11.15..
 */
public class Team {

    private int id;
    private String name;
    private int duration;
    private DateTime startedAt;
    private DateTime endedAt;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public DateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(DateTime startedAt) {
        this.startedAt = startedAt;
    }

    public DateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(DateTime endedAt) {
        this.endedAt = endedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
