package com.inf8405.projet_inf8405.model;

import java.util.List;

/**
 * Created by LyesKriaa on 17-04-13.
 */

public class Chat {
    private String id;
    private String idFirstUser;
    private String idSecondUser;
    private List<Message> messagesHistory;

    public Chat() {
    }

    public Chat(String idFirstUser, String idSecondUser, List<Message> messagesHistory) {
        this.idFirstUser = idFirstUser;
        this.idSecondUser = idSecondUser;
        this.messagesHistory = messagesHistory;
    }

    public Chat(String id, String idFirstUser, String idSecondUser, List<Message> messagesHistory) {
        this.id = id;
        this.idFirstUser = idFirstUser;
        this.idSecondUser = idSecondUser;
        this.messagesHistory = messagesHistory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdFirstUser() {
        return idFirstUser;
    }

    public void setIdFirstUser(String idFirstUser) {
        this.idFirstUser = idFirstUser;
    }

    public String getIdSecondUser() {
        return idSecondUser;
    }

    public void setIdSecondUser(String idSecondUser) {
        this.idSecondUser = idSecondUser;
    }

    public List<Message> getMessagesHistory() {
        return messagesHistory;
    }

    public void setMessagesHistory(List<Message> messagesHistory) {
        this.messagesHistory = messagesHistory;
    }
}
