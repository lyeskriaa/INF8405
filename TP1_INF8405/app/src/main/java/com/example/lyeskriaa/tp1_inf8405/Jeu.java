package com.example.lyeskriaa.tp1_inf8405;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Jeu extends AppCompatActivity {
    @ColorInt public static final int PURPLE       = 0xFF660066;
    @ColorInt public static final int ORANGE      = 0xFFFFAE00;
    @ColorInt public static final int GREEN      = 0xFF44DF06;
    @ColorInt public static final int YELLOW      = 0xFFF4F405;

    public static Logique log;
    private GridLayout gridJeu;
    private float x_start, x_end, y_start, y_end;
    private static final int MIN_SWIPE = 70;
    public boolean BLOCK_SWIPE = false;
    Button boutonReinit;

    public TextView score;
    public TextView coupsRestants;
    public TextView niveauJeu;
    public TextView but;
    public final String SCORE_INIT="Score : ";
    public final String NIVEAU_INIT="Niveau : ";
    public final String BUT_INIT="But : ";
    public final String COUPS_RESTANTS_INIT="Coups restants : ";

    public int counterLog =0;
    public int counter =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);
        Intent mIntent = getIntent();
        log = Logique.creerPartie(mIntent.getIntExtra("NiveauId",-1));

        // recuperer les champs
        score = (TextView) findViewById(R.id.scoreField);
        coupsRestants = (TextView) findViewById(R.id.movesField);
        niveauJeu     = (TextView) findViewById(R.id.levelIdField);
        but = (TextView) findViewById(R.id.targetField);
        boutonReinit = (Button) findViewById(R.id.reinitialiserBtn);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                animer();
                handler.postDelayed(this, 500);  //for interval...
            }
        };
        handler.postDelayed(runnable, 500); //for initial delay..

        boutonReinit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                log = log.creerPartie(log.getIdNiveau());
                                init();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Jeu.this);
                builder.setMessage("Etes-vous sùr de reinitialiser la partie ?").setPositiveButton("Oui", dialogClickListener)
                        .setNegativeButton("Non", dialogClickListener).show();

            }
        });

        gridJeu = (GridLayout) findViewById(R.id.gridLayoutJeu);

        init();

    }

    private void init(){


        // initialiser les textView
        score.setText(SCORE_INIT+"0");
        but.setText(BUT_INIT+log.getPointVoulu());
        niveauJeu.setText(NIVEAU_INIT+log.getIdNiveau());
        coupsRestants.setText(COUPS_RESTANTS_INIT+log.getCoupRestant());

        dessinerGrilleJeu(log.getGrille(), gridJeu);
    }

    private void dessinerGrilleJeu(int[][] grilleLogique, GridLayout gridJeu){

        gridJeu.removeAllViews();

        final int column = grilleLogique[0].length;
        final int row = grilleLogique.length;
        int total = column * row;

        gridJeu.setColumnCount(column);
        gridJeu.setRowCount(row);
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
                    if(!BLOCK_SWIPE){
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
                                    int colId = oImageView.getId() % column ;
                                    int rowId = oImageView.getId() / column ;

                                    if(deltaX > 0 && (Math.abs(deltaX) > Math.abs(deltaY)*2)){
                                        dir = Logique.GAUCHE;
                                        Toast.makeText(Jeu.this,"Gauche "+rowId+", "+colId, Toast.LENGTH_SHORT).show();
                                    }

                                    if(deltaX < 0 && (Math.abs(deltaX) > Math.abs(deltaY)*2)){
                                        dir = Logique.DROITE;
                                        Toast.makeText(Jeu.this,"Droite "+rowId+", "+colId, Toast.LENGTH_SHORT).show();
                                    }

                                    if(deltaY > 0 && (Math.abs(deltaY) > Math.abs(deltaX)*2)){
                                        dir = Logique.HAUT;
                                        Toast.makeText(Jeu.this,"Haut "+rowId+", "+colId, Toast.LENGTH_SHORT).show();
                                    }

                                    if(deltaY < 0 && (Math.abs(deltaY) > Math.abs(deltaX)*2)){
                                        dir = Logique.BAS;
                                        Toast.makeText(Jeu.this,"Bas "+rowId+", "+colId, Toast.LENGTH_SHORT).show();
                                    }

                                    log.bougerPiece(rowId,colId,dir);
                                    BLOCK_SWIPE = true;
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
                    return false;
                }
            });

            gridJeu.addView(oImageView);

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

    public void animer() {
        if(BLOCK_SWIPE){
            //Log.v("Jeu Block Swipe", " "+counterLog++ );
            if(log.prochaineReaction()){
                //Log.v("prochaineReaction", " "+counter++ );
                score.setText(SCORE_INIT+log.getPoints());
                coupsRestants.setText(COUPS_RESTANTS_INIT+log.getCoupRestant());
                dessinerGrilleJeu(log.getGrille(),gridJeu);
            }
            else {
                BLOCK_SWIPE = false;
                if(log.getPoints() >= log.getPointVoulu()){
                    if(log.getIdNiveau() < 4) {
                        Niveaux.grilleNiveaux.set(log.getIdNiveau(), true);

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        log = log.creerPartie(log.getIdNiveau() + 1);
                                        init();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        finish();
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Gagné !! Passer au prochain niveau ?").setPositiveButton("Oui", dialogClickListener)
                                .setNegativeButton("Non", dialogClickListener).show();
                    }
                    else {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Ok
                                        finish();
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Bravo !! Vous avez finis tous les niveaux.").setPositiveButton("Ok", dialogClickListener)
                                .show();
                    }

                }
                else if(log.getCoupRestant()==0){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    log = log.creerPartie(log.getIdNiveau());
                                    init();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    finish();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Perdu !! Rejouer la partie ?").setPositiveButton("Oui", dialogClickListener)
                            .setNegativeButton("Non", dialogClickListener).show();
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Fin de la partie")
                .setMessage("Etes-vous sùr de vouloir quitter la partie?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("Non", null)
                .show();
    }
}
