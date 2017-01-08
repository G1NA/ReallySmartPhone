package com.rocketpowerteam.reallysmartphone;

import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by gina4_000 on 8/1/2017.
 */
public class DateTimeApp {

    AppCompatActivity m;
    Calendar c = Calendar.getInstance();

    final String ordinals[] = {"first", "second", "third", "fourth",	"fifth", "sixth", "seventh", "eighth",
            "ninth", "tenth", "eleventh", "twelfth", "thirteenth", "fourteenth", "fifteenth",
            "sixteenth", "seventeenth", "eighteenth", "nineteenth", "twenty first", "twenty second",
            "twenty third", "twenty fourth", "twenty fifth", "twenty sixth", "twenty seventh",
            "twenty eighth", "twenty ninth", "thirtieth", "thirty first" };

    public DateTimeApp(AppCompatActivity main){
        m = main;
    }

    public String getReadableTime(){
        String time = "";
        time += c.HOUR+" "+c.MINUTE;
        return time;
    }

    public String getReadableDate(){
        String date = "";
        date += c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)+" ";
        date += ordinals[c.DATE-1]+" of ";
        date += c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH )+" ";

        return date;
    }


}
