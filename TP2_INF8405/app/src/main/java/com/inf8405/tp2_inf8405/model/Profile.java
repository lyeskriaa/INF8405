package com.inf8405.tp2_inf8405.model;

/**
 * Created by Louise on 2017-02-16.
 */

public class Profile {
    public static String username = null;
    public static String pictFile = null;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Profile.username = username;
    }

    public static String getPictFile() {
        return pictFile;
    }

    public static void setPictFile(String pictFile) {
        Profile.pictFile = pictFile;
    }

}
