package com.inf8405.tp2_inf8405.dao;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inf8405.tp2_inf8405.activities.MapsActivity;
import com.inf8405.tp2_inf8405.model.Coordinate;
import com.inf8405.tp2_inf8405.model.Enum;
import com.inf8405.tp2_inf8405.model.Group;
import com.inf8405.tp2_inf8405.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Louise on 2017-03-07.
 */

public class GroupDao {

    private final String TAG = "GROUP_DAO";
    private DatabaseReference groupRef = null;
    private ChildEventListener childEventListener = null;

    private GroupDao() {
        groupRef = FirebaseDatabase.getInstance().getReference(Enum.GROUPS.toString());
        groupRef.child(Group.getGroup().getNomGroupe()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                if(dataSnapshot.getKey().equals(Enum.EVENTS.toString())) {
                    Log.d(TAG, "onChildAdded:" + "========== EVENT ========");
                    EventDao.getInstance();
                    if(dataSnapshot.hasChildren()) {
                        for (DataSnapshot event : dataSnapshot.getChildren()) {
                            EventDao.getInstance().readData(event);
                        }
                    }
                }

                if(dataSnapshot.getKey().equals(Enum.LIEUX.toString())) {
                    Log.d(TAG, "onChildAdded:" + "========== LIEUX ========");
                    LieuDao.getInstance();
                    if(dataSnapshot.hasChildren()) {
                        for (DataSnapshot lieu : dataSnapshot.getChildren()) {
                            LieuDao.getInstance().readData(lieu);
                        }
                    }
                }

                if(dataSnapshot.getKey().equals(Enum.USERS.toString())) {
                    Log.d(TAG, "onChildAdded:" + "========== USERS ========");
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        readData(user);
                    }
                    if(MapsActivity.getMapsActivity() != null ) MapsActivity.getMapsActivity().refresh();
                    // TODO: 17-03-22
                    // on enleve ce listener qui genere beaucoup trop de données et on mets des listener sur des child précis
                    // USERS est le dernier a etre chargé
                    groupRef.child(Group.getGroup().getNomGroupe()).removeEventListener(this);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + "========== FIRST LISTENER GROUP DAO ======== "+dataSnapshot.getKey());
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        });

        keepUpdates();
    }

    private void keepUpdates() {
        groupRef.child(Group.getGroup().getNomGroupe()).child(Enum.USERS.toString()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded: 2 ;" + dataSnapshot.getKey());
                readData(dataSnapshot);
                if(MapsActivity.getMapsActivity() != null ) MapsActivity.getMapsActivity().refresh();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: 2 ;" + dataSnapshot.getKey());
                updateData(dataSnapshot);
                if(MapsActivity.getMapsActivity() != null ) MapsActivity.getMapsActivity().refresh();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(MapsActivity.getMapsActivity() != null ) MapsActivity.getMapsActivity().refresh();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        });
    }

    private void updateData(DataSnapshot dataSnapshot) {
        String username      = dataSnapshot.child("username").getValue() != null ? dataSnapshot.child("username").getValue().toString() : null;
        double lon           = dataSnapshot.child("coordinate").getValue() != null ? Double.valueOf(dataSnapshot.child("coordinate").child("longitude").getValue().toString()) : 0;
        double lat           = dataSnapshot.child("coordinate").getValue() != null ? Double.valueOf(dataSnapshot.child("coordinate").child("latitude").getValue().toString()) : 0;
        User user = Group.getGroup().findUser(username);
        if(user != null) {
            user.setCoordinate(lon, lat);
        }
    }

    private void readData(DataSnapshot dataSnapshot) {
        if(dataSnapshot.hasChildren()) {
            String username      = dataSnapshot.child("username").getValue() != null ? dataSnapshot.child("username").getValue().toString() : null;
            String picture       = dataSnapshot.child("pictureURI").getValue() != null ? dataSnapshot.child("pictureURI").getValue().toString() : null;
            boolean organisateur = dataSnapshot.child("organisateur").getValue() != null ? Boolean.valueOf(dataSnapshot.child("organisateur").getValue().toString()) : null;
            double lon           = dataSnapshot.child("coordinate").getValue() != null ? Double.valueOf(dataSnapshot.child("coordinate").child("longitude").getValue().toString()) : 0;
            double lat           = dataSnapshot.child("coordinate").getValue() != null ? Double.valueOf(dataSnapshot.child("coordinate").child("latitude").getValue().toString()) : 0;
            if(Group.getGroup().findUser(username)==null) {
                User user = new User(username, picture, organisateur, lon,lat, Group.getGroup(), false);
                Group.getGroup().addUser(user);
            }
        }
    }

    private static GroupDao INSTANCE = null;

    public static GroupDao getInstance()
    {
        if (INSTANCE == null) {
            INSTANCE = new GroupDao();
        }
        return INSTANCE;
    }

    public DatabaseReference getGroupRef() {
        return groupRef;
    }

    public void addGroupChild(String groupName, Coordinate coordinate, User user) {
        Map<String, Object> userToAdd = new HashMap<String, Object>();
        userToAdd.put(Enum.COORDINATE.toString(), coordinate);
        userToAdd.put("username", user.getUsername());
        userToAdd.put("pictureURI", user.getPicture());
        userToAdd.put("organisateur", String.valueOf(user.isOrganisateur()));

        if(user.isOrganisateur()) {
            groupRef.child(groupName).child(Enum.LIEUX.toString()).setValue("no elements");
            groupRef.child(groupName).child(Enum.EVENTS.toString()).setValue("no elements");
        }
        groupRef.child(groupName).child(Enum.USERS.toString()).child(user.getUsername()).setValue(userToAdd);
    }

    public void removeGroupChild() {
        // juste valide pour un membre qui veut quitter le groupe et qui n<est pas organisateur
        Group.getGroup().removeUser(ProfileDao.getInstance().getCurrentUser());
        groupRef.child(Group.getGroup().getNomGroupe()).child(Enum.USERS.toString())
                .child(ProfileDao.getInstance().getCurrentUser().getUsername()).removeValue();
        ProfileDao.getInstance().destroy();
    }

    public void destroy() {
        groupRef = null;
        INSTANCE = null;
    }
}
