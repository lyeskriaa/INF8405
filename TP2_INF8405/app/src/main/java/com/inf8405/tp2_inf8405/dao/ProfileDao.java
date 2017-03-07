package com.inf8405.tp2_inf8405.dao;

import android.content.Context;
import android.database.Cursor;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import com.google.firebase.provider.FirebaseInitProvider;

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
        open();
        String query = "Select count(*) FROM "+ RencontreDao.DB_TABLE_PROFILES;
        Cursor c = db.rawQuery(query, new String[]{});
        c.moveToFirst();
        int count = c.getInt(0);
        return count==0;
    }
}
