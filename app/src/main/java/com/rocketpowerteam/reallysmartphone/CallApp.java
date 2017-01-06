package com.rocketpowerteam.reallysmartphone;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import android.content.ActivityNotFoundException;

public class CallApp{

    final AppCompatActivity act;
    String name;

    public CallApp(String name, AppCompatActivity act){
        //TODO: case he had said "call bob"
        this.act = act;
        this.name = name;
    }

    public void makeCall(ArrayList<String> results) {
        String id;
        String con_name;
        ContentResolver cr = act.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        String phoneNo = "";

        for(String s:results) {
            Log.d("NAME", s);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    con_name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Log.d(con_name, name);
                    if (name.toLowerCase().equals(con_name.toLowerCase())) {
                        Log.d("equals_names", name);
                        if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                phoneNo = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                            pCur.close();
                            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNo));
                            try {
                                act.startActivity(callIntent);
                            }
                            catch(ActivityNotFoundException e){
                                Log.d("catch", name);
                            }
                        }
                        return;
                    }
                }
            }
        }
    }

}
