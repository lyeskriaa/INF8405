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
    private DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference(Enum.GROUPS.toString());

    private GroupDao() {
        groupRef.child(Group.getGroup().getNomGroupe()).child(Enum.USERS.toString()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                String username      = dataSnapshot.child("username") != null ? dataSnapshot.child("username").getValue().toString() : null;
                String picture       = dataSnapshot.child("pictureURI") != null ? dataSnapshot.child("pictureURI").getValue().toString() : null;
                boolean organisateur = dataSnapshot.child("organisateur") != null ? Boolean.valueOf(dataSnapshot.child("organisateur").getValue().toString()) : null;
                double lon           = dataSnapshot.child("coordinate")!= null? Double.valueOf(dataSnapshot.child("coordinate").child("longitude").getValue().toString()) : 0;
                double lat           = dataSnapshot.child("coordinate")!= null? Double.valueOf(dataSnapshot.child("coordinate").child("latitude").getValue().toString()) : 0;
                User user = new User(username, picture, organisateur, lon,lat, Group.getGroup(), false);
                if(Group.getGroup().findUser(username)==null)
                    Group.getGroup().addUser(user);

                if(MapsActivity.getMapsActivity() != null ) MapsActivity.getMapsActivity().refresh();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                String username      = dataSnapshot.child("username") != null ? dataSnapshot.child("username").getValue().toString() : null;
                double lon           = dataSnapshot.child("coordinate")!= null? Double.valueOf(dataSnapshot.child("coordinate").child("longitude").getValue().toString()) : 0;
                double lat           = dataSnapshot.child("coordinate")!= null? Double.valueOf(dataSnapshot.child("coordinate").child("latitude").getValue().toString()) : 0;
                Group.getGroup().findUser(username).setCoordinate(lon, lat);
                if(MapsActivity.getMapsActivity() != null ) MapsActivity.getMapsActivity().refresh();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
                String username      = dataSnapshot.child("username") != null ? dataSnapshot.child("username").getValue().toString() : null;
                String picture       = dataSnapshot.child("pictureURI") != null ? dataSnapshot.child("pictureURI").getValue().toString() : null;
                boolean organisateur = dataSnapshot.child("organisateur") != null ? Boolean.valueOf(dataSnapshot.child("organisateur").getValue().toString()) : null;
                double lon           = dataSnapshot.child("coordinate")!= null? Double.valueOf(dataSnapshot.child("coordinate").child("longitude").getValue().toString()) : 0;
                double lat           = dataSnapshot.child("coordinate")!= null? Double.valueOf(dataSnapshot.child("coordinate").child("latitude").getValue().toString()) : 0;
                User user = new User(username, picture, organisateur, lon,lat, Group.getGroup(), false);
                Group.getGroup().removeUser(user);

                if(MapsActivity.getMapsActivity() != null ) MapsActivity.getMapsActivity().refresh();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        });
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

}
