package com.inf8405.tp2_inf8405.model;

import java.util.ArrayList;

/**
 * Created by Louise on 2017-03-07.
 */

public class Group {
    private String nomGroupe;
    private ArrayList<User> listeUtilisateurs;

    public Group(String nomGroupe,User utilisateur){
        listeUtilisateurs = new ArrayList<User>();
        this.nomGroupe = nomGroupe;
        listeUtilisateurs.add(utilisateur);
    }

    public Group(){
        listeUtilisateurs = new ArrayList<User>();
    }

    public User getOrganisateur(){
        return listeUtilisateurs.get(0);
    }


    public String getNomGroupe() {
        return nomGroupe;
    }

    public ArrayList<User> getListeUtilisateurs() {
        return listeUtilisateurs;
    }

    public void setListeUtilisateurs(ArrayList<User> users) {
        this.listeUtilisateurs = users;
    }

    public void setNomGroupe(String groupname) {
        this.nomGroupe = groupname;
    }

}
