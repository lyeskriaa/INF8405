package com.inf8405.projet_inf8405.model;

/**
 * Created by LyesKriaa on 17-04-22.
 */

public class Message {
    String id;
    String userName;
    String message;

    public Message(String id, String userName, String message) {
        this.id = id;
        this.userName = userName;
        this.message = message;
    }

    public Message(String userName, String message) {
        this.userName = userName;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
