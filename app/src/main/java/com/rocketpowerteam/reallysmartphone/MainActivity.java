package com.rocketpowerteam.reallysmartphone;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView lv;
    Button btn;
    final int check = 111;
    final int get_contact_name =112;
    ArrayList<String> menu_items;
    enum MenuItem { CONTACT("Add Contact"), TORCH("Switch on torch"), CALL("Call someone"), MUSIC("Play music");
        public String detail;

        MenuItem(String detail){
            this.detail = detail;
        }

        public String getDetail(){return detail.toLowerCase();}

    }

    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView)findViewById(R.id.menu);
        btn = (Button) findViewById(R.id.talktome);
        btn.setOnClickListener(this);
        final Button helpBtn = (Button)findViewById(R.id.help_btn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak(getString(R.string.app_prompt),TextToSpeech.QUEUE_FLUSH,null);
                LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.9f);
                lv.setLayoutParams(p1);
                LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.1f);
                btn.setLayoutParams(p2);
                ViewGroup layout = (ViewGroup) helpBtn.getParent();
                if(null!=layout) //for safety only  as you are doing onClick
                    layout.removeView(helpBtn);
            }
        });
        menu_items = new ArrayList<>();
        for(MenuItem e : MenuItem.values()){
            menu_items.add(e.detail);
        }
        lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, menu_items));
        lv.setEnabled(false);
        lv.setClickable(false);
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
            if(s.toLowerCase().equals(MenuItem.CALL.getDetail())){
                make_call_conversation();
                Log.i("menu"," call ");
                break;
            }else if(s.toLowerCase().equals((MenuItem.CONTACT.getDetail()))){
                add_contact();
                Log.i("menu"," contact ");
                break;
            }else if(s.toLowerCase().equals((MenuItem.TORCH.getDetail()))){
                switch_on_torch();
                Log.i("menu"," torch ");
                break;
            }else if(s.toLowerCase().equals(MenuItem.MUSIC.getDetail())){
                play_music();
                Log.i("menu"," music ");
                break;
            }else{
                continue;
            }
        }
    }

    private void switch_on_torch() {
        //TODO pi8anotata 8a figei auto...an dn figei prepei na pros8esoume svisimo fakou
    }

    private void play_music() {

    }

    private void add_contact() {
        tts.speak(getString(R.string.ask_contact_name),TextToSpeech.QUEUE_FLUSH,null);

    }


    private void make_call_conversation() {
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
