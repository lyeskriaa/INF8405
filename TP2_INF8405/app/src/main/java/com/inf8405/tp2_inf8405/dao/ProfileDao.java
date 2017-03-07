package com.inf8405.tp2_inf8405.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.firebase.provider.FirebaseInitProvider;

/**
 * Created by LyesKriaa on 17-03-07.
 */

public class ProfileDao {

    private FirebaseDatabase db;
    private Context context;


    public ProfileDao(Context c) {
        context = c;
    }


}
