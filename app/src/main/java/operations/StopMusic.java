package operations;

import com.rocketpowerteam.reallysmartphone.MainApp;

import java.util.ArrayList;

/**
 * Created by gina4_000 on 14/1/2017.
 */
public class StopMusic extends MenuItem {

    public StopMusic(String strCommand, int max_state) {
        super(strCommand, max_state);
    }

    @Override
    public void action(ArrayList<String> results, String current, MainApp main) {
        if (main.getPlayer()!=null && main.getPlayer().isPaused())
            main.getPlayer().stopPlayer();
        changeState();
    }
}
