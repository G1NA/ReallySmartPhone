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
    String name;

    public CallApp(String name, AppCompatActivity act){
        //TODO: case he had said "call bob"
        this.act = act;
        this.name = name;
    }

    public boolean makeCall(ArrayList<String> results) {

        String con_name;
        Cursor cur = act.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER }, null, null, null);
        if(cur == null) {
            Log.i("empty","cur null");
            return false;
        }
        String phoneNo = "";

        for(String s:results) {
            Log.d("NAME", s);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    phoneNo = cur.getString(1);
                    con_name = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    Log.d("c_name", con_name);
                    if (name.toLowerCase().equals(con_name.toLowerCase())) {
                       if(callContact("tel:"+phoneNo))
                           return true;
                       else
                           continue;
                    }
                }

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
            Log.d("catch", name);
            return false;
        }
    }

}
