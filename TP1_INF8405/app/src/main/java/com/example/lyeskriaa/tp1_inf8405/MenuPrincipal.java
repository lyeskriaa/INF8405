package com.example.lyeskriaa.tp1_inf8405;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Cette classe représente l'activité principale
 * elle controle le menu principal
 */
public class MenuPrincipal extends AppCompatActivity {

    private Button demarrer_btn;
    private Button reglages_btn;
    private Button quitter_btn;

    /**
     * Cette methode permet de récupérer les boutons du menu principal
     * dans la page xml "activity_menu_principal" et place des listener
     * à l'appuis de l'un de ces boutons.
     * @param savedInstanceState
     */
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
                showRegles();
            }
        });

        quitter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitterJeu();
            }
        });
    }

    /**
     * Cette methode fait la transition vers la grille
     * qui présente les différents niveaux du jeu
     */
    private void showNiveaux() {
        // utilisé pour passer d une activité à une autre
        // en presisant l'intant de départ et celui où on va
        Intent intent = new Intent(this, Niveaux.class);
        startActivity(intent);
    }

    /**
     * Cette methode fait la transition vers la page qui présente
     * les règles du jeu.
     */
    private void showRegles() {
        Intent intent = new Intent(this, ReglesJeu.class);
        startActivity(intent);
    }

    /**
     * Cette methode permet de quitter le jeu.
     */
    private void quitterJeu() {
        finish();

    }


}

