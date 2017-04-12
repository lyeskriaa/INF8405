package com.inf8405.projet_inf8405.utils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import android.content.Context;

/**
 * Created by Louise on 2017-04-12.
 */

public class InfoWindowOnClickListener implements GoogleMap.OnInfoWindowClickListener {
    Context context;
    public InfoWindowOnClickListener(Context context){
        this.context = context;
    }
    public void onInfoWindowClick(Marker marker) {
        //find user
        // if current user return and do nothing
        // else start conversation
    }
}
