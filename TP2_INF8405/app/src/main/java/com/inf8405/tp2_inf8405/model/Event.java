package com.inf8405.tp2_inf8405.model;

/**
 * Created by Louise on 2017-03-15.
 */

public class Event {
    private Coordinate lieuChoisi;
    private String dateStart;
    private String dateEnd;
    private String eventName;
    private String picture;

    public Event(Coordinate lieuChoisi, String eventName, String picture, String dateStart, String dateEnd){
        this.lieuChoisi = lieuChoisi;
        this.picture = picture;
        this.eventName = eventName;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public void setLieuChoisi(Coordinate coor) {lieuChoisi = coor;}
    public Coordinate getLieuChoisit(){ return lieuChoisi; }

    public void setEventName(String eventName) { this.eventName = eventName; }
    public String getEventName() { return eventName; }

    public void setPicture(String picture) { this.picture = picture; }
    public String getPicture() { return picture; }

    public void setDateStart(String date) {this.dateStart = date;}
    public void setDateEnd(String date) {this.dateEnd = date;}
    public String getDateStart() { return this.dateStart;}
    public String getDateEnd() { return this.dateEnd;}
}
