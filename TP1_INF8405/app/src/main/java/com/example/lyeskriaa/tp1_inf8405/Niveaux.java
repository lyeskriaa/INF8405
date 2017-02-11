package com.example.lyeskriaa.tp1_inf8405;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class Niveaux extends AppCompatActivity {

    public static ArrayList<Boolean> grilleNiveaux = new ArrayList<Boolean>();
    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_niveaux);

        grilleNiveaux.add(true);
        grilleNiveaux.add(false);
        grilleNiveaux.add(false);
        grilleNiveaux.add(false);

        gridview = (GridView) findViewById(R.id.gridview);
        dessinerNiveaux();


    }

    @Override
    protected void onResume() {
        super.onResume();
        dessinerNiveaux();
    }

    private void dessinerNiveaux(){
        gridview.setAdapter(new ImageAdapter(this,this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Toast.makeText(Niveaux.this, "" + position, Toast.LENGTH_SHORT).show();
                if (grilleNiveaux.get(position)) {
                    showJeu(position+1);
                }
            }
        });
    }

    private void showJeu(int idJeu) {
        Intent intent = new Intent(this, Jeu.class);
        intent.putExtra("NiveauId",idJeu);
        startActivity(intent);
    }

    public ArrayList<Boolean> getGrilleNiveaux() {
        return grilleNiveaux;
    }

    public void setGrilleNiveaux(int index, Boolean nivStatus) {
        this.grilleNiveaux.set(index, nivStatus);
    }

}
