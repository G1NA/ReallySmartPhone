package com.rocketpowerteam.reallysmartphone;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;

public class CallContact {

    public CallContact(){

    }



    private void makeCall() {
//        Uri number = Uri.parse("tel:123456789");
//        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
//        startActivity(callIntent);
//        tts.speak(getString(R.string.ask_contact_name), TextToSpeech.QUEUE_FLUSH,null);
//        btn = (Button) findViewById(R.id.talktome);
//        btn.setOnClickListener(this);
//        Log.i("call2", result);
//        ContentResolver cr = getContentResolver();
//        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
//                null, null, null, null);
//
//        if (cur.getCount() > 0) {
//            while (cur.moveToNext()) {
//                String id = cur.getString(
//                        cur.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = cur.getString(cur.getColumnIndex(
//                        ContactsContract.Contacts.DISPLAY_NAME));
//                if (name. result) {
//                    String phoneNo = "0";
//                    if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
//                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
//                                new String[]{id}, null);
//                        while (pCur.moveToNext()) {
//                            phoneNo = pCur.getString(pCur.getColumnIndex(
//                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//                        }
//                        pCur.close();
//                    }
//                    Uri number = Uri.parse(phoneNo);
//                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
//                    startActivity(callIntent);
//                }
//            }
//        }
    }

}
