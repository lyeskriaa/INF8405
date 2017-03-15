package com.inf8405.tp2_inf8405.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Louise on 2017-03-15.
 */

public class Event {
    private List<Lieu> listPossible;
    private Lieu lieuChoisi;
    private String eventName;
    private String pictureURI;

    public Event(List<Lieu> listLieu, Lieu lieuChoisi, String eventName, String pictureURI){
        listPossible = listLieu;
        this.lieuChoisi = lieuChoisi;
        this.pictureURI = pictureURI;
        this.eventName = eventName;
    }
    public Event(List<Lieu> listLieu, String eventName, String pictureURI){
        this(listLieu, null, eventName, pictureURI);
    }
    public Event(){
        this(new ArrayList<Lieu>(), null, null, null);
    }

    public boolean addPossible(Lieu lieu){
        if (listPossible.size() < 3){
            listPossible.add(lieu);
            return true;
        }
        return false;
    }

    public void setPossible(List<Lieu> listLieu){ this.listPossible = listLieu; }
    public List<Lieu> getListPossible() { return listPossible; }

    public void chose(Lieu lieu) { lieuChoisi = lieu; }
    public Lieu getChoisit(){ return lieuChoisi; }

    public void setEventName(String eventName) { this.eventName = eventName; }
    public String getEventName() { return eventName; }

    public void setPictureURI(String pictureURI) { this.pictureURI = pictureURI; }
    public String getPictureURI() { return pictureURI; }


}
