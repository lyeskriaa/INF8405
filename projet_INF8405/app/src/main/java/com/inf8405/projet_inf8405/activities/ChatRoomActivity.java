package com.inf8405.projet_inf8405.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
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
import com.inf8405.projet_inf8405.model.ListeChatsCurrentUser;
import com.inf8405.projet_inf8405.model.ListeUsers;
import com.inf8405.projet_inf8405.model.Message;
import com.inf8405.projet_inf8405.model.User;
import com.inf8405.projet_inf8405.utils.ChatUtils;
import com.inf8405.projet_inf8405.utils.Enum;
import com.inf8405.projet_inf8405.utils.Path;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    private String chatID = "";
    private String temp_key;
    private String chat_msg;
    private String chat_user_name;
    private User currentUser;
    private ScrollView scrollView;
    private boolean conversation_exists = false;
    protected Query query;
    List<Message> listHistory = new ArrayList<Message>();
    List<String> listMessagesID = new ArrayList<String>();
    private static ChatRoomActivity chatRoomActivity = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        btn_send_msg = (Button) findViewById(R.id.btn_send);
        automessage = (Button) findViewById(R.id.auto_message);
        find = (Button) findViewById(R.id.find_interlocuteur);
        scrollView = (ScrollView) findViewById(R.id.scrollView2);
        input_msg = (EditText) findViewById(R.id.msg_input);
        chat_conversation = (TextView) findViewById(R.id.textView);

        // informations sur l interlocuteur  id + username
        user_name = getIntent().getExtras().get("user_name").toString();
        user_id = getIntent().getExtras().get("user_id").toString();
        setTitle(user_name);
        currentUser = UserDBHelper.getInstance().getCurrentUser();

        verifierConversation();

        btn_send_msg.setOnClickListener(this);
        automessage.setOnClickListener(this);
        find.setOnClickListener(this);

        chatRoomActivity= this;

    }

    public static ChatRoomActivity getChatRoomActivity() {
        return chatRoomActivity;
    }

    private void verifierConversation() {

        query = FirebaseDatabase.getInstance().getReference().child(Enum.CHATS.toString()).getRef();
        ValueEventListener listen = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("CHAT ACTIVITY ", " DATA CHANGE: " + snapshot.getKey());
                if (snapshot.hasChildren()) {
                    Log.e("HAS CHILDREN ", " DATA CHANGE: " + snapshot.getKey());
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Log.e("CHAT ACTIVITY verification ", " child : " + child.getKey()+ " UID : "+user_id);
                        if(child.child("user1").getValue().toString().equals(user_id) && child.child("user2").getValue().toString().equals(currentUser.getId())
                                || child.child("user1").getValue().toString().equals(currentUser.getId()) && child.child("user2").getValue().toString().equals(user_id)) {

                            chatID = child.getKey();
                            Log.e("CHAT ACTIVITY ", " exist chatID : " + child.getKey());
                            ChatDBHelper.getInstance().readData(child);
                            fillConversationHistory(ListeChatsCurrentUser.getInstance().findChat(chatID).getMessagesHistory());
                            listHistory = ListeChatsCurrentUser.getInstance().findChat(chatID).getMessagesHistory();
                            for (Message message : listHistory) {
                                listMessagesID.add(message.getId());
                            }
                            scrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    scrollView.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                            conversation_exists = true;
                            return;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // nothing here
            }

        };
        query.addListenerForSingleValueEvent(listen);
        query.removeEventListener(listen);

    }

    private void fillConversationHistory(List<Message> messagesHistory) {
        for(Message message : messagesHistory) {
            chat_conversation.append(message.getUser()+" : \n"+message.getMessage()+ "\n");
        }
    }

    private void appendChatConversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()){

            chat_msg = (String)  ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String)  ((DataSnapshot)i.next()).getValue();
            chat_conversation.append(chat_user_name +" : \n"+chat_msg+"\n");
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
            listMessagesID.add(dataSnapshot.getKey());
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btn_send_msg) {
            if(conversation_exists) {
                String msgId = ChatDBHelper.getInstance().addMessage(chatID, currentUser.getUsername(), input_msg.getText().toString());
                listHistory.add(new Message(msgId, currentUser.getUsername(), input_msg.getText().toString()));
            }
            else {
                String message = input_msg.getText().toString();
                Message msg = new Message(currentUser.getUsername(), message);

                listHistory.add(msg);
                chatID = FirebaseDatabase.getInstance().getReference().child(Enum.CHATS.toString()).push().getKey();
                Chat chat = new Chat(chatID, currentUser.getId(), user_id, listHistory);
                conversation_exists = true;
                ChatDBHelper.getInstance().addChatChild(chat);
            }

            root =  FirebaseDatabase.getInstance().getReference().child(Enum.CHATS.toString()).child(chatID).child("history");
            Log.e("CHAT ACTIVITY ", " root : " + chatID);
            root.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                  //  Log.e("CHAT ACTIVITY ", " child : " + dataSnapshot.getKey() +s);
                    if(!listMessagesID.contains(dataSnapshot.getKey()))
                    appendChatConversation(dataSnapshot);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.e("CHAT ACTIVITY ", " child Update: " + dataSnapshot.getKey() +s);
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

            input_msg.setText("");
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


    @Override
    public void onBackPressed() {
        chatRoomActivity = null;
        finish();
    }
}

