package com.inf8405.tp2_inf8405.dao;

import android.location.Location;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.inf8405.tp2_inf8405.model.Enum;

/**
 * Created by LyesKriaa on 17-03-07.
 */

@IgnoreExtraProperties
public class ProfileDao {

    private final String TAG = "PROFILE_DAO";
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference(Enum.GROUPS.toString());

    private ProfileDao() {}

    private static ProfileDao INSTANCE = null;

    public static ProfileDao getInstance()
    {
        if (INSTANCE == null) {
            INSTANCE = new ProfileDao();
        }
        return INSTANCE;
    }

    public DatabaseReference getUsersRef() {
        return usersRef;
    }

    public void setUserProfileRef(String s, String g) {
        usersRef = FirebaseDatabase.getInstance().getReference(Enum.GROUPS.toString()).child(Enum.USERS.toString()).child(g).child(s).getRef();
    }

    public void updateUserLocation(Location location) {
        usersRef.child("coordinate").child("latitude").setValue(location.getLatitude());
        usersRef.child("coordinate").child("longitude").setValue(location.getLongitude());
    }


}
