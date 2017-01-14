package operations;

import com.rocketpowerteam.reallysmartphone.MainApp;

import java.util.ArrayList;

/**
 * Created by gina4_000 on 14/1/2017.
 */

public abstract class MenuItem {

    private String strCommand;
    private int inner_state;
    int max_state;

    MenuItem(String strCommand, int max_state)
    {
        this.strCommand = strCommand;
        this.max_state = max_state;
    }

    public String getDetail() {
        return strCommand;
    }

    public void changeState(){
        inner_state += 1 % max_state;
    }

    public int getState(){ return this.inner_state; }

    public void resetState() { this.inner_state = 0; }

    public void repeat() {
        inner_state--;
    }

    public boolean isFinished(){ return  inner_state == max_state - 1;}
    protected void setFinished(){ inner_state = max_state - 1;}

    public boolean checkCommand(String str){
        String[] s1 = strCommand.split(" ");
        String[] s2 = str.split(" ");

        for(int i = 0; i < s1.length; i++){
            for(int j = 0; j < s2.length; j++){
                if(s1[i].equals(s2[j]))
                    return true;
            }
        }
        return false;
    }

    public abstract void action(ArrayList<String> results, String current, MainApp main);
}
