package com.inf8405.tp2_inf8405.infoWindows;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.Arrays;

import com.inf8405.tp2_inf8405.model.Lieu;
import com.inf8405.tp2_inf8405.model.Group;

/**
 * Created by Louise on 2017-03-20.
 */

public class InfoWindowClickListener implements GoogleMap.OnInfoWindowClickListener {
    private Context context;

    public InfoWindowClickListener(Context context){
        this.context = context;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String[] strings = marker.getSnippet().split(":image:");
        String[] params = strings[0].split("::");
        String type = params[0];

        if (type.equals("user"))     { return; } // nothing to do
        if (type.equals("location")) { locationInfoWindow(marker); return; }
        if (type.equals("event"))    { eventInfoWindow(marker); return;}
    }

    Lieu location;
    int voteValue = -1;
    private void locationInfoWindow(Marker marker){
        location = Group.getGroup().findLocation(marker.getTitle());
        if (location == null) return; // nothing we can do
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set the alert dialog title
        builder.setTitle("Quel est votre score.");

        final String[] voteValues = new String[]{"1", "2", "3", "4", "5"};


        // Set a single choice items list for alert dialog
        builder.setSingleChoiceItems(
                voteValues, // Items list
                -1, // Index of checked item (-1 = no selection)
                new DialogInterface.OnClickListener() // Item click listener
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Get the alert dialog selected item's text
                        voteValue = Integer.valueOf(Arrays.asList(voteValues).get(i));
                    }
                });

        // Set the alert dialog positive button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                location.vote(voteValue);
            }
        });

        // Create the alert dialog
        AlertDialog dialog = builder.create();

        // Finally, display the alert dialog
        dialog.show();
    }

    private void eventInfoWindow(Marker marker){

    }
}
