package com.rocketpowerteam.reallysmartphone;

import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;

public class Alarm {

    final AppCompatActivity act;
    private String hour;
    private String minutes;
    final static int RQS_1 = 1;

    public Alarm(String hour, String minutes, AppCompatActivity act){
        this.hour = hour;
        this.minutes = minutes;
        this.act = act;
    }

    public void setAlarm(){
        Intent alarm = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarm.putExtra(AlarmClock.EXTRA_HOUR, Integer.parseInt(hour));
        alarm.putExtra(AlarmClock.EXTRA_MINUTES, Integer.parseInt(minutes));
        alarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        act.startActivity(alarm);
    }

}
