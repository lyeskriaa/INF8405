package com.inf8405.tp2_inf8405.model;

import com.inf8405.tp2_inf8405.dao.GroupDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louise on 2017-03-07.
 */

public class Group {
    private String nomGroupe;
    static private List<User> listeUtilisateurs = new ArrayList<User>();
    private User organisateur;
    static List<Lieu> locList;
    static Event event;

    public Group(String nomGroupe, List<User> utilisateurs, List<Lieu> locationList, Event event){
        this.nomGroupe = nomGroupe;
        listeUtilisateurs = new ArrayList<User>();
        addUsers(utilisateurs);
        locList = new ArrayList<Lieu>();
        addLocs(locationList);
        this.event = event;
    }

    public Group(String nomGroupe, List<User> utilisateurs){
        this(nomGroupe, utilisateurs, new ArrayList<Lieu>(), null);
    }

    public Group(String nomGroupe){
        this.nomGroupe = nomGroupe;
        listeUtilisateurs = new ArrayList<User>();
        locList = new ArrayList<Lieu>();
        event = null;
}

    public User getOrganisateur(){ return organisateur; }
    public String getNomGroupe() { return nomGroupe; }

    public List<User> getListeUtilisateurs() {
        if (listeUtilisateurs.isEmpty()) {
            listeUtilisateurs = GroupDao.getInstance().getGroupUsers(this);
        }
        return listeUtilisateurs;

    }

    public void addUser(User newUser) {
        if (newUser != null && findUser(newUser.getUsername()) == null) {
            listeUtilisateurs.add(newUser);
            if (newUser.isOrganisateur()) organisateur = newUser;
        }
    }

    public void addUsers(List<User> utilisateurs) {
        if (utilisateurs != null){
            for (User user : utilisateurs){
                addUser(user);
            }
        }
    }

    public void addLoc(Lieu loc) {
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

    static public List<Lieu> getLocList() {return locList; }
    static public void setLocList(List<Lieu> locList) { Group.locList = locList; }
    static public void addEvent(Lieu lieu) { locList.add(lieu); }

    static public Event getEvent() {return event; }
    public void setEvent(Event event) { this.event = event; }


}
