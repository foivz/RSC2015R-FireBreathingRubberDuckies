package com.fbrd.rsc2015.domain.interactor;

import com.example.loginmodule.model.bus.ZET;
import com.example.loginmodule.util.ServiceUtil;
import com.fbrd.rsc2015.domain.model.event.FeedFailureEvent;
import com.fbrd.rsc2015.domain.model.event.FeedSuccessEvent;
import com.fbrd.rsc2015.domain.model.response.FeedItem;
import com.fbrd.rsc2015.domain.model.response.FeedResponse;
import com.fbrd.rsc2015.domain.repository.RSCRepository;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by noxqs on 21.11.15..
 */
public class NotificationInteractor {

    private RSCRepository.Api service;
    private ArrayList<FeedResponse> notificationResponses;

    public NotificationInteractor(RSCRepository.Api service) {
        this.service = service;
    }

    public void fetchNotifications(String token) {
        service.fetchNotifications(ServiceUtil.formatToken(token))
                .flatMapIterable(FeedResponse::getData)
                .map(data -> {
                    FeedItem item = new FeedItem();
                    item.setDescription(data.getDescription());
                    item.setImage(data.getPicture());
                    item.setName(data.getTitle());
                    item.setUrl(data.getOfferUrl());
                    return item;
                })
                .toList()
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    ZET.post(new FeedSuccessEvent(result));
                }, error->{
                    ZET.post(new FeedFailureEvent(ServiceUtil.getStatusCode(error)));
                });
    }

}
