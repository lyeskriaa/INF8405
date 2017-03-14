package com.inf8405.tp2_inf8405.dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inf8405.tp2_inf8405.model.Coordinate;

import java.util.Map;

/**
 * Created by Louise on 2017-03-07.
 */

public class GroupDao {

    private DatabaseReference groupRef;
    private boolean childExist = false;

    public GroupDao() {
        groupRef = FirebaseDatabase.getInstance().getReference("groups");
    }

    public DatabaseReference getGroupRef() {
        return groupRef;
    }

    public boolean childExist(final String childName) {
        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(childName)) {
                    childExist = true;
                }
                else {
                    childExist = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // nothing here
            }
        });
        return childExist;
    }

    public void addGroupChild(String groupName, Coordinate coordinate, Map<String, String> userData) {
         groupRef.child(groupName).child("users").child(userData.get("username")).setValue(userData);
         groupRef.child(groupName).child("users").child(userData.get("username")).child("coordinate").setValue(coordinate);
    }
}
