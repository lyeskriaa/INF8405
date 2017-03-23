package com.inf8405.tp2_inf8405.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.inf8405.tp2_inf8405.R;
import com.inf8405.tp2_inf8405.dao.GroupDao;
import com.inf8405.tp2_inf8405.dao.ProfileDao;
import com.inf8405.tp2_inf8405.services.LocationService;

public class PreferencesActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView textView;
    private Button quitGroupButton;
    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textView = (TextView) findViewById(R.id.progressText);
        quitGroupButton = (Button) findViewById(R.id.quitGroupBtn);
        if(ProfileDao.getInstance().getCurrentUser().isOrganisateur()) quitGroupButton.setVisibility(View.GONE);

        seekBar.setProgress(LocationService.getLocationInterval()/60000);
        seekBar.setMax(30);
        textView.setText(seekBar.getProgress() + "/" + seekBar.getMax()+ "min");
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                        progress = progresValue;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // Display the value in textview
                        textView.setText(progress + "/" + seekBar.getMax()+"min");
                    }
                });

        quitGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PreferencesActivity.this);
                builder.setTitle("Voulez-vous vraiment quitter le groupe ?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO: 17-03-23  GROUP DAO remove child
                        GroupDao.getInstance().removeGroupChild();
                        Intent locationService = new Intent(PreferencesActivity.this, LocationService.class);
                        stopService(locationService);
                        Intent intent = new Intent( PreferencesActivity.this , MainActivity.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        startActivity( intent );
                    }
                });
                builder.setNegativeButton("Non", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // todo sauvegarder cette valeur en local
        LocationService.setLocationInterval(progress * 60000);
    }
}
