package com.rocketpowerteam.reallysmartphone;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.ContactsContract;
import android.content.Intent;
import android.database.Cursor;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.provider.ContactsContract.*;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn;
    final int check = 111;
    final int get_contact_name =112;
    TextToSpeech tts;

    enum MenuItem {
        ADD_CONTACT(1, "add contact"), CALL_CONTACT(2, "call"), PLAY_MUSIC(3, "play music"),
        READ_MESSAGE(4, "read message"), COMPOSE_MESSAGE(5, "create compose message");
        int code;
        String strCommand;
        MenuItem(int code, String strCommand){
            this.code = code;
            this.strCommand = strCommand;
        }

        public String getStrCommand() {
            return strCommand;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.talktome);
        btn.setOnClickListener(this);
        tts = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener(){

            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int res = tts.setLanguage(Locale.ENGLISH);
                    if(res == TextToSpeech.LANG_NOT_SUPPORTED || res == TextToSpeech.LANG_MISSING_DATA){
                        Log.e("TTS", "Language is not supported");
                    }
                }else{
                    Log.e("TTS","Init failed");
                }
            }
        });
        tts.setSpeechRate(0.8f);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        //δεν εχω πολυκαταλαβει νομιζω ακομη τι παιζει με τα codes, στην startActivityForResult καλείται
        //η onActivityResults η οποια εχει για requestCode αυτο το τσεκ που χουμε βαλει εμεις, και το
        //result code dimiourgeitai automata. opote blepoume an aytopoy dimiourghuhke einai ok kai meta
        //tsekaroyme se poia leitoyrgia anaferetai? ma ayto den to kseroume apo prin...an ta exw katalavei kala mexri edv
        //skeftomoun na psaxname apla an yparxei kapoia ap tis lekseis px "add, new, contact" και να λεγαμε
        // τοτε οτι ειμαστε στην περιπτωση 1 πχ, γιατι απο πριν δεν το ξερουμε, αυτό
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case check:
                    find_menu_action(data);
                    break;
                case get_contact_name:
                    break;

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void find_menu_action(Intent data){
        ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        for(String s:results){
            Log.i("res", s);
            if(s.toUpperCase().equals(MenuItem.ADD_CONTACT)){
                //create CallContact object etc
                Log.i("menu"," add contact ");
                break;
            }else if(s.toUpperCase().equals((MenuItem.CALL_CONTACT))){
                Log.i("menu"," call contact ");
                break;
            }else if(s.toUpperCase().equals((MenuItem.PLAY_MUSIC))){
                Log.i("menu"," play music ");
                break;
            }else if(s.toUpperCase().equals(MenuItem.READ_MESSAGE)){
                Log.i("menu"," read message");
                break;
            }else if(s.toUpperCase().equals(MenuItem.COMPOSE_MESSAGE)){
                Log.i("menu"," compose message ");
                break;
            }else{
                continue;
            }
        }
    }


    @Override
    public void onClick(View view) {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Tap and talk baby!");
        startActivityForResult(i, check);
    }
}
