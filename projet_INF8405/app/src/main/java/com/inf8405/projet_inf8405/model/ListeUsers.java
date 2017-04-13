package com.inf8405.projet_inf8405.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LyesKriaa on 17-04-13.
 */

public class ListeUsers {
    private static ListeUsers INSTANCE = null;
    private List<User> userList = new ArrayList<User>();

    public static ListeUsers getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ListeUsers();
        }
        return INSTANCE;
    }
    private ListeUsers() {

    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> users) {
        userList = users;
    }

    public User findUser(String username){
        for (User user : userList) {
            if (user.getUsername().equals(username)) return user;
        }
        return null;
    }

    public void addUser(User newUser) {
        if (userList == null)userList = new ArrayList<User>();
        if (newUser != null && findUser(newUser.getUsername()) == null) {
            userList.add(newUser);
        }
    }
}
