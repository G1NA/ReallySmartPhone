package com.rocketpowerteam.reallysmartphone;

import android.support.v7.app.AppCompatActivity;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by gina4_000 on 8/1/2017.
 */
public class DateTimeApp {

    AppCompatActivity m;
    Locale current;
    TimeZone tz;
    Calendar c;


    final String ordinals[] = {"first", "second", "third", "fourth","fifth", "sixth", "seventh", "eighth",
            "ninth", "tenth", "eleventh", "twelfth", "thirteenth", "fourteenth", "fifteenth",
            "sixteenth", "seventeenth", "eighteenth", "nineteenth", "twenty first", "twenty second",
            "twenty third", "twenty fourth", "twenty fifth", "twenty sixth", "twenty seventh",
            "twenty eighth", "twenty ninth", "thirtieth", "thirty first" };

    public DateTimeApp(AppCompatActivity main){
        m = main;
    }
    private void init(){

        if(current == null)
            current = m.getResources().getConfiguration().locale;
        if(tz == null)
            tz = TimeZone.getDefault();

        c = Calendar.getInstance(tz, current);
    }

    public String getReadableTime(){
        init();
        String time = "";
        time += c.get(Calendar.HOUR_OF_DAY)+" "+c.get(Calendar.MINUTE);
        return time;
    }

    public String getReadableDate(){
        init();
        String date = "";
        date += c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)+" ";
        date += ordinals[c.get(Calendar.DATE)-1]+" of ";
        date += c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH )+" "+c.get(Calendar.YEAR);

        return date;
    }


    public static String[] getTime(String str){
        return str.split(" |:|o'clock");
    }


}
