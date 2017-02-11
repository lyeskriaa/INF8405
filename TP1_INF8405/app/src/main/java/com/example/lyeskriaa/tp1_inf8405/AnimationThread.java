package com.example.lyeskriaa.tp1_inf8405;

import java.util.TimerTask;

/**
 * Created by LyesKriaa on 17-02-11.
 */

public class AnimationThread extends TimerTask {

    public Jeu jeu;

    public AnimationThread() {
    }

    public AnimationThread(Jeu jeu) {
        this.jeu = jeu;
    }

    @Override
    public void run() {
        jeu.animer();
    }

}
