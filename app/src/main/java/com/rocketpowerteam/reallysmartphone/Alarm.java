package com.rocketpowerteam.reallysmartphone;

import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Alarm {

    final AppCompatActivity act;
    String[] time;
    String[] new_time;
    final static int RQS_1 = 1;

    public Alarm(String[] time, AppCompatActivity act){
        this.time = time;
        this.act = act;
    }

    public String[] getTime(){
        if (time.length == 1) {
            new_time = new String[2];
            new_time[0] = time[0];
            new_time[1] = "0";
            return new_time;
        }
        return time;
    }

    public void setAlarm(){
        Log.d("time", time[0]);

        Intent alarm = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarm.putExtra(AlarmClock.EXTRA_HOUR, Integer.parseInt(getTime()[0]));
        alarm.putExtra(AlarmClock.EXTRA_MINUTES, Integer.parseInt(getTime()[1]));
        alarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        act.startActivity(alarm);

    }

}
