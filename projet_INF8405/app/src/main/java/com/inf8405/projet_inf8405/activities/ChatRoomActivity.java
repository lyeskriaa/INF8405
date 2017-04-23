package com.inf8405.projet_inf8405.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.inf8405.projet_inf8405.R;
import com.inf8405.projet_inf8405.fireBaseHelper.ChatDBHelper;
import com.inf8405.projet_inf8405.fireBaseHelper.UserDBHelper;
import com.inf8405.projet_inf8405.model.Chat;
import com.inf8405.projet_inf8405.model.ListeUsers;
import com.inf8405.projet_inf8405.model.Message;
import com.inf8405.projet_inf8405.model.User;
import com.inf8405.projet_inf8405.utils.ChatUtils;
import com.inf8405.projet_inf8405.utils.Enum;
import com.inf8405.projet_inf8405.utils.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by NP on 4/17/2017.
 */

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_send_msg;
    private Button automessage;
    private Button find;
    private EditText input_msg;
    private TextView chat_conversation;

    private String user_name,user_id;
    private DatabaseReference root;
    private String chatID;
    private String temp_key;
    private String chat_msg;
    private String chat_user_name;
    private User currentUser;
    private boolean conversation_exists = false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        btn_send_msg = (Button) findViewById(R.id.btn_send);
        automessage = (Button) findViewById(R.id.auto_message);
        find = (Button) findViewById(R.id.find_interlocuteur);

        input_msg = (EditText) findViewById(R.id.msg_input);
        chat_conversation = (TextView) findViewById(R.id.textView);

        // informations sur l interlocuteur  id + username
        user_name = getIntent().getExtras().get("user_name").toString();
        user_id = getIntent().getExtras().get("user_id").toString();
        setTitle(user_name);
        currentUser = UserDBHelper.getInstance().getCurrentUser();

        btn_send_msg.setOnClickListener(this);
        automessage.setOnClickListener(this);
        find.setOnClickListener(this);

        verifierConversation();

        root =  FirebaseDatabase.getInstance().getReference().child(Enum.CHATS.toString());
        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                appendChatConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                appendChatConversation(dataSnapshot);

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

    private void verifierConversation() {

        Query query = FirebaseDatabase.getInstance().getReference().child(Enum.CHATS.toString()).push().getRef();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("CHAT ACTIVITY ", " DATA CHANGE: " + snapshot.getKey());
                if (snapshot.hasChildren()) {
                    Log.e("HAS CHILDREN ", " DATA CHANGE: " + snapshot.getKey());
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Log.e("CHAT ACTIVITY ", " child : " + child.getKey());
                        if(child.child("user1").getValue().toString().equals(user_id) && child.child("user2").getValue().toString().equals(currentUser.getId())
                                || child.child("user1").getValue().toString().equals(currentUser.getId()) && child.child("user2").getValue().toString().equals(user_id)) {

                            conversation_exists = true;
                            chatID = child.getKey();

                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // nothing here
            }
        });
    }

    private void appendChatConversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()){

            chat_msg = (String)  ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String)  ((DataSnapshot)i.next()).getValue();
            chat_conversation.append(chat_user_name +":"+chat_msg+"\n");

        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn_send_msg) {
            if(conversation_exists) {
                ChatDBHelper.getInstance().addMessage(chatID, currentUser.getUsername(), input_msg.getText().toString());
            }
            else {
                String message = input_msg.getText().toString();
                Message msg = new Message(currentUser.getId(), message);
                List listHistory = new ArrayList<Message>();
                listHistory.add(msg);
                String chatid = FirebaseDatabase.getInstance().getReference().child(Enum.CHATS.toString()).push().getKey().toString();
                Chat chat = new Chat(chatid, currentUser.getId(), user_id, listHistory);
                ChatDBHelper.getInstance().addChatChild(chat);
            }
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

        if (v == automessage) {
            input_msg.setText(ChatUtils.randomMessage(UserDBHelper.getInstance().getCurrentUser(), ListeUsers.getInstance().findUserById(user_id)));
        }

        if (v == find) {
            Path.setDestination(user_id);
            MapsActivity.getMapsActivity().refresh();
            finish();
        }
    }
}

