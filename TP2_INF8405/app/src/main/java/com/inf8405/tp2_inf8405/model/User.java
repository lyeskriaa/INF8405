package com.inf8405.tp2_inf8405.model;

/**
 * Created by Louise on 2017-02-16.
 */

public class User {
    private String username;
    private String pictureURI;
    private boolean organisateur;
    private Coordinate coordinate;
    private Group group;
    private boolean writePermission;

    public User() {

    }

    public User(String username, String pictureURI, boolean organisateur, double longitude, double latitude,
         Group group, boolean writePermission) {
        this.username = username;
        this.pictureURI = pictureURI;
        this.organisateur = organisateur;
        this.coordinate = new Coordinate(longitude, latitude);
        this.group = group;
        this.writePermission = writePermission;
    }

    public String getUsername(){ return  username; }
    public String getPictureURI(){ return  pictureURI; }
    public boolean getOrganisateur(){ return  organisateur; }
    public Coordinate getCoordinate(){ return  coordinate; }
    public Group getGroup(){ return  group; }
    public boolean getWritePermission(){ return  writePermission; }

    public void setUsername(String username){
        if(writePermission) this.username = username;
    }
    public void setPictureURI(String pictureURI){
        if(writePermission) this.pictureURI = pictureURI;
    }
    public void setCoordinate(double longitute, double latitude){
        if(writePermission) coordinate = new Coordinate(longitute, latitude);
    }

}
