package com.fbrd.rsc2015.domain.interactor;

import com.example.loginmodule.model.bus.ZET;
import com.example.loginmodule.util.ServiceUtil;
import com.fbrd.rsc2015.domain.model.event.GamesFailureEvent;
import com.fbrd.rsc2015.domain.model.event.GamesSuccessEvent;
import com.fbrd.rsc2015.domain.model.request.Team;
import com.fbrd.rsc2015.domain.model.response.GamesResponse;
import com.fbrd.rsc2015.domain.repository.RSCRepository;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by noxqs on 22.11.15..
 */
public class GameInteractor {

    private RSCRepository.Api api;

    public GameInteractor(RSCRepository.Api api) {
        this.api = api;
    }

   public void fetchGames(String token, long gameId) {
        api.getGame(ServiceUtil.formatToken(token), gameId)
                .flatMapIterable(GamesResponse::getList)
                .map(grrrr -> {
                    Team team = new Team();
                    team.setId(grrrr.getId());
                    team.setName(grrrr.getTeam().getName());
                    return team;
                })
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        success -> {
                            ZET.post(new GamesSuccessEvent(
                                    success.getName(),
                                    success.getDuration(),
                                    success.getStartedAt(),
                                    success.getEndedAt()));
                        },
                        error -> {
                            ZET.post(new GamesFailureEvent(ServiceUtil.getStatusCode(error)));
                        });
    }
}
