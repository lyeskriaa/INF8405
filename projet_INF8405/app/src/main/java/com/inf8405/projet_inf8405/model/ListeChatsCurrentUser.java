package com.inf8405.projet_inf8405.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LyesKriaa on 17-04-22.
 */

public class ListeChatsCurrentUser {
    private static ListeChatsCurrentUser INSTANCE = null;
    private List<Chat> chatList = new ArrayList<Chat>();

    public static ListeChatsCurrentUser getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ListeChatsCurrentUser();
        }
        return INSTANCE;
    }
    private ListeChatsCurrentUser() {

    }

    public List<Chat> getChatList() {
        return chatList;
    }

    public void setChatList(List<Chat> chats) {
        chatList = chats;
    }

    public Chat findChat(String chatId){
        for (Chat chat : chatList) {
            if (chat.getId().equals(chatId)) return chat;
        }
        return null;
    }

    public void addChat(Chat newChat) {
        if (chatList == null)chatList = new ArrayList<Chat>();
        if (newChat != null && findChat(newChat.getId()) == null) {
            chatList.add(newChat);
        }
    }

}
