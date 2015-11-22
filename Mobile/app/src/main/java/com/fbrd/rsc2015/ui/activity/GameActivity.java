package com.fbrd.rsc2015.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
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
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.SimpleLineIconsIcons;

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
    private long teamId = 33;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        DaggerGameComponent.builder().gameModule(new GameModule(this)).build().inject(this);
        setupTabs();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setTitle(preferences.loadUser().getUsername() + "'s game");
        fab.setImageDrawable(new IconDrawable(this, SimpleLineIconsIcons.icon_microphone).actionBarSize().colorRes(android.R.color.white));
    }

    @OnClick(R.id.fab)
    protected void onVoiceCommand() {
        recognizer.listen("Say the command");
    }

    private void setupTabs() {
        fab.setVisibility(View.GONE);
        nfcFragment.setLabel("Tabs");
        adapter = new TabAdapter(getFragmentManager());
        tabs.setVisibility(View.GONE);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);
        adapter.addTabs(Arrays.asList(nfcFragment, statsFragment, mapFragment));
        tabs.setupWithViewPager(pager);
    }

    public void startGame() {
//        wvChat.setVisibility(View.VISIBLE);
        tabs.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
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
                Toast.makeText(GameActivity.this, "You are going out of bounds", Toast.LENGTH_LONG).show();
                break;
            case "8":
                this.teamId = event.getData().getTeamId();
                nfcFragment.askForPairing();
                pairNfc();
                break;
            case "2":
                preferences.preferences().edit().putLong("GameId", event.getData().getGameId()).commit();
//                startGame();
                break;
            case "9":
                break;
            case "10":
                startService(new Intent(this, LocationUpdateService.class));
                gameInteractor.fetchGames(preferences.getToken(), preferences.preferences().getLong("GameId", 0));
                startGame();
                openUrl("http://95.85.26.58:6767/" + event.getData().getUrl());
                String url = "http://firebreathingrubberduckies.azurewebsites.net/#/mapmobile/" + teamId + "/0?token=" + preferences.getToken();
                mapFragment.showMap(url);
                Log.i("DAM_URL", url);
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
                    pager.setCurrentItem(2);
                    break;
                case CommandParser.STATS:
                    pager.setCurrentItem(1);
                    break;
                case "team":
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
//        Toast.makeText(GameActivity.this, "An error has occured", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            onVoiceCommand();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN && url != null) {
            openUrl(url);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void openUrl(String url) {
        this.url = url;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null);
            startActivity(intent);
        }
    }
}
