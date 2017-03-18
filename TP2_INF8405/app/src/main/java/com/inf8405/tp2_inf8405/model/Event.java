package com.inf8405.tp2_inf8405.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louise on 2017-03-15.
 */

public class Event {
    private Coordinate lieuChoisi;
    private String date;
    private String eventName;
    private String picture;

    public Event(Coordinate lieuChoisi, String eventName, String picture, String date){
        this.lieuChoisi = lieuChoisi;
        this.picture = picture;
        this.eventName = eventName;
        this.date = date;
    }

    public void setLieuChoisi(Coordinate coor) {lieuChoisi = coor;}
    public Coordinate getLieuChoisit(){ return lieuChoisi; }

    public void setEventName(String eventName) { this.eventName = eventName; }
    public String getEventName() { return eventName; }

    public void setPicture(String picture) { this.picture = picture; }
    public String getPicture() { return picture; }

    public void setDate(String date) {this.date = date;}
    public String getDate() { return this.date;}
}
