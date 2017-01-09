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

}
