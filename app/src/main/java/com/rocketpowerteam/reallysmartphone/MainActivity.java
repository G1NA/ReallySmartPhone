package com.rocketpowerteam.reallysmartphone;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener{

    enum MenuItem {
        ADD_CONTACT("add"), CALL_CONTACT("call"), PLAY_MUSIC("play music"),
        READ_MESSAGE("read"), COMPOSE_MESSAGE("create compose");
        String strCommand;
        MenuItem(String strCommand){
            this.strCommand = strCommand;
        }
        public String getDetail() {
            return strCommand;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
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
            if(checkCommand(s.toLowerCase(), MenuItem.CALL_CONTACT.getDetail())){
                Log.i("menu"," call contact ");
                CallContact c = new CallContact();
                break;
            }else if(checkCommand(s.toLowerCase(), MenuItem.ADD_CONTACT.getDetail().toLowerCase())){
                Log.i("menu"," add contact ");
                break;
            }else if(checkCommand(s.toLowerCase(), MenuItem.PLAY_MUSIC.getDetail())){
                Log.i("menu"," play music ");
                break;
            }else if(checkCommand(s.toLowerCase(), MenuItem.READ_MESSAGE.getDetail())){
                Log.i("menu"," read message");
                break;
            }else if(checkCommand(s.toLowerCase(), MenuItem.COMPOSE_MESSAGE.getDetail())){
                Log.i("menu"," compose message ");
                break;
            }else{
                Log.i("else", s);
                continue;
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
