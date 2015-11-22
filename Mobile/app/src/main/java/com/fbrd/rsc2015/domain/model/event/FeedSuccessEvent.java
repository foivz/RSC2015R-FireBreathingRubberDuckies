package com.fbrd.rsc2015.domain.model.event;

import com.fbrd.rsc2015.domain.model.response.FeedItem;

import java.util.List;

/**
 * Created by noxqs on 21.11.15..
 */
public class FeedSuccessEvent {

    private List<FeedItem> list;

    public FeedSuccessEvent(List<FeedItem> list) {
        this.list = list;
    }

    public List<FeedItem> getList() {
        return list;
    }
}

