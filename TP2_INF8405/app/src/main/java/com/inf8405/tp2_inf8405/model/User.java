package com.inf8405.tp2_inf8405.model;

/**
 * Created by Louise on 2017-02-16.
 */

public class User {
    private String username;
    private String picture;
    private boolean organisateur;
    private Coordinate coordinate;
    private Group group;

    public User() {

    }

    public User(String username, String picture, boolean organisateur, double longitude, double latitude,
                Group group, boolean writePermission) {
        this.username = username;
        this.picture = picture;
        this.organisateur = organisateur;
        this.coordinate = new Coordinate(longitude, latitude);
        this.group = group;
    }

    public String getUsername(){ return  username; }
    public String getPicture(){ return picture; }
    public boolean isOrganisateur(){ return  organisateur; }
    public Coordinate getCoordinate(){ return  coordinate; }
    public Group getGroup(){ return  group; }

    public void setUsername(String username){
        this.username = username;
    }
    public void setPicture(String picture){
            this.picture = picture;
    }
    public void setCoordinate(double longitute, double latitude){
        coordinate = new Coordinate(longitute, latitude);
    }

    public void setAsOrganisteur(){
       organisateur = true;
    }
}
