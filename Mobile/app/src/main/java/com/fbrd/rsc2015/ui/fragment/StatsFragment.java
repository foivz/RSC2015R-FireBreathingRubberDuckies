package com.fbrd.rsc2015.ui.fragment;

import com.fbrd.rsc2015.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.Date;

import butterknife.Bind;

/**
 * Created by david on 21.11.2015..
 */
public class StatsFragment extends TabFragment {

    @Bind(R.id.txtGameName)
    TextView txtGameName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, null, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setGameName(String name) {
        txtGameName.setText(name);
    }

    public void startTimer(long duration, DateTime startTime) {

    }


    public void showOdds(int myTeam, int enemyTeam){
        
    }
}
