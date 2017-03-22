package com.inf8405.tp2_inf8405.infoWindows;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.view.View;

import com.inf8405.tp2_inf8405.dao.EventDao;
import com.inf8405.tp2_inf8405.dao.ProfileDao;
import com.inf8405.tp2_inf8405.model.Event;
import com.inf8405.tp2_inf8405.model.Group;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Louise on 2017-03-18.
 */

public class GoOnclickListener implements View.OnClickListener {
    private Context context;

    public GoOnclickListener(Context context){
        this.context = context;
    }
    public void onClick(View v) {
        Date startTime, endTime;
        String title = "", eventLocation="";
        Event event = Group.getGroup().getEvent();
        if (event == null) return;
        // set going to database
        title = event.getEventName();
        try {
            startTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(event.getDateStart());
            endTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(event.getDateEnd());
        }catch (Exception e)
        {
            return;
        }

        EventDao.getInstance().updateGoing(event.getEventName(), ProfileDao.getInstance().getCurrentUser().getUsername());
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime.getTime());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTime());
        intent.putExtra(Events.TITLE, title);
        intent.putExtra(Events.EVENT_LOCATION, eventLocation);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NO_HISTORY
                | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        context.startActivity(intent);

    }
}
