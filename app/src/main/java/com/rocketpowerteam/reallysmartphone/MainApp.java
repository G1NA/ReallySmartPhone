package com.rocketpowerteam.reallysmartphone;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.Locale;

public final class MainApp extends AppCompatActivity implements View.OnClickListener{

    private ImageButton btn;
    private TextToSpeech tts;
    boolean calMode = false;
    private MenuItem mode = null;
    private Contact contact = new Contact();
    private Alarm alarm;
    private PlayMusicApp pm = new PlayMusicApp(this);
    private DateTimeApp dt = new DateTimeApp(this);
    private boolean hasNetworkConnection = false;
    private int clickCount = 0;
    private String hour;
    private String minutes;

    enum MenuItem {
        EXLAIN_MENU("menu", 1), ADD_CONTACT("add contact", 3), CALL_CONTACT("call", 2 ), PLAY_MUSIC("play music", 1),
        READ_MESSAGE("read", 0), COMPOSE_MESSAGE("create compose send", 3), SET_ALARM("alarm", 3),
        STOP_MUSIC("stop", 1), TELL_DATE("date", 1), TELL_TIME("time", 1),HELP("help emergency", 0);
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
        btn = (ImageButton) findViewById(R.id.talktome);
        btn.setOnClickListener(this);
        tts = new TextToSpeech(MainApp.this, new TextToSpeech.OnInitListener(){

            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int res = tts.setLanguage(Locale.ENGLISH);
                    if(res == TextToSpeech.LANG_NOT_SUPPORTED || res == TextToSpeech.LANG_MISSING_DATA){
                        Log.e("TTS", "Language is not supported");
                    }
                    hasNetworkConnection = isNetworkAvailable();
                    String net_string = hasNetworkConnection ? "" : getString(R.string.unavailable_network);
                    tts.speak(getString(R.string.app_prompt) + net_string, TextToSpeech.QUEUE_FLUSH, null);
                }else{
                    Log.e("TTS","Init failed");
                }
            }
        });
    }



    @Override
    public void onClick(View view) {
        clickCount++;
        if(!hasNetworkConnection){
            if(clickCount == 2) {
                hasNetworkConnection = enableNetworkConnection();
                if(hasNetworkConnection)
                    tts.speak(getString(R.string.wifi_enabled),TextToSpeech.QUEUE_FLUSH, null);
                clickCount = 0;
            }
        }else {
            if (pm != null && pm.isPlaying()) {
                pm.volumeDown();
            }
            Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Talk");
            startActivityForResult(i, 1);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
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

    private void find_menu_action(Intent data){
        ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        for(String s:results){
            Log.i("res", s);
            if(checkCommand(s.toLowerCase(), MenuItem.CALL_CONTACT.getDetail()) || mode == MenuItem.CALL_CONTACT){
                if (!(mode == MenuItem.CALL_CONTACT)) {
                    calMode = true;
                    mode = MenuItem.CALL_CONTACT;
                    tts.speak(getString(R.string.ask_contact_to_call), TextToSpeech.QUEUE_FLUSH, null);
                    break;
                }
                else{
                    MainApp m = this;
                    CallApp c = new CallApp(m);
                    if(! c.makeCall(results))
                        tts.speak(getString(R.string.failed_to_find_contact), TextToSpeech.QUEUE_FLUSH, null);
                    calMode = false;
                    mode = null;
                    break;
                }
            }else if(checkCommand(s.toLowerCase(), MenuItem.ADD_CONTACT.getDetail().toLowerCase()) || mode == MenuItem.ADD_CONTACT){
                if(!(mode == MenuItem.ADD_CONTACT)){
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
                            tts.speak(getString(R.string.ask_contact_number), TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case 2:
                            contact.setNumber(s);
                            contact.fixNumber();
                            AddContactApp ac = new AddContactApp(contact, this);
                            ac.addContact();
                            tts.speak(getString(R.string.added_contact), TextToSpeech.QUEUE_FLUSH, null);
                            mode = null;
                            break;
                    }
                }
                break;
            }else if(checkCommand(s.toLowerCase(), MenuItem.STOP_MUSIC.getDetail())) {
                if (pm!=null && pm.isPaused())
                    pm.stopPlayer();
                break;
            }else if(checkCommand(s.toLowerCase(), MenuItem.PLAY_MUSIC.getDetail()) || mode == MenuItem.PLAY_MUSIC){
                pm = new PlayMusicApp(this);
                if(!pm.playMusic())
                    tts.speak(getString(R.string.failed_to_find_songs), TextToSpeech.QUEUE_FLUSH, null);
                else
                    tts.speak(getString(R.string.stop_music), TextToSpeech.QUEUE_FLUSH, null);
                mode = null;
                break;
            }else if(checkCommand(s.toLowerCase(), MenuItem.READ_MESSAGE.getDetail()) || mode == MenuItem.READ_MESSAGE){
                MessageApp mes = new MessageApp(this);
                ArrayList<String> messages = mes.readMessages();
                if(messages.size() == 0){
                    tts.speak(getString(R.string.no_messages), TextToSpeech.QUEUE_FLUSH, null);
                }
                else{
                    tts.speak(getString(R.string.you_have)+" "+messages.size()+" "+getString(R.string.unread_mes),TextToSpeech.QUEUE_FLUSH,null);
                    for(String message: mes.readMessages()){
                        tts.speak(message , TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
                Log.i("menu"," read message");
                break;
            }else if(checkCommand(s.toLowerCase(), MenuItem.COMPOSE_MESSAGE.getDetail()) || mode == MenuItem.COMPOSE_MESSAGE) {
                Log.i("menu", " compose message ");
                if (!(mode == MenuItem.COMPOSE_MESSAGE)) {
                    Log.i("first"," compose message ");
                    mode = MenuItem.COMPOSE_MESSAGE;
                    mode.resetState();
                    tts.speak(getString(R.string.ask_contact_to_send_message), TextToSpeech.QUEUE_FLUSH, null);
                    break;
                }
                else{
                    mode.changeState();
                    switch (mode.getState()){
                        case 1:
                            contact = new Contact();
                            Cursor cur = Contact.getContacts(this);
                            String phoneNo = null;
                            for(String name:results) {
                                phoneNo = Contact.getContactNumber(name,cur);
                                if(phoneNo != null) {
                                    contact.setName(name);
                                    contact.setNumber(phoneNo);
                                    break;
                                }
                            }

                            if(phoneNo != null) {
                                tts.speak(getString(R.string.tell_body_message), TextToSpeech.QUEUE_FLUSH, null);
                            }else {
                                tts.speak(getString(R.string.failed_to_find_contact), TextToSpeech.QUEUE_FLUSH, null);
                                mode = null;
                            }
                            break;
                        case 2:
                            Log.i("message", s);
                            MessageApp ma = new MessageApp(this);
                            //ma.sendMessage(new Message(s,contact));
                            ma.sendLongSMS();
                            tts.speak(getString(R.string.composed_message), TextToSpeech.QUEUE_FLUSH, null);
                            mode = null;
                            break;
                    }
                    break;
                }
            }else if(checkCommand(s.toLowerCase(),MenuItem.TELL_DATE.getDetail())) {
                tts.speak(dt.getReadableDate(),TextToSpeech.QUEUE_FLUSH, null);
                break;
            }else if(checkCommand(s.toLowerCase(), MenuItem.TELL_TIME.getDetail())) {
                tts.speak(dt.getReadableTime(), TextToSpeech.QUEUE_FLUSH, null);
                break;
            } else if(checkCommand(s.toLowerCase(),MenuItem.SET_ALARM.getDetail())|| mode == MenuItem.SET_ALARM){
                if(!(mode == MenuItem.SET_ALARM)){
                    tts.speak(getString(R.string.ask_hour_for_alarm), TextToSpeech.QUEUE_FLUSH, null);
                    mode = MenuItem.SET_ALARM;
                }else {
                    mode.changeState();
                    switch (mode.getState()) {
                        case 1:
                            hour = s;
                            tts.speak(getString(R.string.ask_minutes_for_alarm), TextToSpeech.QUEUE_FLUSH, null);
                        case 2:
                            minutes = s;
                            alarm = new Alarm(hour, minutes, this);
                            alarm.setAlarm();
                            mode = null;
                    }
                }
                break;
            }else if(checkCommand(s.toLowerCase(),MenuItem.EXLAIN_MENU.getDetail())) {
                tts.speak(getString(R.string.menu), TextToSpeech.QUEUE_FLUSH, null);
                break;
            }else if(checkCommand(s.toLowerCase(),MenuItem.HELP.getDetail())){
                CallApp c = new CallApp(this);
                if(c.makeCall(Contact.POLICE)) {
                    tts.speak("Please stay calm!", TextToSpeech.QUEUE_FLUSH, null);
                }else{
                    tts.speak("You are so unlucky today! I cannot call the police! I am really sorry master!",
                            TextToSpeech.QUEUE_FLUSH, null);
                }

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

    private boolean checkCommand(String str1,String str2){
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

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private boolean enableNetworkConnection() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);
        return true;
    }

}