package com.fbrd.rsc2015.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dmacan.lightandroid.ui.custom.tabs.TabAdapter;
import com.dmacan.lightandroid.ui.custom.view.SexyWebView;
import com.example.loginmodule.model.bus.ZET;
import com.fbrd.lightandroidgoodies.SpeechRecognizer;
import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.app.di.component.DaggerGameComponent;
import com.fbrd.rsc2015.app.di.module.GameModule;
import com.fbrd.rsc2015.domain.interactor.GameInteractor;
import com.fbrd.rsc2015.domain.interactor.PairingErrorEvent;
import com.fbrd.rsc2015.domain.interactor.PairingInteractor;
import com.fbrd.rsc2015.domain.manager.NFCManager;
import com.fbrd.rsc2015.domain.model.event.GamesFailureEvent;
import com.fbrd.rsc2015.domain.model.event.GamesSuccessEvent;
import com.fbrd.rsc2015.domain.model.event.GcmMessageEvent;
import com.fbrd.rsc2015.domain.model.event.PairingSuccessEvent;
import com.fbrd.rsc2015.domain.repository.RSCPreferences;
import com.fbrd.rsc2015.domain.service.LocationUpdateService;
import com.fbrd.rsc2015.domain.service.NFCScannedEvent;
import com.fbrd.rsc2015.domain.util.CommandParser;
import com.fbrd.rsc2015.ui.fragment.MapFragment;
import com.fbrd.rsc2015.ui.fragment.NfcFragment;
import com.fbrd.rsc2015.ui.fragment.StatsFragment;

import java.util.Arrays;
import java.util.Collections;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.halfbit.tinybus.Subscribe;

public class GameActivity extends AppCompatActivity {

    @Inject
    MapFragment mapFragment;
    @Inject
    StatsFragment statsFragment;
    @Inject
    NfcFragment nfcFragment;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.viewpager)
    ViewPager pager;
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.wvChat)
    SexyWebView wvChat;
    TabAdapter adapter;
    @Inject
    NFCManager nfcManager;
    @Inject
    GameInteractor gameInteractor;
    @Inject
    RSCPreferences preferences;
    @Inject
    PairingInteractor pairingInteractor;
    @Inject
    SpeechRecognizer recognizer;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private long gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        DaggerGameComponent.builder().gameModule(new GameModule(this)).build().inject(this);
        setupTabs();
        startService(new Intent(this, LocationUpdateService.class));
        setupChat();
        gameInteractor.fetchGames(preferences.getToken(), 1);
    }

    @OnClick(R.id.fab)
    protected void onVoiceCommand() {
        recognizer.listen("Say the command");
    }

    private void setupTabs() {
        fab.setVisibility(View.GONE);
        adapter = new TabAdapter(getFragmentManager());
        tabs.setVisibility(View.GONE);
        pager.setAdapter(adapter);
        adapter.addTabs(Collections.singletonList(nfcFragment));
        tabs.setupWithViewPager(pager);
    }

    public void setupChat() {
        wvChat.load("http://95.85.26.58:6767/");
    }

    public void startGame() {
        wvChat.setVisibility(View.VISIBLE);
        tabs.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
        adapter.clearTabs();
        adapter.addTabs(Arrays.asList(statsFragment, mapFragment));
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ZET.register(this);
//        ZET.register(nfcFragment);
        nfcManager.setupForegroundDispatch(this, GameActivity.class);
    }

    @Override
    protected void onStop() {
        ZET.unregister(this);
//        ZET.unregister(nfcFragment);
        nfcManager.stopForegroundDispatch();
        super.onStop();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        nfcManager.handleIntent(intent);
    }

    @Subscribe
    public void onGCMMessage(GcmMessageEvent event) {
        switch (event.getAction()) {
            case "3":
                Log.i("DAM", "My team: " + event.getData().getMyTeam());
                Log.i("DAM", "Enemy team: " + event.getData().getEnemyTeam());
                break;
            case "5":
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(3000);
                break;
            case "8":
                nfcFragment.askForPairing();
                pairNfc();
                this.gameId = event.getData().getTeamId();
                break;
            case "2":
                preferences.preferences().edit().putLong("GameId", event.getData().getGameId()).commit();
                startGame();
                break;
            case "9":
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String command = recognizer.interpretResult(requestCode, resultCode, data);
        if (command != null) {
            command = CommandParser.compare(command);
            switch (command) {
                case CommandParser.MAP:
                    pager.setCurrentItem(1);
                    break;
                case CommandParser.STATS:
                    pager.setCurrentItem(0);
                    break;
            }
        }
    }

    private void pairNfc() {
        nfcManager.connect(success -> {
            if (success) {
                nfcManager.handleIntent(getIntent());
            }
        });
    }

    @Subscribe
    public void onNFCMessage(NFCScannedEvent event) {
        Toast.makeText(GameActivity.this, event.getResult(), Toast.LENGTH_SHORT).show();
        pairingInteractor.pair(event.getResult(), 0);
    }

    @Subscribe
    public void onGameStatsLoaded(GamesSuccessEvent event) {
        statsFragment.setGameName(event.getName());
        statsFragment.startTimer(event.getDuration(), event.getStartedAt());
    }

    @Subscribe
    public void onGameStatsError(GamesFailureEvent event) {
        Toast.makeText(GameActivity.this, "An error has occured", Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onPairingSuccess(PairingSuccessEvent event) {
        Toast.makeText(GameActivity.this, "Pairing success", Toast.LENGTH_SHORT).show();
        nfcFragment.askToWait();
    }

    @Subscribe
    public void onPairingError(PairingErrorEvent errorEvent) {
        Toast.makeText(GameActivity.this, "Pairing error", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, LocationUpdateService.class));
        super.onDestroy();
    }
}
