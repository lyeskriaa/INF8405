package com.inf8405.tp2_inf8405.dao;

import android.location.Location;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.inf8405.tp2_inf8405.model.Enum;
import com.inf8405.tp2_inf8405.model.User;

/**
 * Created by LyesKriaa on 17-03-07.
 */

@IgnoreExtraProperties
public class ProfileDao {

    private final String TAG = "PROFILE_DAO";
    private DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference(Enum.GROUPS.toString());
    private User currentUser;

    private ProfileDao() {}

    private static ProfileDao INSTANCE = null;

    public static ProfileDao getInstance()
    {
        if (INSTANCE == null) {
            INSTANCE = new ProfileDao();
        }
        return INSTANCE;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public DatabaseReference getUsersRef() {
        return usersRef;
    }

    public void setUserProfileRef(String user, String group) {
        usersRef = FirebaseDatabase.getInstance().getReference(Enum.GROUPS.toString()).child(group).child(Enum.USERS.toString()).child(user).getRef();
        Log.e("ProfileDAO", "refUser set to : " + usersRef.getRef());
    }

    public void updateUserLocation(Location location, String userName) {
        usersRef.child("coordinate").child("latitude").setValue(location.getLatitude());
        usersRef.child("coordinate").child("longitude").setValue(location.getLongitude());
    }


}
