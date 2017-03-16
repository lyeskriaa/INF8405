package com.inf8405.tp2_inf8405.dao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inf8405.tp2_inf8405.model.Coordinate;
import com.inf8405.tp2_inf8405.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Louise on 2017-03-07.
 */

public class GroupDao {

    private DatabaseReference groupRef;
    private final String GROUPS = "groups";

    public GroupDao() {
        groupRef = FirebaseDatabase.getInstance().getReference(GROUPS);
    }

    public DatabaseReference getGroupRef() {
        return groupRef;
    }

    public void addGroupChild(String groupName, Coordinate coordinate, User user) {
        Map<String, String> userData = new HashMap<String, String>();
        userData.put("username", user.getUsername());
        userData.put("pictureURI", user.getPicture());
        userData.put("organisateur", String.valueOf(user.isOrganisateur()));

        groupRef.child(groupName).child("users").child(userData.get("username")).setValue(userData);
        groupRef.child(groupName).child("users").child(userData.get("username")).child("coordinate").setValue(coordinate);

    }
}
