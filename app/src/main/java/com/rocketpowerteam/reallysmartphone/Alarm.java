package com.rocketpowerteam.reallysmartphone;

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
    String time;
    String date;
    final static int RQS_1 = 1;

    public Alarm(String time, String date, AppCompatActivity act){
        this.time = time;
        this.date = date;
        this.act = act;
    }

    public String getTime(){return time;}

    public String getDate(){return date;}

    public void setTime(String time){ this.time = time;}

    public void setDate(String date){ this.date = date;}

    private int getHour(String time){
        return Integer.parseInt(time.split(":")[0]);
    }

    private int getMinutes(String time){
        return Integer.parseInt(time.split(":")[1]);
    }

    private int getMonth(String date){
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();

        String[] possibleMonths = date.split(" ");
        for(int i = 0; i < possibleMonths.length; i++){
            for(int j = 0; j < months.length; j++) {
                if(possibleMonths[i].toLowerCase().equals(months[j])) {

                    try {
                        Date d = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(months[j]);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(d);
                        return cal.get(Calendar.MONTH);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH);   //if user does not mention month, return the current one
    }

    private int getDayOfMonth(String day){
        String[] possibleDays = date.split(" ");
        int a;
        for (int i = 0; i < possibleDays.length; i++){
            possibleDays[i].replace("th", "");
            try {
                a = Integer.parseInt(day);
                return a;
            } catch (NumberFormatException e) {

            }
        }
        return 0;
    }

    public void setAlarm(){
        Log.d(date, time);
        Date dat  = new Date();//initializes to now
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(dat);
        cal_alarm.setTime(dat);
        cal_alarm.set(Calendar.HOUR_OF_DAY,getHour(time));//set the alarm time
        cal_alarm.set(Calendar.MINUTE, getMinutes(time));
        cal_alarm.set(Calendar.SECOND,0);
        if(cal_alarm.before(cal_now)){//if its in the past increment
            cal_alarm.add(Calendar.DATE,1);
        }

        cal_alarm.set(Calendar.YEAR, 2017);
        if(getMonth(date) != -1)
            cal_alarm.set(Calendar.MONTH, getMonth(date));
        else{
            Log.d("lathos mhnas", "");
        }
        cal_alarm.set(Calendar.DAY_OF_MONTH, getDayOfMonth(date));

        Intent intent = new Intent(act, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(act, RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) act.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);
    }

}
