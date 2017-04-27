package com.inf8405.projet_inf8405.utils;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.inf8405.projet_inf8405.activities.ChatRoomActivity;
import com.inf8405.projet_inf8405.fireBaseHelper.UserDBHelper;
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
        User interlocuteur = ListeUsers.getInstance().findUserById(marker.getTitle());
        if (interlocuteur.getId().equals(UserDBHelper.getInstance().getCurrentUser().getId())){
            return;
        }

        Intent intent = new Intent(context, ChatRoomActivity.class);
        intent.putExtra("user_id", interlocuteur.getId());
        intent.putExtra("user_name", interlocuteur.getUsername());
        context.startActivity(intent);
    }
}
