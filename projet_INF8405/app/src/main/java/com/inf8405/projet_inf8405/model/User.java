package com.inf8405.projet_inf8405.model;

/**
 * Created by LyesKriaa on 17-04-09.
 */

public class User {
    private String id;
    private String username;
    private String description;
    private String picture;
    private Coordinate coordinate;
    private String sexe;

    public User() {

    }

    public User(String id, String username, String description, String picture, double longitude, double latitude, String sexe) {
        this.id = id;
        this.username = username;
        this.description = description;
        this.picture = picture;
        this.coordinate = new Coordinate(longitude, latitude);
        this.sexe = sexe;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return  username;
    }

    public String getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }

    public Coordinate getCoordinate() { return coordinate; }

    public String getSexe() {
        return sexe;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUsername(String username){
            this.username = username;
        }

    public void setPicture(String picture){
            this.picture = picture;
        }
    public void setCoordinate(double longitute, double latitude) {
            coordinate = new Coordinate(longitute, latitude);
        }
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
