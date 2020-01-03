package hr.dbab.chats.model;

public class Contact {

    private String id, username, imageURL;

    public Contact() {
    }

    public Contact(String id, String username, String imageURL) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getImageURL() {
        return imageURL;
    }
}
