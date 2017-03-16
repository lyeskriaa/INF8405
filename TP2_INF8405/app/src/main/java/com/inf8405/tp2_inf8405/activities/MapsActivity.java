package com.inf8405.tp2_inf8405.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.inf8405.tp2_inf8405.R;
import com.inf8405.tp2_inf8405.infoWindows.InfoWindow;
import com.inf8405.tp2_inf8405.model.Group;
import com.inf8405.tp2_inf8405.model.User;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


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
        refresh();

    }

    public void refresh(){
        //TODO refresh group
        setUsersMarkers(Group.getListeUtilisateurs());
        setLocationMarkers();
        setEventMarkers();
    }


    public void setUsersMarkers(List<User> users){
        for (User user : users){
            LatLng userPosition = new LatLng(user.getCoordinate().latitude, user.getCoordinate().longitude);
            MarkerOptions marker = new MarkerOptions();
            marker.position(userPosition);
            marker.title(user.getUsername());
            marker.snippet("user:image"+user.getPicture());
            mMap.addMarker(marker);
        }
    }

    public void setLocationMarkers() {
        //TODO location markers
    }

    public void setEventMarkers() {
        //TODO events markers
    }
}
