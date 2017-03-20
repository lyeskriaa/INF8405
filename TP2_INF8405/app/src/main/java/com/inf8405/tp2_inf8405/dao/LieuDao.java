package com.inf8405.tp2_inf8405.dao;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.inf8405.tp2_inf8405.activities.MapsActivity;
import com.inf8405.tp2_inf8405.model.Group;
import com.inf8405.tp2_inf8405.model.User;

import static com.inf8405.tp2_inf8405.model.Enum.LIEUX;

/**
 * Created by LyesKriaa on 17-03-20.
 */

public class LieuDao {
    private static LieuDao Instance = null;
    private DatabaseReference lieuxRef = null;
    private final String TAG = "LIEU DAO";

    public static LieuDao getInstance() {
        if (Instance == null) {
            Instance = new LieuDao();
        }
        return Instance;
    }

    public DatabaseReference getLieuxRef() {
        return lieuxRef;
    }

    private LieuDao() {
        lieuxRef = GroupDao.getInstance().getGroupRef().child(LIEUX.toString());
        lieuxRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                for(DataSnapshot snapUser : dataSnapshot.getChildren()){
                    String username      = snapUser.child("username") != null ? snapUser.child("username").getValue().toString() : null;
                    String picture       = snapUser.child("pictureURI") != null ? snapUser.child("pictureURI").getValue().toString() : null;
                    boolean organisateur = snapUser.child("organisateur") != null ? Boolean.valueOf(snapUser.child("organisateur").getValue().toString()) : null;
                    double lon           = snapUser.child("coordinate")!= null? Double.valueOf(snapUser.child("coordinate").child("longitude").getValue().toString()) : null;
                    double lat           = snapUser.child("coordinate")!= null? Double.valueOf(snapUser.child("coordinate").child("latitude").getValue().toString()) : null;
                    User user = new User(username, picture, organisateur, lon,lat, Group.getGroup(), false);
                    //Group.getGroup().addLieu(user);
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
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
                for(DataSnapshot snapUser : dataSnapshot.getChildren()){
                    String username      = snapUser.child("username") != null ? snapUser.child("username").getValue().toString() : null;
                    String picture       = snapUser.child("pictureURI") != null ? snapUser.child("pictureURI").getValue().toString() : null;
                    boolean organisateur = snapUser.child("organisateur") != null ? Boolean.valueOf(snapUser.child("organisateur").getValue().toString()) : null;
                    double lon           = snapUser.child("coordinate")!= null? Double.valueOf(snapUser.child("coordinate").child("longitude").getValue().toString()) : null;
                    double lat           = snapUser.child("coordinate")!= null? Double.valueOf(snapUser.child("coordinate").child("latitude").getValue().toString()) : null;
                    User user = new User(username, picture, organisateur, lon,lat, Group.getGroup(), false);
                    Group.getGroup().removeUser(user);
                }
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

}
