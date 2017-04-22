package com.inf8405.projet_inf8405.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inf8405.projet_inf8405.R;
import com.inf8405.projet_inf8405.model.User;
import com.inf8405.projet_inf8405.utils.ChatUtils;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    List<TextView> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        history =  new ArrayList<TextView>();
        history.add((TextView) findViewById(R.id.history0));
        history.add((TextView) findViewById(R.id.history1));
        history.add((TextView) findViewById(R.id.history2));
        history.add((TextView) findViewById(R.id.history3));
        history.add((TextView) findViewById(R.id.history4));
        history.add((TextView) findViewById(R.id.history5));
        history.add((TextView) findViewById(R.id.history6));
        history.add((TextView) findViewById(R.id.history7));
        history.add((TextView) findViewById(R.id.history8));
        history.add((TextView) findViewById(R.id.history9));
        history.add((TextView) findViewById(R.id.history10));
        history.add((TextView) findViewById(R.id.history11));
        history.add((TextView) findViewById(R.id.history12));
        history.add((TextView) findViewById(R.id.history13));
        history.add((TextView) findViewById(R.id.history14));

        Button automessage = (Button) findViewById(R.id.auto_message);
        automessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText mEdit   = (EditText)findViewById(R.id.msg_input);
                mEdit.setText(ChatUtils.randomMessage(null, null));
            }
        });

        Button sendMessage = (Button) findViewById(R.id.send_message);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText mEdit   = (EditText)findViewById(R.id.msg_input);
                String message = mEdit.getText().toString();
                // TODO send message to database
            }
        });
    }

    public void refresh(){
        List<User> users = new ArrayList<User>(); // TODO find real message
        List<String> messages = new ArrayList<String>(); // TODO find real message

        for (int i = 0; i < 15; ++i){
            String message = messages.get(i);
            if (message == null) history.get(i).setVisibility(View.GONE);
            else history.get(i).setText(users.get(i).getUsername() + " a dit : " + message);
        }
    }
}
