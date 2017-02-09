package com.example.lyeskriaa.tp1_inf8405;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class Jeu extends AppCompatActivity {
    @ColorInt public static final int PURPLE       = 0xFFFFA500;
    @ColorInt public static final int ORANGE      = 0xFF800080;
    @ColorInt public static final int GREEN      = 0xFF44DF06;
    @ColorInt public static final int YELLOW      = 0xFFF4F405;

    Logique log;
    GridLayout gridLayout;
    GridLayout gridJeu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);
        Intent mIntent = getIntent();
        log = Logique.creerPartie(mIntent.getIntExtra("NiveauId",-1));
        gridLayout = (GridLayout) findViewById(R.id.gridLayoutJeu);
        dessinerGrilleJeu(log.getGrille(), gridLayout);
       // gridLayout.addView(gridJeu);
        Toast.makeText(Jeu.this,log.getGrille().length + " / " + log.getGrille()[0].length, Toast.LENGTH_LONG).show();
    }

    private void dessinerGrilleJeu(int[][] grilleLogique, GridLayout gridLayout){
       // GridLayout grilleJeu = new GridLayout(this);

        gridLayout.removeAllViews();

        int column = grilleLogique[0].length;
        int row = grilleLogique.length;
        int total = column * row;

        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row);
        for(int i =0, c = 0, r = 0; i < total; i++, c++)
        {
            if(c == column)
            {
                c = 0;
                r++;
            }
            ImageView oImageView = new ImageView(this);
            Bitmap tempBitmap = Bitmap.createBitmap(110,110,Bitmap.Config.ARGB_8888);
            tempBitmap = tempBitmap.copy(tempBitmap.getConfig(), true);
            Canvas canvas = new Canvas(tempBitmap);
            canvas.drawBitmap(tempBitmap,0,0,null);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(verifierCouleur(grilleLogique[r][c]));

            canvas.drawCircle(c+50,r+50,50,paint);

            oImageView.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));

            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(c);
            param.rowSpec = GridLayout.spec(r);

            oImageView.setLayoutParams (param);

            gridLayout.addView(oImageView);

        }
    }

    private int verifierCouleur(int i) {
        int couleur = Color.BLACK;
        switch (i) {
            case Logique.ROUGE:
                couleur = Color.RED;
                break;
            case Logique.BLEU:
                couleur = Color.BLUE;
                break;
            case Logique.VERT:
                couleur = GREEN;
                break;
            case Logique.N_ORANGE:
                couleur = ORANGE;
                break;
            case Logique.JAUNE:
                couleur = YELLOW;
                break;
            case Logique.VIOLET:
                couleur = PURPLE;
                break;
        }


        return couleur;

    }
}
