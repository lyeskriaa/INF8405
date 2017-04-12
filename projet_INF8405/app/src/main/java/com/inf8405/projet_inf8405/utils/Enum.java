package com.inf8405.projet_inf8405.utils;

/**
 * Created by LyesKriaa on 17-04-09.
 */

public enum Enum {
    USERS("users"),
    COORDINATE("coordinate"),
    CHATS("chats");

    private final String text;

    private Enum(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
