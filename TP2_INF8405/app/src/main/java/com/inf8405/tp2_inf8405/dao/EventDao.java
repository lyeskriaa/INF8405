package com.inf8405.tp2_inf8405.dao;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.inf8405.tp2_inf8405.activities.MapsActivity;
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

    private void updateData(DataSnapshot dataSnapshot) {
        Log.d(TAG,"UPDATE EVENT");
    }

    private void readData(DataSnapshot dataSnapshot) {
        Log.d(TAG,"READ EVENT");
        if (dataSnapshot.hasChildren()) {
            String lieuName      = dataSnapshot.child("eventName").getValue() != null ? dataSnapshot.child("eventName").getValue().toString() : null;

        }
    }

    public  void addEventChild(Event event) {
        Map<String, Object> eventToAdd = new HashMap<String, Object>();
        eventToAdd.put(Enum.COORDINATE.toString(), event.getLieuChoisit());
        eventToAdd.put("dateStart",event.getDateStart());
        eventToAdd.put("dateEnd",event.getDateEnd());
        eventToAdd.put("eventName",event.getEventName());
        eventToAdd.put("picture",event.getPicture());
        eventToAdd.put("going",event.getGoing());
        eventToAdd.put("maybe",event.getMaybe());
        eventToAdd.put("notGoing",event.getNotGoing());
        eventRef.child(event.getEventName()).setValue(eventToAdd);
    }

}
