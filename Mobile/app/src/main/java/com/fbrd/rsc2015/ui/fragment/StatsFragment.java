package com.fbrd.rsc2015.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fbrd.rsc2015.R;
import com.fbrd.rsc2015.domain.util.DateTimeHelper;

import org.joda.time.DateTime;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by david on 21.11.2015..
 */
public class StatsFragment extends TabFragment {

    @Bind(R.id.txtGameName)
    TextView txtGameName;
    @Bind(R.id.timerHours)
    TextView timerHours;
    @Bind(R.id.timerMinutes)
    TextView timerMinutes;
    @Bind(R.id.timerSeconds)
    TextView timerSeconds;
    @Bind(R.id.myTeamAlive)
    TextView myTeamAlive;
    @Bind(R.id.enemyTeamAlive)
    TextView enemyTeamAlive;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, null, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setGameName(String name) {
        txtGameName.setText(name);
    }

    public void startTimer(int duration, DateTime startTime) {
        DateTimeHelper dateTimeHelper = new DateTimeHelper(startTime, duration);
        String remainingTime = dateTimeHelper.calculateElapsedTime();

        timerHours.setText(remainingTime.substring(0,1));
        timerMinutes.setText(remainingTime.substring(3,4));
        timerSeconds.setText(remainingTime.substring(5,6));
    }


    public void showOdds(int myTeam, int enemyTeam) {
        myTeamAlive.setText("Players alive: " + myTeam);
        enemyTeamAlive.setText("Players alive:" + enemyTeam);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
