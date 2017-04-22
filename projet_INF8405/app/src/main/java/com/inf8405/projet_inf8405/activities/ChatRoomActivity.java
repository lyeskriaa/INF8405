package com.inf8405.projet_inf8405.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inf8405.projet_inf8405.R;
import com.inf8405.projet_inf8405.fireBaseHelper.UserDBHelper;
import com.inf8405.projet_inf8405.model.ListeUsers;
import com.inf8405.projet_inf8405.utils.ChatUtils;
import com.inf8405.projet_inf8405.utils.Path;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by NP on 4/17/2017.
 */

public class ChatRoomActivity extends AppCompatActivity{

    private Button btn_send_msg;
    private Button automessage;
    private Button find;
    private EditText input_msg;
    private TextView chat_conversation;

    private String user_name,user_id;
    private DatabaseReference root;
    private String temp_key;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        btn_send_msg = (Button) findViewById(R.id.btn_send);
        input_msg = (EditText) findViewById(R.id.msg_input);
        chat_conversation = (TextView) findViewById(R.id.textView);

        user_name = getIntent().getExtras().get("user_name").toString();
        user_id = getIntent().getExtras().get("user_id").toString();
        setTitle(user_name);

        root = FirebaseDatabase.getInstance().getReference().child(user_id);

        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference massage_root = root.child(temp_key);
                Map< String, Object> map2 = new HashMap<String,Object>();
                map2.put("name",user_name);
                map2.put("msg",input_msg.getText().toString());

                massage_root.updateChildren(map2);
                //input_msg.setText("");

            }
        });
        automessage = (Button) findViewById(R.id.auto_message);
        automessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                input_msg.setText(ChatUtils.randomMessage(UserDBHelper.getInstance().getCurrentUser(), ListeUsers.getInstance().findUserById(user_id)));
            }
        });
        find = (Button) findViewById(R.id.find_interlocuteur);
        automessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Path.setDestination(user_id);
                finish();
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String chat_msg,chat_user_name;

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            chat_msg = (String)  ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String)  ((DataSnapshot)i.next()).getValue();

            chat_conversation.append(chat_user_name +":"+chat_msg+"\n");

        }
    }
}

