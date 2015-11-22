package com.fbrd.rsc2015.domain.model.event;

/**
 * Created by noxqs on 22.11.15..
 */
public class DeathSuccessEvent  {

    private boolean isDead;

    public DeathSuccessEvent(boolean isDead) {
        this.isDead = isDead;
    }

    public boolean isDead() {
        return isDead;
    }
}
