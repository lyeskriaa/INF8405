package com.inf8405.tp2_inf8405.model;

/**
 * Created by Louise on 2017-03-07.
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