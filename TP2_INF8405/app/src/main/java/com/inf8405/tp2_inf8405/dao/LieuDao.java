package com.inf8405.tp2_inf8405.dao;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.inf8405.tp2_inf8405.activities.MapsActivity;
import com.inf8405.tp2_inf8405.model.Coordinate;
import com.inf8405.tp2_inf8405.model.Enum;
import com.inf8405.tp2_inf8405.model.Group;
import com.inf8405.tp2_inf8405.model.Lieu;

import java.util.HashMap;
import java.util.Map;

import static android.util.Log.d;
import static com.inf8405.tp2_inf8405.model.Group.getGroup;


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
        lieuxRef = GroupDao.getInstance().getGroupRef().child(getGroup().getNomGroupe()).child(Enum.LIEUX.toString());
        lieuxRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                d(TAG, "onChildAdded:" + dataSnapshot);
                if(dataSnapshot.hasChildren()) {
                    String lieuName      = dataSnapshot.child("name") != null ? dataSnapshot.child("name").getValue().toString() : null;
                    String picture       = dataSnapshot.child("picture") != null ? dataSnapshot.child("picture").getValue().toString() : null;
                    float votes            = dataSnapshot.child("votes") != null ? Float.valueOf(dataSnapshot.child("votes").getValue().toString()) : 0;
                    double lon           = dataSnapshot.child("coordinate")!= null? Double.valueOf(dataSnapshot.child("coordinate").child("longitude").getValue().toString()) : 0;
                    double lat           = dataSnapshot.child("coordinate")!= null? Double.valueOf(dataSnapshot.child("coordinate").child("latitude").getValue().toString()) : 0;
                    Coordinate coordinate = new Coordinate(lon,lat);
                    Lieu lieu = new Lieu(coordinate,lieuName, picture, votes);
                    getGroup().addLoc(lieu);
                }
                if(MapsActivity.getMapsActivity() != null ) MapsActivity.getMapsActivity().refresh();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                String lieuName = dataSnapshot.child("name") != null ? dataSnapshot.child("name").getValue().toString() : null;
                float votes     = dataSnapshot.child("votes") != null ? Float.valueOf(dataSnapshot.child("votes").getValue().toString()) : 0;
                int nbrVotes    = dataSnapshot.child("nbrVotes") != null ? Integer.valueOf(dataSnapshot.child("nbrVotes").getValue().toString()) : 0;
                if(lieuName != null) {
                    Lieu lieuToUpdate = Group.getGroup().findLocation(lieuName);
                    lieuToUpdate.setVotes(votes);
                    lieuToUpdate.setNbrVotes(nbrVotes);
                    // TODO: 17-03-21 la notification
                    for (Lieu votedPlace : Group.getGroup().getLocList()) {

                    }
                    if(nbrVotes == Group.getGroup().getListeUtilisateurs().size()) {

                    }
                }
                if(MapsActivity.getMapsActivity() != null ) MapsActivity.getMapsActivity().refresh();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                d(TAG, "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        });

    }

    public void addLieuChild(Lieu lieu) {
        Map<String, Object> lieuToAdd = new HashMap<String, Object>();
        lieuToAdd.put(Enum.COORDINATE.toString(), lieu.getCoordinate());
        lieuToAdd.put("name", lieu.getName());
        lieuToAdd.put("picture", lieu.getPicture());
        lieuToAdd.put("votes", String.valueOf(lieu.getVotes()));
        lieuxRef.child(lieu.getName()).setValue(lieuToAdd);
    }

    public void saveVote(int vote, Lieu lieu) {
        float oldVote = lieu.getVotes();
        int nbrVotes = lieu.getNbrVotes();
        nbrVotes++;
        float newVote = (float)Math.round(oldVote + vote)/ nbrVotes ;
        Map<String, String> voteToSave = new HashMap<String, String>();
        voteToSave.put("votes",String.format(java.util.Locale.US,"%.2f", newVote));
        voteToSave.put("nbrVotes", String.valueOf(nbrVotes));
        lieuxRef.child(lieu.getName()).setValue(voteToSave);
    }
}
