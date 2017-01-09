package com.rocketpowerteam.reallysmartphone;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.ArrayList;
import android.content.ActivityNotFoundException;
import android.widget.Button;

public class CallApp {

    final AppCompatActivity act;


    public CallApp(AppCompatActivity act){
        this.act = act;
    }

    public boolean makeCall(ArrayList<String> results) {

        String con_name;
        Cursor cur = Contact.getContacts(act);

        if(cur == null) {
            Log.i("empty","cur null");
            return false;
        }

        String phoneNo = "";

        for(String s:results) {
            //checks if the call is for emergency
            if(checkEmergency(s))
                return true;
            Log.d("NAME", s);
            phoneNo = Contact.getContactNumber(s,cur);
            if(phoneNo != null){
               if(callContact("tel:"+phoneNo))
                   return true;
               else
                   continue;
            }
        }
        return false;
    }

    private boolean callContact(String phoneNo){
        Intent callIntent = new Intent((Intent.ACTION_CALL), Uri.parse(phoneNo));
        try {
            act.startActivity(callIntent);
            Log.d("it s ok","");
            return true;
        }
        catch(ActivityNotFoundException e){
            return false;
        }
    }

    private boolean checkEmergency(String name){
        if (name.toLowerCase().equals(Contact.POLICE.getName().toLowerCase())) {
            if(callContact("tel:"+Contact.POLICE.getNumber()))
                return true;
        }

        if(name.toLowerCase().equals(Contact.FIRE_DEPARTMENT.getName().toLowerCase())){
            if(callContact("tel:"+Contact.FIRE_DEPARTMENT.getNumber()))
                return true;
        }

        return false;
    }

}
