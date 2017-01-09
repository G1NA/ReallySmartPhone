package com.rocketpowerteam.reallysmartphone;

/**
 * Created by gina4_000 on 9/1/2017.
 */

public class Message {
    String body;
    Contact contact;

    public Message(String body, Contact contact){
        this.body = body;
        this.contact = contact;
    }

    public  Message(){}

    public void setBody(String body){ this.body = body; }
    public void setContact(Contact c){ this.contact = c;}
}
