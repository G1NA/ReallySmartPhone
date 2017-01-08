package com.rocketpowerteam.reallysmartphone;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Locale;

public final class MainApp extends AppCompatActivity implements View.OnClickListener{

    Button btn;
    TextToSpeech tts;
    boolean calMode = false;
    MenuItem mode;
    Contact contact;
    private PlayMusicApp pm;
    private DateTimeApp dt = new DateTimeApp(this);

    enum MenuItem {
        ADD_CONTACT("add contact", 3), CALL_CONTACT("call", 2 ), PLAY_MUSIC("play music", 1),
        READ_MESSAGE("read", 0), COMPOSE_MESSAGE("create compose", 0), SET_ALARM("alarm", 0),
        STOP_MUSIC("stop", 0), TELL_DATE("date", 0), TELL_TIME("time", 0);
        String strCommand;
        int inner_state = 0; // used to choose between inner states of a menu item
        // for example call has two states 1) ask for name 2) make call
        int max_state;
        MenuItem(String strCommand, int max_state)
        {
            this.strCommand = strCommand;
            this.max_state = max_state;
        }
        public String getDetail() {
            return strCommand;
        }
        public void changeState(){
            inner_state += 1 % max_state;
        }
        public int getState(){ return this.inner_state; }

        public void resetState() { this.inner_state = 0; }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.talktome);
        btn.setOnClickListener(this);
        tts = new TextToSpeech(MainApp.this, new TextToSpeech.OnInitListener(){

            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int res = tts.setLanguage(Locale.ENGLISH);
                    if(res == TextToSpeech.LANG_NOT_SUPPORTED || res == TextToSpeech.LANG_MISSING_DATA){
                        Log.e("TTS", "Language is not supported");
                    }
                    tts.speak(getString(R.string.app_prompt),TextToSpeech.QUEUE_FLUSH,null);
                    //tts.setSpeechRate(0.8f);
                }else{
                    Log.e("TTS","Init failed");
                }

            }
        });


    }

    @Override
    public void onClick(View view) {
        if(pm != null && pm.isPlaying()){
            pm.volumeDown();
        }
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Tap and talk baby!");
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //TODO isws na 3ananevainei edw i fwni....
        if(pm != null && pm.isPlaying()){
            pm.pause();
        }
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && (resultCode == RESULT_OK)) {
            find_menu_action(data);
        }
        else if(resultCode == RESULT_CANCELED){
            tts.speak(getString(R.string.wrong_input), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    protected  void init(){
        //TODO
        //INITIALIZE ALL FIELDS THAT ARE NEEDED FOR THE APP (like dt)
    }
    private void find_menu_action(Intent data){
        ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        for(String s:results){
            Log.i("res", s);
            if(checkCommand(s.toLowerCase(), MenuItem.CALL_CONTACT.getDetail()) || mode == MenuItem.CALL_CONTACT){
                if (!(mode == MenuItem.CALL_CONTACT)) {
                    Log.i("first"," call contact ");
                    calMode = true;
                    mode = MenuItem.CALL_CONTACT;
                    tts.speak(getString(R.string.ask_contact_to_call), TextToSpeech.QUEUE_FLUSH, null);
                    break;
                }
                else{
                    MainApp m = this;
                    CallApp c = new CallApp(s, m);
                    if(! c.makeCall(results))
                        tts.speak("I am so sorry. I could not find your contact master!", TextToSpeech.QUEUE_FLUSH, null);
                        //tts.speak(""); TODO EROOR MESSAGE HERE
                    //exoume dio epiloges...na 3anarwta to onoma i na 3anagirnaei sto arxiko menou


                    Log.i("second "," call contact ");
                    calMode = false;
                    mode = null;
                    break;
                }
            }else if(checkCommand(s.toLowerCase(), MenuItem.ADD_CONTACT.getDetail().toLowerCase()) || mode == MenuItem.ADD_CONTACT){
                if(!(mode == MenuItem.ADD_CONTACT)){
                    Log.i("first", "add contact");
                    mode = MenuItem.ADD_CONTACT;
                    mode.resetState();
                    tts.speak(getString(R.string.ask_contact_name), TextToSpeech.QUEUE_FLUSH, null);
                    break;
                }else{
                    mode.changeState();
                    switch (mode.getState()){
                        case 1:
                            contact = new Contact();
                            contact.setName(s);
                            Log.i("name", s);
                            tts.speak(getString(R.string.ask_contact_number), TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case 2:
                            contact.setNumber(s);
                            contact.fixNumber();
                            Log.i("tel", s);
                            AddContactApp ac = new AddContactApp(contact, this);
                            ac.addContact();
                            tts.speak("Your contact is added!", TextToSpeech.QUEUE_FLUSH, null);
                            mode = null;
                            break;
                    }

                }
                Log.i("menu"," add contact ");
                break;
            }else if(checkCommand(s.toLowerCase(), MenuItem.PLAY_MUSIC.getDetail()) || mode == MenuItem.PLAY_MUSIC){

                pm = new PlayMusicApp(this);
                if(!pm.playMusic())
                    tts.speak("I am so sorry master! I could not find songs in your phone!", TextToSpeech.QUEUE_FLUSH, null);
                else
                    tts.speak("To stop the music please say STOP", TextToSpeech.QUEUE_FLUSH, null);
                mode = null;

                Log.i("menu"," play music ");
                break;
            }else if(checkCommand(s.toLowerCase(), MenuItem.READ_MESSAGE.getDetail())){
                Log.i("menu"," read message");
                break;
            }else if(checkCommand(s.toLowerCase(), MenuItem.COMPOSE_MESSAGE.getDetail())) {
                Log.i("menu", " compose message ");
                break;
            }else if(checkCommand(s.toLowerCase(), MenuItem.STOP_MUSIC.getDetail())) {
                //-->dn xreiazetai na alla3eis mode edw.....
                Log.i("menu", "stop music");
                if (pm!=null && pm.isPaused()) {
                    pm.stopPlayer();
                }
                break;
            }else if(checkCommand(s.toLowerCase(),MenuItem.TELL_DATE.getDetail())) {
                Log.i("menu", "tell date");
                tts.speak(dt.getReadableDate(),TextToSpeech.QUEUE_FLUSH, null);
            }else if(checkCommand(s.toLowerCase(), MenuItem.TELL_TIME.getDetail())){
                Log.i("menu", "tell time");
                tts.speak(dt.getReadableTime(), TextToSpeech.QUEUE_FLUSH, null);
            }else{
                Log.i("else", s);
                continue;
            }
        }

        if(mode == null){
            if(pm!=null && pm.isPaused()){
                pm.resume();
                pm.volumeUp();
            }
        }
    }

    public boolean checkCommand(String str1,String str2){
        String[] s1 = str1.split(" ");
        String[] s2 = str2.split(" ");

        for(int i = 0; i < s1.length; i++){
            for(int j = 0; j < s2.length; j++){
                if(s1[i].equals(s2[j]))
                    return true;
            }
        }
        return false;
    }

}
