package com.inf8405.tp2_inf8405.model;

/**
 * Created by Louise on 2017-03-15.
 */

public class Lieu {
    private Coordinate coordinate;
    private String picture;
    private String name;
    private int votes;
    private int myVote;

    public Lieu(Coordinate coordinate, String name, String picture, int votes){
        this.name = name;
        this.picture = picture;
        this.coordinate = coordinate;
        this.votes = votes;
    }

    public void setVotes(int votes){ this.votes = votes; }
    public int getVotes() { return votes; }
    public void vote(int vote) { myVote = vote; }

    public String getName() {return name;}
    public String getPicture() {return picture;}
    public Coordinate getCoordinate() {return coordinate;}

    public boolean asName(String name){
        if (this.name.equals(name)) return true;
        else return false;
    }

    public boolean asCoordinate(Coordinate coordinate){
        if (this.coordinate.getLatitude() == coordinate.getLatitude() &&
            this.coordinate.getLongitude() == coordinate.getLongitude()) return true;
        else return false;
    }
}
