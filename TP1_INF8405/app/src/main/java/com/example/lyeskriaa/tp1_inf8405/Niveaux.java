package com.example.lyeskriaa.tp1_inf8405;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class Niveaux extends AppCompatActivity {

    public final static int NIV_1 = 1;
    public final static int NIV_2 = 2;
    public final static int NIV_3 = 3;
    public final static int NIV_4 = 4;

    private ArrayList<Boolean> grilleNiveaux = new ArrayList<Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_niveaux);

        grilleNiveaux.add(true);
        grilleNiveaux.add(false);
        grilleNiveaux.add(false);
        grilleNiveaux.add(false);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this,this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(Niveaux.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public ArrayList<Boolean> getGrilleNiveaux() {
        return grilleNiveaux;
    }

    public void setGrilleNiveaux(int index, Boolean nivStatus) {
        this.grilleNiveaux.set(index, nivStatus);
    }

}
