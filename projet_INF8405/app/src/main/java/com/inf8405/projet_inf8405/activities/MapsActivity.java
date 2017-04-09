package com.inf8405.projet_inf8405.activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.inf8405.projet_inf8405.R;

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
        mMap = googleMap;

        //TODO add markers
    }

    public void refresh() {
        mMap.clear();
        // for each user add marker
        //for (User user : users){
        //    LatLng userPosition = new LatLng(user.getCoordinate().latitude, user.getCoordinate().longitude);
        //    MarkerOptions marker = new MarkerOptions();
        //    marker.position(userPosition);
        //    marker.title(user.getUsername());
        //    marker.snippet("user:image:"+user.getPicture());
        //    mMap.addMarker(marker);
        //    TODO if current user, change color. if conversation, change to a third color.
        //}
    }
}
