package com.inf8405.projet_inf8405.model;

/**
 * Created by LyesKriaa on 17-04-09.
 */

public class Coordinate {
    final public double longitude;
    final public double latitude;
    public Coordinate(double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
