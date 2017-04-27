package com.inf8405.projet_inf8405.fireBaseHelper;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inf8405.projet_inf8405.activities.ChatRoomActivity;
import com.inf8405.projet_inf8405.activities.MapsActivity;
import com.inf8405.projet_inf8405.model.Chat;
import com.inf8405.projet_inf8405.model.ListeChatsCurrentUser;
import com.inf8405.projet_inf8405.model.Message;
import com.inf8405.projet_inf8405.utils.Enum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LyesKriaa on 17-04-22.
 */

public class ChatDBHelper {
    private final String TAG = "CHAT_DB_HELPER";
    private DatabaseReference chatsRef = null;
    private Chat currentChat;
    private static ChatDBHelper INSTANCE = null;
    private ChildEventListener childEventListener = null;


    private ChatDBHelper() {
        chatsRef = FirebaseDatabase.getInstance().getReference().child(Enum.CHATS.toString()).getRef();
        chatsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                //for (DataSnapshot chat : dataSnapshot.getChildren()) {
                    readData(dataSnapshot);
                //}

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                //if(MapsActivity.getMapsActivity() != null ) MapsActivity.getMapsActivity().refresh();
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }

        });
        Log.e(TAG, "listener:" + "set: "+ chatsRef);
    }

    public static ChatDBHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChatDBHelper();
        }
        return INSTANCE;
    }

    public Chat getCurrentChat() {
        return currentChat;
    }

    public void setCurrentChat(Chat currentChat) {
        this.currentChat = currentChat;
    }

    public DatabaseReference getchatsRef() {
        return chatsRef;
    }

    public void setChatRef(String chat) {
        chatsRef = FirebaseDatabase.getInstance().getReference();
        Log.e(TAG, "refChat set to : " + chatsRef.child(chat).getRef());
    }

    public void addChatChild(Chat chat) {
        Map<String, Object> chatToAdd = new HashMap<String, Object>();
        chatToAdd.put("history", chat.getMessagesHistory());
        chatToAdd.put("user1", chat.getIdFirstUser());
        chatToAdd.put("user2", chat.getIdSecondUser());

        chatsRef.child(chat.getId()).setValue(chatToAdd);

    }

    public String addMessage(String idChat, String username, String message) {
        HashMap<String,String> messageToAdd = new HashMap<String, String>();
        messageToAdd.put("message", message);
        messageToAdd.put("user", username);
        String msgId = chatsRef.child(idChat).child("history").push().getKey();
        chatsRef.child(idChat).child("history").child(msgId).setValue(messageToAdd);
        return msgId;
    }

    public void readData(DataSnapshot dataSnapshot) {

        if(dataSnapshot.hasChildren()) {
            String chatId        = dataSnapshot.getKey();
            String user1         = dataSnapshot.child("user1").getValue() != null ? dataSnapshot.child("user1").getValue().toString() : null;
            String user2         = dataSnapshot.child("user2").getValue() != null ? dataSnapshot.child("user2").getValue().toString() : null;

            if(UserDBHelper.getInstance().getCurrentUser().getId().equals(user1) || UserDBHelper.getInstance().getCurrentUser().getId().equals(user2)) {
                List<Message> history = new ArrayList<Message>();
                if (dataSnapshot.child("history").hasChildren()) {
                    for (DataSnapshot messageNode : dataSnapshot.child("history").getChildren()) {
                        Message msg = new Message(messageNode.getKey(), messageNode.child("user").getValue().toString(), messageNode.child("message").getValue().toString());
                        history.add(msg);
                    }
                }
                ListeChatsCurrentUser.getInstance().addChat(new Chat(chatId, user1, user2, history));
            }
        }
        if(MapsActivity.getMapsActivity() != null && ChatRoomActivity.getChatRoomActivity() == null) {
            MapsActivity.getMapsActivity().notifyMsg();
        }
    }

    public void destroy() {
        currentChat = null;
        chatsRef = null;
        INSTANCE = null;
    }
}

