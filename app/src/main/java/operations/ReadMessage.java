package operations;

import com.rocketpowerteam.reallysmartphone.MainApp;
import com.rocketpowerteam.reallysmartphone.R;

import java.util.ArrayList;

import services.MessageApp;

/**
 * Created by gina4_000 on 14/1/2017.
 */
public class ReadMessage extends MenuItem {

    public ReadMessage(String strCommand, int max_state) {
        super(strCommand, max_state);
    }

    @Override
    public void action(ArrayList<String> results, String current, MainApp main) {
        MessageApp mes = new MessageApp(main);
        ArrayList<String> messages = mes.readMessages();
        if(messages.size() == 0){
            main.speak(main.getString(R.string.no_messages));
        }
        else{
            main.speak(main.getString(R.string.you_have)+" "+messages.size()+" "+main.getString(R.string.unread_mes));
            for(String message: messages){
                main.speak(message);
            }
        }
    }
}
