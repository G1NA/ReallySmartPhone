package operations;

import com.rocketpowerteam.reallysmartphone.MainApp;
import com.rocketpowerteam.reallysmartphone.R;

import java.util.ArrayList;

import commons.Contact;
import helpers.NumFixer;
import services.AddContactApp;

/**
 * Created by gina4_000 on 14/1/2017.
 */

public class AddContact extends MenuItem {

    private Contact contact;

    public AddContact(String strCommand, int max_state) {
        super(strCommand, max_state);
    }

    @Override
    public void action(ArrayList<String> results, String current, MainApp main) {
        switch (getState()){
            case 0:
                main.speak(main.getString(R.string.ask_contact_name));
                break;
            case 1:
                contact = new Contact();
                contact.setName(current);
                main.speak(main.getString(R.string.ask_contact_number));
                break;
            case 2:
                contact.setNumber(NumFixer.fixNumber(current));
                AddContactApp ac = new AddContactApp(contact, main);
                ac.addContact();
                main.speak(main.getString(R.string.added_contact));
                break;
        }
        changeState();
    }
}
