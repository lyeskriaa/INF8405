package com.inf8405.tp2_inf8405.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inf8405.tp2_inf8405.R;
import com.inf8405.tp2_inf8405.dao.ProfileDao;
import com.inf8405.tp2_inf8405.infoWindows.InfoWindow;
import com.inf8405.tp2_inf8405.infoWindows.InfoWindowClickListener;
import com.inf8405.tp2_inf8405.model.Event;
import com.inf8405.tp2_inf8405.model.Group;
import com.inf8405.tp2_inf8405.model.Lieu;
import com.inf8405.tp2_inf8405.model.User;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Group group;
    private static MapsActivity mapsActivity = null;
    private User currentUser;
    private boolean votesCompleted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        group = Group.getGroup();
        currentUser = ProfileDao.getInstance().getCurrentUser();
        mapsActivity = this;
    }

    public static MapsActivity getMapsActivity() {return mapsActivity;}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Button clickButton = (Button) findViewById(R.id.refresh);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsActivity.this.refresh();
            }
        });
        FloatingActionButton eventButton = (FloatingActionButton) findViewById(R.id.addEventBtn);
        if(!currentUser.isOrganisateur()) eventButton.setVisibility(View.GONE);
        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(votesCompleted) {
                    MapsActivity.this.gotoEventActivity();
                }
                else {
                    AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this)
                            .setTitle("Alerte")
                            .setMessage("Les votes ne sont pas encore finalisés !")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        mMap = googleMap;
        mMap.setInfoWindowAdapter(new InfoWindow(this));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                if (currentUser == null) {
                    Log.e("MapsActivity", "Didn't find current user!!!!");
                    return;
                }
                if (currentUser.isOrganisateur()) {
                    if (group.getLocList().size() < 3) {
                        Intent intent = new Intent(MapsActivity.this, NewLocationActivity.class);
                        Bundle b = new Bundle();
                        b.putDouble("longitude", latLng.longitude);
                        b.putDouble("latitude", latLng.latitude);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                    else {
                        AlertDialog dialog = new AlertDialog.Builder(MapsActivity.this)
                                .setTitle("Alerte")
                                .setMessage("Vous avez déjà choisis trois lieux !")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            }
        });

        mMap.setOnInfoWindowClickListener(new InfoWindowClickListener(this));
        //todo            je ne pense pas que refresh(); est utile ici car les data sont pas encore chargées
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.5, -73.6), 12.0f));

    }

    public void refresh(){
        mMap.clear();
        if (group.getListeUtilisateurs()==null || group.getListeUtilisateurs().isEmpty()) Log.e("MapActivity","No users!!!");
        setUsersMarkers(group.getListeUtilisateurs());
        if (group.getLocList()==null || group.getLocList().isEmpty()) Log.e("MapActivity","No locations!!!");
        setLocationMarkers(group.getLocList());
        if (group.getEvent() == null) Log.e("MapActivity","No event!!!");
        setEventMarker(group.getEvent());
    }

    public boolean isVotesCompleted() {
        return votesCompleted;
    }

    public void setVotesCompleted(boolean votesCompleted) {
        this.votesCompleted = votesCompleted;
    }

    public void gotoEventActivity() {
        if (currentUser.isOrganisateur()) {
            Intent intent = new Intent(MapsActivity.this, CreateEventActivity.class);
            startActivity(intent);
        }
    }


    private void setUsersMarkers(List<User> users){
        if (users == null) return;
        for (User user : users){
            LatLng userPosition = new LatLng(user.getCoordinate().latitude, user.getCoordinate().longitude);
            MarkerOptions marker = new MarkerOptions();
            marker.position(userPosition);
            marker.title(user.getUsername());
            marker.snippet("user:image:"+user.getPicture());
            mMap.addMarker(marker);
        }
    }

    private void setLocationMarkers(List<Lieu> locations) {
        if (locations == null) return;
        for (Lieu location : locations){
            LatLng userPosition = new LatLng(location.getCoordinate().latitude, location.getCoordinate().longitude);
            MarkerOptions marker = new MarkerOptions();
            marker.position(userPosition);
            marker.title(location.getName());
            marker.snippet("location:image:"+location.getPicture());
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            mMap.addMarker(marker);
        }
    }

    private void setEventMarker(Event event) {
        if (event == null) return;
        LatLng userPosition = new LatLng(event.getLieuChoisit().latitude, event.getLieuChoisit().longitude);
        MarkerOptions marker = new MarkerOptions();
        marker.position(userPosition);
        marker.title(event.getEventName());
        marker.snippet("event:image:"+event.getPicture());
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.addMarker(marker);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            Toast.makeText(this, "Appuiez une deuxième fois si vous êtes sûr de vouloir quitter.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
        }

    }

}
