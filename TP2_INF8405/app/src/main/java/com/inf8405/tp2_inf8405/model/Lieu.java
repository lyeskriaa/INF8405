package com.inf8405.tp2_inf8405.model;

/**
 * Created by Louise on 2017-03-15.
 */

public class Lieu {
    private Coordinate coordinate;
    private String picture;
    private String name;
    private float votes;
    private int nbrVotes;
    private int myVote;

    public Lieu(Coordinate coordinate, String name, String picture, float votes){
        this.name = name;
        this.picture = picture;
        this.coordinate = coordinate;
        this.votes = votes;
        this.nbrVotes = 0;
    }

    public void setVotes(float votes){ this.votes = votes; }
    public float getVotes() { return votes; }

    public int getNbrVotes() {
        return nbrVotes;
    }
    public void setNbrVotes(int nbrVotes) {
        this.nbrVotes = nbrVotes;
    }

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
