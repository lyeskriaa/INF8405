package com.inf8405.tp2_inf8405.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inf8405.tp2_inf8405.R;
import com.inf8405.tp2_inf8405.infoWindows.InfoWindow;
import com.inf8405.tp2_inf8405.model.Event;
import com.inf8405.tp2_inf8405.model.Group;
import com.inf8405.tp2_inf8405.model.Lieu;
import com.inf8405.tp2_inf8405.model.User;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Group group;
    private static MapsActivity mapsActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        group = Group.getGroup();
        mapsActivity = this;
    }

    public static MapsActivity getMapsActivity() {return mapsActivity;}

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Button clickButton = (Button) findViewById(R.id.refresh);
        clickButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsActivity.this.refresh();
            }
        });
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new InfoWindow(this));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng).title("marker here"));
                //TODO add input name and photo
            }
        });
        refresh();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(45.5,-73.6)));

    }

    public void refresh(){
        mMap.clear();
        if (group.getListeUtilisateurs()==null || group.getListeUtilisateurs().isEmpty()) Log.e("MapActivity","No users!!!");
        setUsersMarkers(group.getListeUtilisateurs());
        setLocationMarkers(group.getLocList());
        setEventMarker(group.getEvent());
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
        marker.snippet("location::"+event.getDateStart()+"::"+event.getDateEnd()+":image:"+event.getPicture());
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.addMarker(marker);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}
