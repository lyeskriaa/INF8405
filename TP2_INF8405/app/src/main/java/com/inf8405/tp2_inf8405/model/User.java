package com.inf8405.tp2_inf8405.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.IOException;

/**
 * Created by Louise on 2017-02-16.
 */

public class User {
    private String username;
    private String picture;
    private boolean organisateur;
    private Coordinate coordinate;
    private Group group;
    private boolean writePermission;

    public User() {

    }

    public User(String username, String picture, boolean organisateur, double longitude, double latitude,
                Group group, boolean writePermission) {
        this.username = username;
        this.picture = picture;
        this.organisateur = organisateur;
        this.coordinate = new Coordinate(longitude, latitude);
        this.group = group;
        this.writePermission = writePermission;
    }

    public String getUsername(){ return  username; }
    public String getPicture(){ return picture; }
    public boolean isOrganisateur(){ return  organisateur; }
    public Coordinate getCoordinate(){ return  coordinate; }
    public Group getGroup(){ return  group; }
    public boolean getWritePermission(){ return  writePermission; }

    public void setUsername(String username){
        if(writePermission) this.username = username;
    }
    public void setPicture(String picture){
        if(writePermission) {
            this.picture = picture;
        }
    }
    public void setCoordinate(double longitute, double latitude){
        if(writePermission) coordinate = new Coordinate(longitute, latitude);
    }

    public void setAsOrganisteur(){
        if(writePermission) organisateur = true;
    }

    public Bitmap pictureAsBitmap(){
        try {
            byte[] decodedByteArray = android.util.Base64.decode(picture, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
