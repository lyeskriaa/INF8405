package com.inf8405.tp2_inf8405.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by LyesKriaa on 17-03-07.
 */

public class ProfileDao {

    private SQLiteDatabase db;
//    private MeetingDBHelper meetingdbHelper;
    private Context context;


    public ProfileDao(Context c) {
        context = c;
    }


}
