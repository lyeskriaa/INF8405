package com.example.lyeskriaa.tp1_inf8405;

import android.support.annotation.Nullable;

import java.util.Random;

/**
 * La logique de jeu. Fonction et data permettant de jouer le jeu.
 * Created by Louise on 2017-01-24.
 */

public class Logique {
    public final static int ROUGE = 1;
    public final static int BLEU = 2;
    public final static int VERT = 3;
    public final static int ORANGE = 4;
    public final static int JAUNE = 5;
    public final static int VIOLET = 6;
    public final static int N_ROUGE = -1;
    public final static int N_BLEU = -2;
    public final static int N_VERT = -3;
    public final static int N_ORANGE = -4;
    public final static int N_JAUNE = -5;
    public final static int N_VIOLET = -6;
    public final static int NB_COULEUR = 6;

    public final static int HAUT = 10;
    public final static int BAS = 11;
    public final static int GAUCHE = 12;
    public final static int DROITE = 13;

    private int idNiveau, nbCoup, pointVoulu, nbColone, nbRange, coupRestant, points, chaine;
    private int grille[][];
    private Random rand = new Random();
    private boolean pret;

    @Nullable
    static public Logique creerPartie(int idNiveau) {
        if (idNiveau == 1) return new Logique(idNiveau,  6,  800, 5, 8);
        if (idNiveau == 2) return new Logique(idNiveau, 10, 1200, 6, 8);
        if (idNiveau == 3) return new Logique(idNiveau, 10, 1400, 7, 7);
        if (idNiveau == 4) return new Logique(idNiveau, 10, 1800, 8, 7);
        return null;
    }

    public boolean bougerPiece(int range, int colone, int direction){
        if (coupRestant <= 0 || !testCoupValide(range, colone, direction))
            return false;

        coupRestant--;
        pret = true;
        return true;

    }

    public boolean prochaineReaction(){
        if (pret = false) return false;

        boolean fall = false;
        for (int i = 0; i < nbRange; ++i){
            for (int j = 0; j < nbColone; ++j) {
                if (this.grille[i][j] == -1){
                    fall = true;
                    for (int k = i; i > 0; --i) {
                        grille[k][j] = grille[k - 1][j];
                    }
                    grille[0][j] = couleurAleatoire();
                }
            }
        }

        if (fall = true) return true;

        int tempPoint = this.points;
        for (int i = 0; i < nbRange; ++i){
            for (int j = 0; j < nbColone; ++j){
                if (this.grille[i][j] > 0
                        && i < nbRange - 2
                        && (Math.abs(this.grille[i][j]) == Math.abs(this.grille[i + 1][j])
                        && Math.abs(this.grille[i][j]) == Math.abs(this.grille[i + 2][j])) ) {
                    int couleur = Math.abs(this.grille[i][j]);
                    this.grille[i][j] = 0 - couleur;
                    this.grille[i + 1][j] = 0 - couleur;
                    this.grille[i + 2][j] = 0 - couleur;
                    this.points += 100 * chaine;
                    for (int k = i + 3; i < nbRange; ++i) {
                        if (Math.abs(this.grille[i][j]) == couleur) {
                            this.grille[k][j] = -1;
                            if (k <= i + 4) this.points += 100 * chaine;
                        }
                        else break;
                    }
                }
                if (this.grille[i][j] > 0
                        && j < nbColone
                        && (Math.abs(this.grille[i][j]) == Math.abs(this.grille[i][j + 1])
                        && Math.abs(this.grille[i][j]) == Math.abs(this.grille[i][j + 2])) ){
                    int couleur = Math.abs(this.grille[i][j]);
                    this.grille[i][j] = 0 - couleur;
                    this.grille[i][j + 1] = 0 - couleur;
                    this.grille[i][j + 2] = 0 - couleur;
                    this.points += 100 * chaine;
                    for (int k = j + 3; i < nbRange; ++i) {
                        if (Math.abs(this.grille[i][j]) == couleur) {
                            this.grille[i][k] = 0 - couleur;
                            if (k <= j + 4) this.points += 100 * chaine;
                        }
                        else break;
                    }
                }

            }
        }

        if (tempPoint == this.points){
            chaine = 1;
            pret = false;
            return false;
        }
        else{
            chaine++;
            pret = true;
            return true;
        }

    }

