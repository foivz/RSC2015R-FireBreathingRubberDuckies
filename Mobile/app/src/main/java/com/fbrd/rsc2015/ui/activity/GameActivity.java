package com.fbrd.rsc2015.ui.activity;

import com.dmacan.lightandroid.ui.custom.tabs.TabAdapter;
import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.app.di.component.DaggerGameComponent;
import com.fbrd.rsc2015.app.di.module.GameModule;
import com.fbrd.rsc2015.domain.service.LocationUpdateService;
import com.fbrd.rsc2015.ui.fragment.MapFragment;
import com.fbrd.rsc2015.ui.fragment.NfcFragment;
import com.fbrd.rsc2015.ui.fragment.StatsFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    TabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        DaggerGameComponent.builder().gameModule(new GameModule(this)).build().inject(this);
        setupTabs();
        startService(new Intent(this, LocationUpdateService.class));
    }

    private void setupTabs() {
        adapter = new TabAdapter(getFragmentManager());
        pager.setAdapter(adapter);
        adapter.addTabs(Arrays.asList(nfcFragment));
        tabs.setupWithViewPager(pager);
    }


}
