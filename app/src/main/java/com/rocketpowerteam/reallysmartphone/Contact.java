package com.rocketpowerteam.reallysmartphone;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gina4_000 on 7/1/2017.
 */

public class Contact {

    public static Contact POLICE = new Contact("police","100");
    public static Contact FIRE_DEPARTMENT = new Contact("fire department","199");

    private String name;
    private String number;
    private Pattern digits = Pattern.compile("[0-9]*");
    private Pattern numbers = Pattern.compile("zero|one|two|three|four|five|six|seven|eight|nine");

    public Contact(){
    }

    public Contact(String name, String phone){
        this.name = name;
        this.number = phone;
    }

    public void setName(String name){ this.name = name;}
    public void setNumber(String number){ this.number = number;}
    public String getName(){ return this.name; }
    public String getNumber(){ return this.number; }

    public void fixNumber(){
        String[] splitted = number.split(" ");
        if(splitted.length == 1){
            // either it will be the word of a digit and it will return a digit or it will return ""
            String fixed = toDigit(number);
            if(!fixed.equals("")){
                number = fixed;
            }else {
                number = number.replaceAll("\\D+", "");
                Matcher m = digits.matcher(number);
                if (m.matches()) {
                    return;
                } else {
                    number = "";
                }
            }
        }else{
            String fixed = "";
            for(String s: splitted){
                Matcher m = digits.matcher(s);
                if(m.matches()){
                    fixed += s;
                }else{
                    fixed += toDigit(s);
                }
            }
            number = fixed;
        }

    }

    public static Cursor getContacts(AppCompatActivity app){
        Cursor cur = app.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER }, null, null, null);
        return cur;
    }

    public static boolean hasContacts(AppCompatActivity app){
        return  getContacts(app) != null;
    }

    public static String getContactNumber(String name, Cursor cur){
        String con_name = "";
        String phoneNo = "";
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            while (cur.moveToNext()) {
                phoneNo = cur.getString(1);
                con_name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                Log.d("c_name", con_name);
                if (name.toLowerCase().equals(con_name.toLowerCase())) {
                    return phoneNo;
                }
            }

        }else{
            return null;
        }

        return null;
    }

    private String toDigit(String s){

        if(s.equals("zero")){
            return "0";
        }else if(s.equals("one")) {
            return "1";
        }else if(s.equals("two")) {
            return "2";
        }else if(s.equals("three")) {
            return "3";
        }else if(s.equals("four")) {
            return "4";
        }else if(s.equals("five")) {
            return "5";
        }else if(s.equals("six")) {
            return "6";
        }else if(s.equals("seven")) {
            return "7";
        }else if(s.equals("eight")) {
            return "8";
        }else if(s.equals("nine")) {
            return "9";
        }else {
            return "";
        }
    }
}
