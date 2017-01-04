package com.rocketpowerteam.reallysmartphone;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Locale;


public class Activity extends AppCompatActivity implements View.OnClickListener {

    Button btn;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.talktome);
        btn.setOnClickListener(this);
        tts = new TextToSpeech(Activity.this, new TextToSpeech.OnInitListener(){

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
                tts.speak(getString(R.string.app_prompt),TextToSpeech.QUEUE_FLUSH,null);
                tts.setSpeechRate(0.8f);
            }
        });


    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Tap and talk baby!");
        startActivityForResult(i, 1);
    }
}
