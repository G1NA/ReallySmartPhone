package operations;

import android.database.Cursor;

import com.rocketpowerteam.reallysmartphone.MainApp;
import com.rocketpowerteam.reallysmartphone.R;

import java.util.ArrayList;

import commons.Contact;
import commons.Message;
import services.MessageApp;

/**
 * Created by gina4_000 on 14/1/2017.
 */
public class ComposeMessage extends MenuItem {

    private Contact contact;

    public ComposeMessage(String strCommand, int max_state) {
        super(strCommand, max_state);
    }

    @Override
    public void action(ArrayList<String> results, String current, MainApp main) {

        switch (getState()){
            case 0:
                main.speak(main.getString(R.string.ask_contact_to_send_message));
                break;
            case 1:
                contact = new Contact();
                Cursor cur = Contact.getContacts(main);
                String phoneNo = null;
                for(String name:results) {
                    phoneNo = Contact.getContactNumber(name,cur);
                    if(phoneNo != null) {
                        contact.setName(name);
                        contact.setNumber(phoneNo);
                        break;
                    }
                }

                if(phoneNo != null) {
                    main.speak(main.getString(R.string.tell_body_message));
                }else {
                    main.speak(main.getString(R.string.failed_to_find_contact));
                    setFinished();
                }
                break;
            case 2:
                MessageApp ma = new MessageApp(main);
                //TODO do smthg with b
                boolean b = ma.sendMessage(new Message(results.get(0), contact));
                main.speak(main.getString(R.string.composed_message));
                break;
        }

        changeState();
    }
}
