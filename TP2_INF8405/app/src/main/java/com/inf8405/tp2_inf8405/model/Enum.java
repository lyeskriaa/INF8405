package com.inf8405.tp2_inf8405.model;

/**
 * Created by LyesKriaa on 17-03-18.
 */

public enum Enum {
    USERS("users"),
    COORDINATE("coordinate"),
    GROUPS("groups"),
    LIEUX("lieux"),
    GROUPS_NAMES("groupsNames");

    private final String text;

    private Enum(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
