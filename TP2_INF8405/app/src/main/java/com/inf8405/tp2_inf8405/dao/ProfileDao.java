package com.inf8405.tp2_inf8405.dao;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by LyesKriaa on 17-03-07.
 */

@IgnoreExtraProperties
public class ProfileDao {

    private DatabaseReference db;
    private Context context;

    private RencontreDao rencontreDao;


    public ProfileDao(Context c) {
        context = c;
        db = FirebaseDatabase.getInstance().getReference();
    }


    public ProfileDao open() {
        rencontreDao = new RencontreDao(context);
        return this;
    }

    public boolean isEmpty(){
       return true;
    }
}
