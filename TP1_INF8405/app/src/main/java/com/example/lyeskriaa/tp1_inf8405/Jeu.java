package com.example.lyeskriaa.tp1_inf8405;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class Jeu extends AppCompatActivity {

    Logique log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);
        Intent mIntent = getIntent();
        log = Logique.creerPartie(mIntent.getIntExtra("niveau", -1));

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
    }
}
