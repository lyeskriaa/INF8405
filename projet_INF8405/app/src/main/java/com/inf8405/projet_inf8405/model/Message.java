package com.inf8405.projet_inf8405.model;

/**
 * Created by LyesKriaa on 17-04-22.
 */

public class Message {
    String id;
    String user;
    String message;

    public Message(String id, String user, String message) {
        this.id = id;
        this.user = user;
        this.message = message;
    }

    public Message(String user, String message) {
        this.user = user;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
