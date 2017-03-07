package com.example.lyeskriaa.tp1_inf8405;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Cette Classe représente l'activité des règles du jeu
 * et permet de lister les différentes règles.
 * @author Mustapha Yousefi
 */

public class ReglesJeu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regles_jeu);
        TextView tv = (TextView) findViewById(R.id.textView2);
        tv.setPadding(20, 40, 20, 10);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setText("Les règles du jeu :\n" +

            "-\tLe jeu est une grille de jetons (cercles) de couleur aléatoire (rouge, bleu, vert, orange, jaune, violet).\n" +
            "-\tOn peut échanger que deux jetons voisin, que ce soit verticalement ou horizontalement.\n" +
            "-\tLorsque qu’un groupe de 3 ou plus est formé, il doit disparaitre.\n" +
            "-\tUn échange de jeton n’est possible que si un groupe de trois est formé sur la grille.\n" +
            "-\tChaque groupe de jeton disparu donne des points (3 : +100, 4 : +200, 5 : +300).\n" +
            "-\tSi plusieurs alignements sont fait en un coup, cela fait une chaine et multiplie les points donnés par la longueur de la chaine.\n" +
            "-\tIl y a un nombre de coups limité.\n" +
            "-\tIl faut atteindre un certain nombre de points pour gagner la partie.\n" +
            "-\tGagner un niveau débloque le suivant.\n");
    }
}

