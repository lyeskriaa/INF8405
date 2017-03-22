package com.inf8405.tp2_inf8405.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louise on 2017-03-07.
 */

public class Group {
    static Group group;
    private String nomGroupe;
    private List<User> listeUtilisateurs = new ArrayList<User>();
    private User organisateur;
    private List<Lieu> locList;
    private Event event;


    private Group(String nomGroupe, List<User> utilisateurs, List<Lieu> locationList, Event event){
        this.nomGroupe = nomGroupe;
        listeUtilisateurs = new ArrayList<User>();
        addUsers(utilisateurs);
        locList = new ArrayList<Lieu>();
        addLocs(locationList);
        this.event = event;
    }
    static public Group getGroup() {return group;}
    static public Group createGroup(String nomGroupe, List<User> utilisateurs, List<Lieu> locationList, Event event){
        group = new Group(nomGroupe, utilisateurs, locationList, event);
        return group;
    }
    static public Group createGroup(String nomGroupe, List<User> utilisateurs){
        group = new Group(nomGroupe, utilisateurs);
        return group;
    }
    static public Group createGroup(String nomGroupe){
        group = new Group(nomGroupe);
        return group;
    }

    private Group(String nomGroupe, List<User> utilisateurs){
        this(nomGroupe, utilisateurs, new ArrayList<Lieu>(), null);
    }

    private Group(String nomGroupe){
        this.nomGroupe = nomGroupe;
        listeUtilisateurs = new ArrayList<User>();
        locList = new ArrayList<Lieu>();
        event = null;
    }

    public User getOrganisateur(){ return organisateur; }
    public String getNomGroupe() { return nomGroupe; }

    public List<User> getListeUtilisateurs() {
        return listeUtilisateurs;

    }

    public void update(){
        //TODO add location end events
    }

    public void addUser(User newUser) {
        if (listeUtilisateurs == null)listeUtilisateurs = new ArrayList<User>();
        if (newUser != null && findUser(newUser.getUsername()) == null) {
            listeUtilisateurs.add(newUser);
            if (newUser.isOrganisateur()) organisateur = newUser;
        }
    }

    public void removeUser(User user) {
        listeUtilisateurs.remove(user);
    }

    public void addUsers(List<User> utilisateurs) {
        if (utilisateurs != null){
            for (User user : utilisateurs){
                addUser(user);
            }
        }
    }

    public void addLoc(Lieu loc) {
        if (locList == null) locList = new ArrayList<Lieu>();
        locList.add(loc);
    }

    public void addLocs(List<Lieu> locs) {
        if (locs != null){
            for (Lieu loc : locs){
                addLoc(loc);
            }
        }
    }

    public User findUser(String username){
        for (User user : listeUtilisateurs) {
                if (user.getUsername().equals(username)) return user;
        }
        return null;
    }

    public Lieu findLocation(String location){
        for (Lieu lieu : locList) {
            if (lieu.getName().equals(location)) return lieu;
        }
        return null;
    }

    public User findCurrentUser(){
        for (User user : listeUtilisateurs) {
            return user;
        }
        return null;
    }

    public List<Lieu> getLocList() {return locList; }
    public void setLocList(List<Lieu> locList) { this.locList = locList; }
    public void addEvent(Lieu lieu) { locList.add(lieu); }

    public Event getEvent() {return event; }
    public void setEvent(Event event) { this.event = event; }


}
