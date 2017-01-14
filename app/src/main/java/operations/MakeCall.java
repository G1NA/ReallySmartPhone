package operations;

import com.rocketpowerteam.reallysmartphone.MainApp;
import com.rocketpowerteam.reallysmartphone.R;

import java.util.ArrayList;

import services.CallApp;

/**
 * Created by gina4_000 on 14/1/2017.
 */

public class MakeCall extends MenuItem {


    public MakeCall(String strCommand, int max_state) {
        super(strCommand, max_state);
    }

    @Override
    public void action(ArrayList<String> results, String current, MainApp main) {
        switch (getState()) {
            case 0:
                main.speak(main.getString(R.string.ask_contact_to_call));
                break;
            case 1:
                CallApp c = new CallApp(main);
                if (!c.makeCall(results))
                    main.speak(main.getString(R.string.failed_to_find_contact));
                break;
        }

        changeState();
    }

}
