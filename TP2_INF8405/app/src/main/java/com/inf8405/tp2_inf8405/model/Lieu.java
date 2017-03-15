package com.inf8405.tp2_inf8405.model;

/**
 * Created by Louise on 2017-03-15.
 */

public class Lieu {
    private Coordinate coordinate;
    private int votes;

    public Lieu(double longitude, double latitude){ this(new Coordinate(longitude, latitude), 0); }
    public Lieu(Coordinate coordinate){ this(coordinate, 0); }
    public Lieu(double longitude, double latitude, int votes){ this(new Coordinate(longitude, latitude), votes); }
    public Lieu(Coordinate coordinate, int votes){
        this.coordinate = coordinate;
        this.votes = votes;
    }

    public void setVotes(int votes){ this.votes = votes; }
    public int getVotes() { return votes; }
    public int incrementVote() {return ++votes; }
}
