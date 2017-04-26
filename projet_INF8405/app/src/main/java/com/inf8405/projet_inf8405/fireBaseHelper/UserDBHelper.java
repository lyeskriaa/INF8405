package com.inf8405.projet_inf8405.fireBaseHelper;

import android.location.Location;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inf8405.projet_inf8405.activities.MapsActivity;
import com.inf8405.projet_inf8405.model.ListeUsers;
import com.inf8405.projet_inf8405.model.User;
import com.inf8405.projet_inf8405.utils.Enum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LyesKriaa on 17-04-09.
 */

public class UserDBHelper {
    private final String TAG = "USER_DB_HELPER";
    private DatabaseReference usersRef = null;
    private User currentUser;
    private static UserDBHelper INSTANCE = null;
    private ChildEventListener childEventListener = null;


    private UserDBHelper() {
        usersRef = FirebaseDatabase.getInstance().getReference().child(Enum.USERS.toString()).getRef();
        usersRef.child(Enum.USERS.toString()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.e(TAG, "onChildAdded:" + dataSnapshot.getKey());
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    readData(user);
                }
                if(MapsActivity.getMapsActivity() != null ) MapsActivity.getMapsActivity().refresh();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                if(MapsActivity.getMapsActivity() != null ) MapsActivity.getMapsActivity().refresh();
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

    public void setUserProfileRef(String user) {
        usersRef = FirebaseDatabase.getInstance().getReference().child(Enum.USERS.toString()).getRef();
        Log.e(TAG, "refUser set to : " + usersRef.getRef());
    }

    public void updateUserLocation(Location location, String userId) {
        usersRef.child("coordinate").child("latitude").setValue(location.getLatitude());
        usersRef.child("coordinate").child("longitude").setValue(location.getLongitude());
    }

    public void addUserChild(User user) {
        Map<String, Object> userToAdd = new HashMap<String, Object>();
        userToAdd.put(Enum.COORDINATE.toString(), user.getCoordinate());
        userToAdd.put("username", user.getUsername());
        userToAdd.put("pictureURI", user.getPicture());
        userToAdd.put("description", user.getDescription());
        userToAdd.put("sexe", user.getSexe());

        usersRef.setValue(userToAdd);
    }

    public User readData(DataSnapshot dataSnapshot) {
        User user = new User();
        if(dataSnapshot.hasChildren()) {
            String username      = dataSnapshot.child("username").getValue() != null ? dataSnapshot.child("username").getValue().toString() : null;
            String picture       = dataSnapshot.child("pictureURI").getValue() != null ? dataSnapshot.child("pictureURI").getValue().toString() : null;
            String description   = dataSnapshot.child("description").getValue() != null ? dataSnapshot.child("description").getValue().toString() : null;
            double lon           = dataSnapshot.child("coordinate").getValue() != null ? Double.valueOf(dataSnapshot.child("coordinate").child("longitude").getValue().toString()) : 0;
            double lat           = dataSnapshot.child("coordinate").getValue() != null ? Double.valueOf(dataSnapshot.child("coordinate").child("latitude").getValue().toString()) : 0;
            String sexe          = dataSnapshot.child("sexe").getValue() != null ? dataSnapshot.child("sexe").getValue().toString() : null;

            // put the user in usersList of the MapsActivity
            user = new User(dataSnapshot.getKey(), username, description, picture, lon, lat, sexe);
            ListeUsers.getInstance().addUser(user);
        }

        return user;
    }

    public void destroy() {
        currentUser = null;
        usersRef = null;
        INSTANCE = null;
    }
}

