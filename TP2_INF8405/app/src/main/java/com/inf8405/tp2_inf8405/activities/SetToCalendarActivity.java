package com.inf8405.tp2_inf8405.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.inf8405.tp2_inf8405.R;
import com.inf8405.tp2_inf8405.infoWindows.GoOnclickListener;
import com.inf8405.tp2_inf8405.infoWindows.MaybeOnClickListener;
import com.inf8405.tp2_inf8405.infoWindows.NoGoOnclickListener;
import com.inf8405.tp2_inf8405.model.Group;

public class SetToCalendarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_to_calendar);

        TextView text = (TextView) findViewById(R.id.cal_event_text);
        text.setText(Group.getGroup().getEvent().getEventName());

        Button go = (Button) findViewById(R.id.cal_go_button);
        go.setOnClickListener(new GoOnclickListener(this));
        Button maybe = (Button) findViewById(R.id.cal_maybe_button);
        maybe.setOnClickListener(new MaybeOnClickListener(this));
        Button no = (Button) findViewById(R.id.cal_no_button);
        no.setOnClickListener(new NoGoOnclickListener());

    }

    @Override
    protected void onResume() {
        super.onResume();
        //finish();
    }
}
