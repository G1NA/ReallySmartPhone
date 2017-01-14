package com.rocketpowerteam.reallysmartphone;

import android.content.Context;
import android.content.Intent;
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

import operations.*;
import services.*;


public final class MainApp extends AppCompatActivity implements View.OnClickListener{

    //try comment

    private ImageButton btn;
    private TextToSpeech tts;
    private static final int NO_MODE = -1; //invalid num for no mode
    private int mode = NO_MODE;
    private PlayMusicApp pm = new PlayMusicApp(this);
    private DateTimeApp dt = new DateTimeApp(this);
    private boolean hasNetworkConnection = false;
    private int clickCount = 0;
    private ArrayList<MenuItem> menuItems = new ArrayList<>();
    private boolean confirmation_state = false;
    private String data_to_use = "";
    private ArrayList<String> results_to_use;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (ImageButton) findViewById(R.id.talktome);
        btn.setOnClickListener(this);
        initMenuItems();
        initTextToSpeech();
    }

    private void initTextToSpeech() {
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


    private void initMenuItems(){
        menuItems.add(new ExplainMenu("menu", 1));
        menuItems.add(new MakeCall("call",2));
        menuItems.add(new AddContact("contact", 3));
        menuItems.add(new TellDate("date", 1));
        menuItems.add(new TellTime("time", 1));
        menuItems.add(new SetAlarm("alarm", 3));
        menuItems.add(new StopMusic("stop",1));
        menuItems.add(new PlayMusic("play music",1));
        menuItems.add(new ReadMessage("read unread", 1));
        menuItems.add(new ComposeMessage("create compose send", 3));
        menuItems.add(new Help("help emergency", 1));
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
        boolean menu_item_found = false;

        for(String s:results){
            Log.i("res", s);
            int item_num = 0;
            for(MenuItem item : menuItems){
                if(item.checkCommand(s.toLowerCase()) || mode == item_num){
                    menu_item_found = true;

                    if(mode == NO_MODE){ //init mode
                        item.resetState();
                        mode = item_num;
                    }else { //confirm user data
                        if (confirmation_state) {
                            if(s.toLowerCase().equals("no")){
                                item.repeat();
                            }
                            confirmation_state = false;
                        } else {
                            data_to_use = s;
                            results_to_use = results;
                            confirmData(s);
                            break;
                        }
                    }

                    item.action(results_to_use, data_to_use, this);
                    //if after action TextToSpeech queue is not empty it is finally flushed here
                    speak_flush();
                    //if the whole operation finished then we return to the <arxiko> mode
                    if(item.isFinished()){
                        mode = NO_MODE;
                    }
                    break;

                }
                item_num++;
            }

            if(menu_item_found)
                break;
        }

        if(!menu_item_found){
            tts.speak(getString(R.string.wrong_input), TextToSpeech.QUEUE_FLUSH, null);
        }

        if(mode == NO_MODE){
            if(pm!=null && pm.isPaused()){
                pm.resume();
                pm.volumeUp();
            }
        }
    }

    private void confirmData(String s) {
        tts.speak(getString(R.string.you_said)+" "+s+" "+getString(R.string.is_that_correct),
                TextToSpeech.QUEUE_FLUSH, null);
        confirmation_state = true;
    }

    public DateTimeApp getDT(){ return dt; }

    public PlayMusicApp getPlayer(){ return pm; }

    //TODO compose sentence explaining menu from the items in menu
    public String getMenu() {
        return getString(R.string.menu);
    }

    public void speak(String sentence){ tts.speak(sentence,TextToSpeech.QUEUE_ADD, null); }
    public void speak_flush(){ tts.speak(null, TextToSpeech.QUEUE_FLUSH, null); }


    //TODO maybe create another class for network connection managment
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