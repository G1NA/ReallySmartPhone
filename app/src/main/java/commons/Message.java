package commons;

public class Message {
    private String body;
    private Contact contact;

    public Message(String body, Contact contact){
        this.body = body;
        this.contact = contact;
    }

    public Contact getContact() {return contact;}
    public String getBody() {return body;}

    public void setBody(String body){ this.body = body; }
    public void setContact(Contact c){ this.contact = c;}
}
