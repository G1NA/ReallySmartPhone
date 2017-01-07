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
        Cursor cur = act.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        if(cur == null)
            return false;
        String phoneNo = "55555";

        for(String s:results) {
            Log.d("NAME", s);
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (cur.moveToNext()) {
                    con_name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    phoneNo = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if (name.toLowerCase().equals(con_name.toLowerCase())) {
                        phoneNo = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        callContact(phoneNo);
                    }
                }

            }
        }
        return false;
    }

    private void callContact(String phoneNo){
        Intent callIntent = new Intent((Intent.ACTION_CALL), Uri.parse(phoneNo));
        try {
            act.startActivity(callIntent);
            Log.d("it s ok","");
        }
        catch(ActivityNotFoundException e){
            Log.d("catch", name);
        }
    }

}
