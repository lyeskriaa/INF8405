package com.inf8405.projet_inf8405.utils;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.inf8405.projet_inf8405.model.ListeUsers;
import com.inf8405.projet_inf8405.model.User;

/**
 * Created by Louise on 2017-04-12.
 */

public class InfoWindowOnClickListener implements GoogleMap.OnInfoWindowClickListener {
    Context context;
    public InfoWindowOnClickListener(Context context){
        this.context = context;
    }
    public void onInfoWindowClick(Marker marker) {
        User interlocuteur = ListeUsers.getInstance().findUser(marker.getTitle());

        //find user
        // if current user return and do nothing
        // else start conversation
    }
}
