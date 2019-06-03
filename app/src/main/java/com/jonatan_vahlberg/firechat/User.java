package com.jonatan_vahlberg.firechat;

import android.content.Context;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class User {
    public static  final User current =  new User();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userChats;

    public String UID = "";

    public String nickname;

    private String email;
    public ArrayList<Chat> chats = new ArrayList<>();
   // public Map<String,Object> chatIds = new HashMap<>();


    private User() {

    }

    public void setUser(String uid, String email, String nick) {
        this.UID = uid;
        this.email = email;
        this.nickname = nick;
    }

    public void updateChats(final Context context){
        userChats = db.collection("users").document(UID).collection("chats");
        userChats.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    //TODO
                }
                else{
                    chats.clear();
                    for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                        Chat chat = queryDocumentSnapshot.toObject(Chat.class);
                        chat.setId(queryDocumentSnapshot.getId());
                        chats.add(chat);
                    }
                    if(context instanceof ChatsActivity){
                        ((ChatsActivity) context).recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void wipeUserFromMemory(){
        UID = "";
        nickname = "";
        chats.clear();
        email = "";

    }
}
