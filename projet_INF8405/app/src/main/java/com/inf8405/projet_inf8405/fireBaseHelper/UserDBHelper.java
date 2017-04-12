package com.inf8405.projet_inf8405.fireBaseHelper;

import android.location.Location;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inf8405.projet_inf8405.model.User;

/**
 * Created by LyesKriaa on 17-04-09.
 */

class UserDBHelper {
    private final String TAG = "USER_DB_HELPER";
    private DatabaseReference usersRef = null;
    private User currentUser;
    private static UserDBHelper INSTANCE = null;



    private UserDBHelper() {
    }

    public static UserDBHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserDBHelper();
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
        usersRef = FirebaseDatabase.getInstance().getReference();
        Log.e("ProfileDAO", "refUser set to : " + usersRef.getRef());
    }

    public void updateUserLocation(Location location, String userName) {
        usersRef.child("coordinate").child("latitude").setValue(location.getLatitude());
        usersRef.child("coordinate").child("longitude").setValue(location.getLongitude());
    }


    public void destroy() {
        currentUser = null;
        usersRef = null;
        INSTANCE = null;
    }
}

