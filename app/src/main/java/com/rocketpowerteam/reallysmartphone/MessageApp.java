package com.rocketpowerteam.reallysmartphone;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by gina4_000 on 9/1/2017.
 */
public class MessageApp {

    AppCompatActivity act;

    public MessageApp(AppCompatActivity mainApp) {
        act = mainApp;
    }

    public ArrayList<String> readMessages() {
        Uri SMS_INBOX_CONTENT_URI = Uri.parse("content://sms/inbox");
        String WHERE_CONDITION = "read = 0";
        String SORT_ORDER = "date DESC";
        int count ;

        Cursor cursor = act.getContentResolver().query(
                SMS_INBOX_CONTENT_URI,
                new String[] { "person", "date", "body" },
                null,
                null,
                SORT_ORDER);

        if (cursor != null) {
            try {
                count = cursor.getCount();
                if (count > 0) {
                    cursor.moveToFirst();
                    String[] columns = cursor.getColumnNames();
                    for (int i=0; i < columns.length; i++) {
                        Log.v("columns " + i + ": " + columns[i] + ": " + cursor.getString(i),"");
                    }
                    long contactId = cursor.getLong(0);
                    String contactId_string = String.valueOf(contactId);
                    long timestamp = cursor.getLong(1);
                    String body = cursor.getString(2);
                    Log.i("", contactId_string+" "+timestamp+" "+body);
                    //TODO return something readable enough
                }
            } finally {
                cursor.close();
            }
        }
        ArrayList<String> l =  new ArrayList<String>();
        l.add("sam said blah");
        return l;
    }

    public void sendLongSMS() {
        String phoneNumber = "0123456789";
        String message = "Hello World! Now we are going to demonstrate " +
                "how to send a message with more than 160 characters from your Android application.";
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
    }

    public boolean sendMessage(Message m){

        String phoneNo = Contact.getContactNumber(m.getContact().getName(), Contact.getContacts(act));

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, m.getBody(), null, null);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

}
