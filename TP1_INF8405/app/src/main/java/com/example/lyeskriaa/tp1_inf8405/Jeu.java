package com.example.lyeskriaa.tp1_inf8405;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;


public class Jeu extends AppCompatActivity {
    @ColorInt public static final int PURPLE       = 0xFF660066;
    @ColorInt public static final int ORANGE      = 0xFFFFAE00;
    @ColorInt public static final int GREEN      = 0xFF44DF06;
    @ColorInt public static final int YELLOW      = 0xFFF4F405;

    public static Logique log;
    private GridLayout gridLayout;
    private GridLayout gridJeu;
    private float x_start, x_end, y_start, y_end;
    private static final int MIN_SWIPE = 140;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);
        Intent mIntent = getIntent();
        log = Logique.creerPartie(mIntent.getIntExtra("NiveauId",-1));
        gridLayout = (GridLayout) findViewById(R.id.gridLayoutJeu);
        dessinerGrilleJeu(log.getGrille(), gridLayout);
       // gridLayout.addView(gridJeu);
       // Toast.makeText(Jeu.this,log.getGrille().length + " / " + log.getGrille()[0].length, Toast.LENGTH_LONG).show();
    }

    private void dessinerGrilleJeu(int[][] grilleLogique, GridLayout gridLayout){
       // GridLayout grilleJeu = new GridLayout(this);

        gridLayout.removeAllViews();

        final int column = grilleLogique[0].length;
        final int row = grilleLogique.length;
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
            final ImageView oImageView = new ImageView(this);
            Bitmap tempBitmap = Bitmap.createBitmap(110,110,Bitmap.Config.ARGB_8888);
            tempBitmap = tempBitmap.copy(tempBitmap.getConfig(), true);
            final Canvas canvas = new Canvas(tempBitmap);
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
            oImageView.setId(i);

            oImageView.setLayoutParams(param);
            oImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = MotionEventCompat.getActionMasked(event);

                    switch(action) {
                        case (MotionEvent.ACTION_DOWN) :
                            x_start = event.getX();
                            y_start = event.getY();
                            return true;

                        case (MotionEvent.ACTION_UP) :

                            x_end = event.getX();
                            y_end = event.getY();
                            float deltaX = x_start - x_end;
                            float deltaY = y_start - y_end;
                            if ((Math.abs(deltaX) > MIN_SWIPE)  || (Math.abs(deltaY) > MIN_SWIPE))
                            {
                                int dir = 0;
                                int x = oImageView.getId() / row ;
                                int y = oImageView.getId() % row ;

                                if(deltaX > 0 && (Math.abs(deltaX) > Math.abs(deltaY)*2))
                                    dir = Logique.DROITE;
                                if(deltaX < 0 && (Math.abs(deltaX) > Math.abs(deltaY)*2))
                                    dir = Logique.GAUCHE;
                                if(deltaY > 0 && (Math.abs(deltaY) > Math.abs(deltaX)*2))
                                    dir = Logique.BAS;
                                if(deltaY < 0 && (Math.abs(deltaY) > Math.abs(deltaX)*2))
                                    dir = Logique.HAUT;

                                log.bougerPiece(x,y,dir);
                                //boolean animating = true;
                                return true;
                            }
                            else
                            {
                                return false;
                            }

                        default :
                            return false;
                    }
                }
            });

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
            case Logique.ORANGE:
                couleur = ORANGE;
                break;
            case Logique.JAUNE:
                couleur = YELLOW;
                break;
            case Logique.VIOLET:
                couleur = PURPLE;
                break;
            default:
                couleur = Color.TRANSPARENT;
                break;
        }


        return couleur;

    }

}
