package com.rocketpowerteam.reallysmartphone;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class CallApp{

    AppCompatActivity act;
    String name;

    public CallApp(String name, AppCompatActivity act){
        //TODO: case he had said "call bob"
        this.act = act;
        this.name = name;
    }

    public void makeCall(String name) {
        String id;
        String con_name;
        ContentResolver cr = act.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        String phoneNo = "";
        Log.d("NAME", name);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                con_name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (name.equals(con_name)) {
                    Log.d("equals_names", name);
                    if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            phoneNo = pCur.getString(pCur.getColumnIndex(
                                      ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        pCur.close();
                        Uri number = Uri.parse(phoneNo);
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                        act.startActivity(callIntent);
                        return;
                    }

                }
            }
        }

    }

}
