package com.inf8405.tp2_inf8405.infoWindows;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.view.View;

import com.inf8405.tp2_inf8405.dao.EventDao;
import com.inf8405.tp2_inf8405.dao.ProfileDao;
import com.inf8405.tp2_inf8405.model.Event;
import com.inf8405.tp2_inf8405.model.Group;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Louise on 2017-03-19.
 */

public class MaybeOnClickListener implements View.OnClickListener {
    private Context context;

    public MaybeOnClickListener(Context context){
        this.context = context;
    }
    public void onClick(View v) {
        Date startTime=new Date(), endTime=new Date();
        String title = "", eventLocation="";
        Event event = Group.getGroup().getEvent();
        if (event == null) return;
        // set maybe to database
        title = event.getEventName();
        try {
            startTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(event.getDateStart());
            endTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(event.getDateEnd());
        }catch (Exception e)
        {
            return;
        }

        EventDao.getInstance().updateMaybe(event.getEventName(), ProfileDao.getInstance().getCurrentUser().getUsername());
        Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, String.valueOf(startTime.getTime()));
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, String.valueOf(endTime.getTime()));
        intent.putExtra(CalendarContract.Events.TITLE, title);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, eventLocation);
        context.startActivity(intent);


    }
}
