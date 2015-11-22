package com.fbrd.rsc2015.domain.util;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

import android.util.Log;

/**
 * Created by noxqs on 22.11.15..
 */
public class DateTimeHelper {

    private DateTime startedAt;

    private String finalTime = "";

    private int duration;

    public DateTimeHelper(DateTime startedAt, int duration) {
        this.startedAt = startedAt;
        this.duration = duration;
    }

    public String calculateElapsedTime() {
        int hours, minutes, seconds;
        DateTime currDate = new DateTime();
        String time;

        startedAt = startedAt.plusMinutes(duration);
        Log.e("startedAT", "" + startedAt);
        long startedAtMillis = startedAt.getMillis();
        long currMillis = currDate.getMillis();
        long totalMillis = startedAtMillis - currMillis;
        Log.e("TOTAL", totalMillis + "");
        long minutesMillis = (totalMillis / 1000) / 60;
        Log.e("minutesMillis", minutesMillis + "");
        long secondsMillis = (totalMillis / 1000) - minutesMillis * 60;
        Log.e("secondsMillis", secondsMillis + "");

        if (duration > 60) {
            long hoursMillis = minutesMillis / 60;
            minutesMillis = minutesMillis - hoursMillis * 60;
            time = hoursMillis + ":" + minutesMillis + ":" + secondsMillis;
        } else {
            time = "00:" + minutesMillis + secondsMillis;
        }

        return time;
    }
}



