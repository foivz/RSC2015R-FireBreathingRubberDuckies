package com.fbrd.rsc2015.domain.model.event;

import org.joda.time.DateTime;

/**
 * Created by noxqs on 22.11.15..
 */
public class GamesSuccessEvent {

    private String name;
    private long duration;
    private DateTime startedAt;
    private DateTime endedAt;

    public GamesSuccessEvent(String name, long duration, DateTime startedAt, DateTime endedAt) {
        this.name = name;
        this.duration = duration;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public String getName() {
        return name;
    }

    public long getDuration() {
        return duration;
    }

    public DateTime getStartedAt() {
        return startedAt;
    }

    public DateTime getEndedAt() {
        return endedAt;
    }
}
