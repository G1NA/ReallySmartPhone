package com.rocketpowerteam.reallysmartphone;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by gina4_000 on 9/1/2017.
 */
public class MessageApp {

    AppCompatActivity m;

    public MessageApp(AppCompatActivity mainApp) {
        m = mainApp;
    }

    public void readMessages() {
        Uri SMS_INBOX_CONTENT_URI = Uri.parse("content://sms/inbox");
        String WHERE_CONDITION = "read = 0";
        String SORT_ORDER = "date DESC";
        int count ;

        Cursor cursor = m.getContentResolver().query(
                SMS_INBOX_CONTENT_URI,
                new String[] { "person", "date", "body" },
                WHERE_CONDITION,
                null,
                SORT_ORDER);

        if (cursor != null) {
            try {
                count = cursor.getCount();
                if (count > 0) {
                    cursor.moveToFirst();
                    String[] columns = cursor.getColumnNames();
                    for (int i=0; i<columns.length; i++) {
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
    }

    public boolean composeMessage(String contact_name, String body){
        //http://stackoverflow.com/questions/8578689/sending-text-messages-programmatically-in-android
        //TODO
        return false;
    }

}
