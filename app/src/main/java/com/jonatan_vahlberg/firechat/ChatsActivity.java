package com.jonatan_vahlberg.firechat;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

public class ChatsActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userChats;

    private Dialog createDialog;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    Button searchBtn;
    EditText searchText;
    FloatingActionButton createDialogBtn;

    @Override
    protected void onStart() {
        super.onStart();
        userChats = db.collection("users").document(User.current.UID).collection("chats");
        userChats.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    //TODO
                }
                else{
                    User.current.chats.clear();
                    for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                        Chat chat = queryDocumentSnapshot.toObject(Chat.class);
                        chat.setId(queryDocumentSnapshot.getId());
                        User.current.chats.add(chat);
                    }
                    ChatsActivity.this.recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        setupRecyclerView();

        createDialogBtn = findViewById(R.id.chats_create_dialog_btn);
        createDialogBtn.setOnClickListener(this);
        searchText = findViewById(R.id.chats_find_text);

        searchBtn = findViewById(R.id.chats_find_btn);
        searchBtn.setOnClickListener(this);
    }

    private void setupRecyclerView(){
        recyclerView = findViewById(R.id.chats_recycler_view);
        adapter = new ChatsRecycleViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chats_create_dialog_btn:
                renderCreateFragment(this);
                break;
            case R.id.chats_find_btn:
                searchForChat();
                break;
        }
    }

    private void searchForChat() {
        db.collection("Chats").whereEqualTo("chat_id",searchText.getText().toString())
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(ChatsActivity.this,"Cant find chat based on that id",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(queryDocumentSnapshots.size() < 1) Toast.makeText(ChatsActivity.this,"Cant find chat based on that id",Toast.LENGTH_SHORT).show();
                    for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                        final String docRefId = queryDocumentSnapshot.getId();
                        Map<String,Object> map = queryDocumentSnapshot.getData();

                        final HashMap<String,Object> mapForUsers = new HashMap<>();
                        mapForUsers.put("chat_id",map.get("chat_id"));
                        mapForUsers.put("chat_name",map.get("chat_name"));


                        db.collection("users").document(User.current.UID).collection("chats").document(docRefId).set(mapForUsers);
                        User.current.updateChats(ChatsActivity.this);
                    }
                }

            }
        });
    }

    private void renderCreateFragment(final Context context) {
        createDialog = new Dialog(this);
        createDialog.setContentView(R.layout.create_dialog);
        Random random = new Random();
        final int id = random.nextInt(9999999)+1000000;
        final EditText chatName = createDialog.findViewById(R.id.dialog_create_edit);
        TextView idTextView = createDialog.findViewById(R.id.dialog_create_id_text);
        idTextView.setText("Auto Generated Id: #"+id);
        final Button createBtn;
        ImageButton cancelBtn = createDialog.findViewById(R.id.dialog_create_cancel_btn);

        createBtn = createDialog.findViewById(R.id.dialog_create_btn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chatName.getText().toString().length() > 0){
                    final Map<String,Object> chatMap = new HashMap<>();
                    chatMap.put("chat_name",chatName.getText().toString());
                    chatMap.put("chat_id",id);
                    db.collection("Chats").add(chatMap)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    final String docRefId = documentReference.getId();
                                       db.collection("users").document(User.current.UID).collection("chats").document(docRefId).set(chatMap)
                                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                   @Override
                                                   public void onSuccess(Void aVoid) {
                                                       createDialog.dismiss();
                                                       if(context instanceof  ChatsActivity){
                                                           Chat chat = new Chat(docRefId,Integer.toString(id),chatName.getText().toString());
                                                           User.current.chats.add(chat);
                                                           ((ChatsActivity) context).adapter.notifyDataSetChanged();
                                                       }

                                                   }
                                               });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //TODO: failure message
                                }
                            });


                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog.dismiss();
            }
        });
        createDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        createDialog.show();
    }
}
