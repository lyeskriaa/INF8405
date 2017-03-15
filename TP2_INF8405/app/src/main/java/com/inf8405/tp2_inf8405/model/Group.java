package com.inf8405.tp2_inf8405.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louise on 2017-03-07.
 */

public class Group {
    private String nomGroupe;
    static private List<User> listeUtilisateurs = new ArrayList<User>();;
    private static final Object usersLock = new Object();
    private User organisateur;

    public Group(String nomGroupe, List<User> utilisateurs){
        this.nomGroupe = nomGroupe;
        synchronized (usersLock){
            listeUtilisateurs = new ArrayList<User>();
        }
        addUsers(utilisateurs);
    }

    public Group(String nomGroupe){
        this.nomGroupe = nomGroupe;
        synchronized (usersLock){
            listeUtilisateurs = new ArrayList<User>();
        }
    }

    public User getOrganisateur(){ return organisateur; }
    public String getNomGroupe() { return nomGroupe; }

    static public List<User> getListeUtilisateurs() {
        synchronized (usersLock) {
            return listeUtilisateurs;
        }
    }

    public void addUser(User newUser) {
        if (newUser != null && findUser(newUser.getUsername()) == null) {
            synchronized (usersLock) {
                listeUtilisateurs.add(newUser);
            }
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

    public User findUser(String username){
        synchronized (usersLock) {
            for (User user : listeUtilisateurs) {
                if (user.getUsername().equals(username)) return user;
            }
        }
        return null;
    }
}
