package com.inf8405.tp2_inf8405.infoWindows;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

import com.inf8405.tp2_inf8405.dao.LieuDao;
import com.inf8405.tp2_inf8405.model.Group;
import com.inf8405.tp2_inf8405.model.Lieu;

import java.util.Arrays;

/**
 * Created by Louise on 2017-03-18.
 */

public class VoteOnclickListener implements View.OnClickListener {
    Context context;
    Lieu location;
    int voteValue = 0;

    public VoteOnclickListener(Context context, String locationName) {
        this.context = context;
        location = Group.getGroup().findLocation(locationName);
    }

    public void onClick(View v) {
        if (location == null) return; // nothing we can do
        else if(location.getMyVote() != 0) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Erreur")
                    .setMessage("Vous avez déjà voté pour ce lieu!")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }
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
                Log.d("ON CLICK OK", "========== Vote OK ======== ");
                location.vote(voteValue);
                LieuDao.getInstance().saveVote(voteValue, location);
            }
        });

        // Create the alert dialog
        AlertDialog dialog = builder.create();

        // Finally, display the alert dialog
        dialog.show();
    }

}

