package com.inf8405.tp2_inf8405.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.util.List;

/**
 * Created by Louise on 2017-03-07.
 */

public class GroupDao {

    private Context context;
    private String group;

    private List<User> users;



    public GroupDao(Context c, String groupName) {
        context = c;
        group = groupName;

        //firebase
        //put in users
    }


    public class User {
        public String username;
        public String PictureURI;
        public boolean organisateur;
        public Coordinate coordinate;

        User(String username, String PictureURI, boolean organisateur, double longitude, double latitude){
            this.username = username;
            this.PictureURI = PictureURI;
            this.organisateur= organisateur;
            this.coordinate = new Coordinate(longitude, latitude);
        }
    }
    public class Coordinate {
        public double longitude;
        public double latitude;
        Coordinate(double longitude, double latitude){
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }
}
