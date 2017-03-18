package com.inf8405.tp2_inf8405.dao;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inf8405.tp2_inf8405.model.Coordinate;
import com.inf8405.tp2_inf8405.model.Group;
import com.inf8405.tp2_inf8405.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Louise on 2017-03-07.
 */

public class GroupDao {

    private final String GROUPS = "groups";
    private final String USERS = "users";
    private final String TAG = "GROUP_DAO";
    private DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference(GROUPS);

    private GroupDao() {}

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
        Map<String, String> userData = new HashMap<String, String>();
        userData.put("username", user.getUsername());
        userData.put("pictureURI", user.getPicture());
        userData.put("organisateur", String.valueOf(user.isOrganisateur()));

        groupRef.child(groupName).child(USERS).child(userData.get("username")).setValue(userData);
        groupRef.child(groupName).child(USERS).child(userData.get("username")).child("coordinate").setValue(coordinate);

    }

    public List<User> getGroupUsers(final Group group)
    {
        final List<User> listeUsers = new ArrayList<User>();
        groupRef.child(group.getNomGroupe()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                for(DataSnapshot snapUser : dataSnapshot.getChildren()){
                    String username      = snapUser.child("username") != null ? snapUser.child("username").getValue().toString() : null;
                    String picture       = snapUser.child("pictureURI") != null ? snapUser.child("pictureURI").getValue().toString() : null;
                    boolean organisateur = snapUser.child("organisateur") != null ? Boolean.valueOf(snapUser.child("organisateur").getValue().toString()) : null;
                    double lon           = snapUser.child("coordinate")!= null? Double.valueOf(snapUser.child("coordinate").child("longitude").getValue().toString()) : null;
                    double lat           = snapUser.child("coordinate")!= null? Double.valueOf(snapUser.child("coordinate").child("latitude").getValue().toString()) : null;
                    User user = new User(username, picture, organisateur, lon,lat, group, false);
                    group.addUser(user);
                    listeUsers.add(user);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
                for(DataSnapshot snapUser : dataSnapshot.getChildren()){
                    String username      = snapUser.child("username") != null ? snapUser.child("username").getValue().toString() : null;
                    String picture       = snapUser.child("pictureURI") != null ? snapUser.child("pictureURI").getValue().toString() : null;
                    boolean organisateur = snapUser.child("organisateur") != null ? Boolean.valueOf(snapUser.child("organisateur").getValue().toString()) : null;
                    double lon           = snapUser.child("coordinate")!= null? Double.valueOf(snapUser.child("coordinate").child("longitude").getValue().toString()) : null;
                    double lat           = snapUser.child("coordinate")!= null? Double.valueOf(snapUser.child("coordinate").child("latitude").getValue().toString()) : null;
                    User user = new User(username, picture, organisateur, lon,lat, group, false);
                    group.removeUser(user);
                    listeUsers.remove(user);
                }
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

        return listeUsers;
    }
}
