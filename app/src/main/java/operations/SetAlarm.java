package operations;

import com.rocketpowerteam.reallysmartphone.MainApp;
import com.rocketpowerteam.reallysmartphone.R;

import java.util.ArrayList;

import helpers.NumFixer;
import services.AlarmApp;

/**
 * Created by gina4_000 on 14/1/2017.
 */
public class SetAlarm extends MenuItem {

    private String hour;
    private String minutes;
    private AlarmApp alarmApp;

    public SetAlarm(String strCommand, int max_state) {
        super(strCommand, max_state);
    }

    @Override
    public void action(ArrayList<String> results, String current, MainApp main) {
            changeState();
            switch (getState()) {
                case 0:
                    main.speak(main.getString(R.string.ask_hour_for_alarm));
                case 1:
                    hour = "";
                    for(String token : results){
                        hour = NumFixer.fixNumber(token);
                        if(!hour.equals(""))
                            break;
                    }

                    if(hour.equals("")){
                        main.speak(main.getString(R.string.wrong_hour));
                        repeat();
                    }else {
                        main.speak(main.getString(R.string.ask_minutes_for_alarm));
                    }
                    break;
                case 2:
                    minutes = "";
                    for(String token : results){
                        minutes = NumFixer.fixNumber(token);
                        if(!minutes.equals(""))
                            break;
                    }

                    if(minutes.equals("")){
                        main.speak(main.getString(R.string.wrocng_minute));
                        repeat();
                    }else {
                        alarmApp = new AlarmApp(hour, minutes, main);
                        alarmApp.setAlarm();
                    }
                    break;
            }
    }
}
