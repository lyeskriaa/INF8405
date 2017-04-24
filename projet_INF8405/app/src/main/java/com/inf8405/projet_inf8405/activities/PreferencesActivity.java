package com.inf8405.projet_inf8405.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.inf8405.projet_inf8405.R;
import com.inf8405.projet_inf8405.services.LocationService;

public class PreferencesActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView textView;

    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textView = (TextView) findViewById(R.id.progressText);



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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // todo sauvegarder cette valeur en local
        LocationService.setLocationInterval(progress);
    }
}