    private boolean testCoupValide(int range, int colone, int direction){
        if (direction == HAUT){
            if (range == 0) return false;
            int temp0 = this.grille[range][colone];
            int temp1 = this.grille[range - 1][colone];
            this.grille[range][colone] = temp1;
            this.grille[range - 1][colone] = temp0;
            if (testmatch()){
                return true;
            }
            else {
                this.grille[range][colone] = temp0;
                this.grille[range - 1][colone] = temp1;
                return false;
            }
        }

        if (direction == BAS){
            if (range == nbRange - 1) return false;
            int temp0 = this.grille[range][colone];
            int temp1 = this.grille[range + 1][colone];
            this.grille[range][colone] = temp1;
            this.grille[range + 1][colone] = temp0;
            if (testmatch()){
                return true;
            }
            else {
                this.grille[range][colone] = temp0;
                this.grille[range + 1][colone] = temp1;
                return false;
            }
        }

        if (direction == GAUCHE){
            if (range == 0) return false;
            int temp0 = this.grille[range][colone];
            int temp1 = this.grille[range][colone - 1];
            this.grille[range][colone] = temp1;
            this.grille[range][colone - 1] = temp0;
            if (testmatch()){
                return true;
            }
            else {
                this.grille[range][colone] = temp0;
                this.grille[range][colone - 1] = temp1;
                return false;
            }
        }

        if (direction == DROITE){
            if (range == nbColone - 1) return false;
            int temp0 = this.grille[range][colone];
            int temp1 = this.grille[range][colone + 1];
            this.grille[range][colone] = temp1;
            this.grille[range][colone + 1] = temp0;
            if (testmatch()){
                return true;
            }
            else {
                this.grille[range][colone] = temp0;
                this.grille[range][colone + 1] = temp1;
                return false;
            }
        }

        return false;
    }

    private boolean testmatch(){
        for (int i = 0; i < nbRange; ++i){
            for (int j = 0; j < nbColone; ++j){
                if (i < nbRange - 2 && (this.grille[i][j] == this.grille[i + 1][j] && this.grille[i][j] == this.grille[i + 2][j]) )
                        return true;
                if (j < nbColone && (this.grille[i][j] == this.grille[i][j + 1] && this.grille[i][j] == this.grille[i][j + 2]) )
                        return true;
            }
        }
        return false;
    }

    private Logique(int idNiveau, int nbCoup, int pointVoulu, int nbRange, int nbColone){
        this.idNiveau = idNiveau;
        this.nbCoup = nbCoup;
        this.pointVoulu = pointVoulu;
        this.nbRange = nbRange;
        this.nbColone = nbColone;
        this.coupRestant = nbCoup;
        this.points = 0;
        this.chaine = 1;
        this.pret = false;

        genererGrille();
    }

    private void genererGrille(){
        this.grille = new int[nbRange][];
        for (int i = 0; i < nbRange; ++i){
            this.grille[i] = new int[nbColone];
            for (int j = 0; j < nbColone; ++j){
                this.grille[i][j] = couleurAleatoire();
            }
        }
    }

    private int couleurAleatoire(){
        return this.rand.nextInt(NB_COULEUR) + 1;
    }

    public int[][] getGrille(){
        return grille;
    }
    public int getCouleur(int range, int colone){
        return grille[range][colone];
    }
    public boolean pariteFinie(){
        return coupRestant > 0;
    }
    public int getIdNiveau(){
        return idNiveau;
    }
    public int getNbCoup(){
        return nbCoup;
    }
    public int getPointVoulu(){
        return pointVoulu;
    }
    public int getNbColone(){
        return nbColone;
    }
    public int getNbRange(){
        return nbRange;
    }
    public int getCoupRestant(){
        return coupRestant;
    }
    public int getPoints(){
        return points;
    }
}
