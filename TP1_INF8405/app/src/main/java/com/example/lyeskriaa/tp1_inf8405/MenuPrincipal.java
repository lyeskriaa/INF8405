package com.example.lyeskriaa.tp1_inf8405;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MenuPrincipal extends AppCompatActivity {

    private Button demarrer_btn;
    private Button reglages_btn;
    private Button quitter_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        demarrer_btn = (Button) findViewById(R.id.demarrerBtn);
        reglages_btn = (Button) findViewById(R.id.reglagesBtn);
        quitter_btn = (Button) findViewById(R.id.quitterBtn);

        demarrer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNiveaux();
            }
        });

        reglages_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showReglages();
            }
        });

        quitter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitterJeu();
            }
        });
    }

    private void showNiveaux() {
        // plus utilisé pour passer d une activité à une autre
        // en presisant l'intant de départ et celui où on va

        Intent intent = new Intent(this, Niveaux.class);
        startActivity(intent);
    }


    private void quitterJeu() {
        finish();

    }


}

