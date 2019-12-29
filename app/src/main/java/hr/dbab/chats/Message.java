package hr.dbab.chats;

public class Message {

    private String sender, receiver, messageText;

    public Message() {
    }

    public Message(String sender, String receiver, String messageText) {
        this.sender = sender;
        this.receiver = receiver;
        this.messageText = messageText;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessageText() {
        return messageText;
    }
}
