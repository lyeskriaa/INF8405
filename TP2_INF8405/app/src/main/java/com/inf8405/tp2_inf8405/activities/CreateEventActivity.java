package com.inf8405.tp2_inf8405.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.widget.EditText;

import java.util.List;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


import com.inf8405.tp2_inf8405.model.Group;
import com.inf8405.tp2_inf8405.model.Lieu;
import com.inf8405.tp2_inf8405.model.Event;

import com.inf8405.tp2_inf8405.R;

public class CreateEventActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        List<Lieu> locations = Group.getGroup().getLocList();
        if (locations.size() == 3){
            Lieu current = locations.get(0);
            Button button1 = (Button) findViewById(R.id.event_choix1);
            button1.setText(current.getName() + " " + Float.valueOf(current.getVotes()).toString() +"/5");
            button1.setOnClickListener(new CreateWithLocation(current));

            current = locations.get(1);
            Button button2 = (Button) findViewById(R.id.event_choix2);
            button2.setText(current.getName() + " " + Float.valueOf(current.getVotes()).toString() +"/5");
            button2.setOnClickListener(new CreateWithLocation(current));

            current = locations.get(2);
            Button button3 = (Button) findViewById(R.id.event_choix3);
            button3.setText(current.getName() + " " + Float.valueOf(current.getVotes()).toString() +"/5");
            button3.setOnClickListener(new CreateWithLocation(current));
        }


    }

    public class CreateWithLocation implements View.OnClickListener {

        Lieu location;

        public CreateWithLocation(Lieu lieu){
            location = lieu;
        }

        public void onClick(View v) {
            EditText dateStartEF = (EditText) findViewById(R.id.start_time_create_edit);
            String dateStart = dateStartEF.getText().toString();
            EditText dateEndEF = (EditText) findViewById(R.id.end_time_create_edit);
            String dateEnd = dateEndEF.getText().toString();
            EditText nameEF = (EditText) findViewById(R.id.event_desc_edit);
            String name = nameEF.getText().toString();
            if (name == null || name.equals("") ||
                    dateStart == null || dateStart.equals("") ||
                    dateEnd == null || dateEnd.equals("")) {
                //todo error message
                return;
            }

            try {
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date result = df.parse(dateStart);
                result = df.parse(dateEnd);
            } catch (Exception e){
                // TODO error message
                return;
            }
            // todo EVENT DAO + delete lieu (APRES la création de l'event
            Group.getGroup().setEvent(new Event(location.getCoordinate(), name, location.getPicture(), dateStart, dateEnd));
        }
    }

}
