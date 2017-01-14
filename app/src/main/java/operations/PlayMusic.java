package operations;

import com.rocketpowerteam.reallysmartphone.MainApp;
import com.rocketpowerteam.reallysmartphone.R;

import java.util.ArrayList;

/**
 * Created by gina4_000 on 14/1/2017.
 */
public class PlayMusic extends MenuItem {

    public PlayMusic(String strCommand, int max_state) {
        super(strCommand, max_state);
    }

    @Override
    public void action(ArrayList<String> results, String current, MainApp main) {
        if(!main.getPlayer().playMusic())
            main.speak(main.getString(R.string.failed_to_find_songs));
        else
            main.speak(main.getString(R.string.stop_music));
        changeState();
    }
}
