package operations;

import com.rocketpowerteam.reallysmartphone.MainApp;

import java.util.ArrayList;

/**
 * Created by gina4_000 on 14/1/2017.
 */
public class TellTime extends MenuItem {

    public TellTime(String strCommand, int max_state) {
        super(strCommand, max_state);
    }

    @Override
    public void action(ArrayList<String> results, String current, MainApp main) {
        main.speak(main.getDT().getReadableTime());
    }
}
