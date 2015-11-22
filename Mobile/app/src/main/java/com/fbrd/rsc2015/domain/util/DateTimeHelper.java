package com.fbrd.rsc2015.domain.util;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by noxqs on 22.11.15..
 */
public class DateTimeHelper {

    private DateTime startedAt;

    private DateTime endedAt;

    private String finalTime = "";
    private int duration;

    public DateTimeHelper(DateTime startedAt, DateTime endedAt, int duration) {
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.duration = duration;
    }

    private String calculateElapsedTime() {
        int hours, minutes, seconds;

        if(duration > 60){
            startedAt = startedAt.plusMinutes(duration);
        }
        Date currDate = new Date();

        hours = Hours.hoursBetween(new DateTime(startedAt), new DateTime(currDate)).getHours();
        finalTime = "" + hours;
        minutes = Minutes.minutesBetween(new DateTime(startedAt), new DateTime(currDate)).getMinutes();
        finalTime = ":" + minutes;
        seconds = Seconds.secondsBetween(new DateTime(startedAt), new DateTime(currDate)).getSeconds();
        finalTime = ":" + seconds;

        Log.e("FinalTime", finalTime);

        return finalTime;

    }
}



