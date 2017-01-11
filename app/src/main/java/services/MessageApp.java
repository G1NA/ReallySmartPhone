package services;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;

import commons.Contact;
import commons.Message;

import java.util.ArrayList;


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
        ArrayList<String> l =  new ArrayList<String>();

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
                    Log.i("id", contactId_string);
                    Log.i("ts", timestamp+"");
                    Log.i("body",body);
                    //TODO return something readable enough
                }
            } finally {
                cursor.close();
            }
        }
        l.add("Sam said Hello! on Saturday tenth of October 2017");

        return l;
    }

    public boolean sendMessage(Message m){

        String phoneNo = Contact.getContactNumber(m.getContact().getName(), Contact.getContacts(act));

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, m.getBody(), null, null);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

}
