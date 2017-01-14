package operations;

import com.rocketpowerteam.reallysmartphone.MainApp;
import com.rocketpowerteam.reallysmartphone.R;

import java.util.ArrayList;

import commons.Contact;
import services.CallApp;

/**
 * Created by gina4_000 on 14/1/2017.
 */
public class Help extends MenuItem {

    public Help(String strCommand, int max_state) {
        super(strCommand, max_state);
    }

    @Override
    public void action(ArrayList<String> results, String current, MainApp main) {
        CallApp c = new CallApp(main);
        if(c.makeCall(Contact.POLICE)) {
            main.speak(main.getString(R.string.calm));
        }else{
            main.speak(main.getString(R.string.unlucky));
        }
        changeState();
    }
}
