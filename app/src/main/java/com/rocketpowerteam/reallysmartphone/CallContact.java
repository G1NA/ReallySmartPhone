package com.rocketpowerteam.reallysmartphone;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;


public class CallContact extends Activity implements View.OnClickListener{

    public CallContact(){
        speakOut();
    }

    public void speakOut(){
        tts.speak(getString(R.string.ask_contact_to_call),TextToSpeech.QUEUE_FLUSH,null);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && (resultCode == RESULT_OK)) {
            makeCall(data);
        }
        else if(resultCode == RESULT_CANCELED){
            tts.speak(getString(R.string.wrong_input), TextToSpeech.QUEUE_FLUSH, null);
        }

    }

    private void makeCall(Intent data) {

        ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        String phoneNo = "";
        for(String s:results){
            Log.d(s, s);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (name.equals(s)) {
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
                            startActivity(callIntent);
                            return;
                        }

                    }
                }
            }
        }

    }

}
