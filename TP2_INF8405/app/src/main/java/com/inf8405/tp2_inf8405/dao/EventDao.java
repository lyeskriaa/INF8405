package com.inf8405.tp2_inf8405.dao;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.inf8405.tp2_inf8405.activities.MapsActivity;
import com.inf8405.tp2_inf8405.model.Coordinate;
import com.inf8405.tp2_inf8405.model.Enum;
import com.inf8405.tp2_inf8405.model.Event;
import com.inf8405.tp2_inf8405.model.Group;

import java.util.HashMap;
import java.util.Map;

import static android.util.Log.d;

/**
 * Created by LyesKriaa on 17-03-07.
 */

public class EventDao {

    private static EventDao Instance = null;
    private DatabaseReference eventRef = null;
    private final String TAG = "EVENT DAO";

    public static EventDao getInstance() {
        if(Instance == null) {
            Instance = new EventDao();
        }
        return Instance;
    }

    public DatabaseReference getEventRef() {return eventRef;}

    private EventDao() {
        eventRef = GroupDao.getInstance().getGroupRef().child(Group.getGroup().getNomGroupe()).child(Enum.EVENTS.toString());
        eventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                readData(dataSnapshot);
                if(MapsActivity.getMapsActivity() != null )
                    MapsActivity.getMapsActivity().refresh();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                updateData(dataSnapshot);
                if(MapsActivity.getMapsActivity() != null ) MapsActivity.getMapsActivity().refresh();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateData(DataSnapshot dataSnapshot) {
        Log.d(TAG,"UPDATE EVENT");
        if (dataSnapshot.hasChildren()) {
            String eventName      = dataSnapshot.child("eventName").getValue() != null ? dataSnapshot.child("eventName").getValue().toString() : null;
            Event event = Group.getGroup().getEvent();
            if(event.getEventName().equals(eventName)) {
                for (DataSnapshot goinUser : dataSnapshot.child("going").getChildren()) {
                    event.addGoing(goinUser.getValue().toString());
                }

                for (DataSnapshot maybeUser : dataSnapshot.child("maybe").getChildren()) {
                    event.addMaybe(maybeUser.getValue().toString());
                }

                for (DataSnapshot notGoinUser : dataSnapshot.child("notGoing").getChildren()) {
                    event.addNotGoing(notGoinUser.getValue().toString());
                }
            }
        }
    }

    public void readData(DataSnapshot dataSnapshot) {
        Log.d(TAG,"READ EVENT");
        if (dataSnapshot.hasChildren()) {
            String eventName      = dataSnapshot.child("eventName").getValue() != null ? dataSnapshot.child("eventName").getValue().toString() : null;
            String dateStart      = dataSnapshot.child("dateStart").getValue() != null ? dataSnapshot.child("dateStart").getValue().toString() : null;
            String dateEnd      = dataSnapshot.child("dateEnd").getValue() != null ? dataSnapshot.child("dateEnd").getValue().toString() : null;
            String picture      = dataSnapshot.child("picture").getValue() != null ? dataSnapshot.child("picture").getValue().toString() : null;
            double lon           = dataSnapshot.child("coordinate").getValue() != null? Double.valueOf(dataSnapshot.child("coordinate").child("longitude").getValue().toString()) : 0;
            double lat           = dataSnapshot.child("coordinate").getValue() != null? Double.valueOf(dataSnapshot.child("coordinate").child("latitude").getValue().toString()) : 0;
            Coordinate coordinate = new Coordinate(lon,lat);

            Event event = new Event(coordinate, eventName, picture, dateStart, dateEnd);

            for (DataSnapshot goinUser : dataSnapshot.child("going").getChildren()) {
                event.addGoing(goinUser.getValue().toString());
            }

            for (DataSnapshot maybeUser : dataSnapshot.child("maybe").getChildren()) {
                event.addMaybe(maybeUser.getValue().toString());
            }

            for (DataSnapshot notGoinUser : dataSnapshot.child("notGoing").getChildren()) {
                event.addNotGoing(notGoinUser.getValue().toString());
            }

            Group.getGroup().setEvent(event);
        }
    }

    public  void addEventChild(Event event) {
        Map<String, Object> eventToAdd = new HashMap<String, Object>();
        eventToAdd.put(Enum.COORDINATE.toString(), event.getLieuChoisit());
        eventToAdd.put("dateStart",event.getDateStart());
        eventToAdd.put("dateEnd",event.getDateEnd());
        eventToAdd.put("eventName",event.getEventName());
        eventToAdd.put("picture",event.getPicture());
        eventToAdd.put("going",null);
        eventToAdd.put("maybe",null);
        eventToAdd.put("notGoing",null);
        eventRef.child(event.getEventName()).setValue(eventToAdd);
    }

    public void updateGoing(String eventName, String userName) {
        eventRef.child(eventName).child("going").push().setValue(userName);
    }

    public void updateMaybe(String eventName, String userName) {
        eventRef.child(eventName).child("maybe").push().setValue(userName);
    }

    public void updateNotGoing(String eventName, String userName) {
        eventRef.child(eventName).child("notGoing").push().setValue(userName);
    }

}
