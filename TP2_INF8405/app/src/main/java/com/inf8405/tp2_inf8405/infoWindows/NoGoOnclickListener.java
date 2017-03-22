package com.inf8405.tp2_inf8405.infoWindows;

import android.view.View;

import com.inf8405.tp2_inf8405.dao.EventDao;
import com.inf8405.tp2_inf8405.dao.ProfileDao;
import com.inf8405.tp2_inf8405.model.Group;

/**
 * Created by Louise on 2017-03-18.
 */

public class NoGoOnclickListener implements View.OnClickListener {
    public void onClick(View v) {
        EventDao.getInstance().updateNotGoing(Group.getGroup().getEvent().getEventName(), ProfileDao.getInstance().getCurrentUser().getUsername());
    }
}
