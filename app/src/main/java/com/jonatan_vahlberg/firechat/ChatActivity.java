package com.jonatan_vahlberg.firechat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class ChatActivity extends AppCompatActivity  {

    public String chatPublicId;
    public String chatId = "";
    public String chatName;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<Message> messages = new ArrayList<>();

    EditText messageEdit;

    Button sendBtn;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference chatRef;

    @Override
    protected void onStart() {
        super.onStart();
        //loadMessages();

        chatRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                //messages = new ArrayList<>();
                messages.clear();
                if(e != null){
                    //TODO error
                }
                else{

                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        Message message = documentSnapshot.toObject(Message.class);
                        messages.add(message);
                    }
                    orderArrayOnIndex();
                    adapter.notifyDataSetChanged();
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });

    }

    private void orderArrayOnIndex() {
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return Integer.valueOf(o1.getIndex()).compareTo(o2.getIndex());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeNormal);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setupRecyclerView();

        if(getIntent().hasExtra("CHAT_ID")){
            chatPublicId = "#" + getIntent().getStringExtra("CHAT_PUBLIC_ID");
            chatId = getIntent().getStringExtra("CHAT_ID");
            chatName = getIntent().getStringExtra("CHAT_NAME");
        }
        chatRef = db.collection("Chats/"+chatId+"/messages");
        setupLayout();
    }

    private  void setupLayout(){
        messageEdit = findViewById(R.id.chat_message_edit);
        messageEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    createAndUploadMessage(v.getText().toString());
                    return  false;
                }
                return true;
            }
        });
    }

    private void setupRecyclerView(){
        recyclerView = findViewById(R.id.chat_recycle_view);
        adapter = new ChatRecyclerViewAdapter(this,messages);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager l = new LinearLayoutManager(this);
        l.setStackFromEnd(true);
        recyclerView.setLayoutManager(l);
    }

    private void loadMessages() {
        chatRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    Message message = documentSnapshot.toObject(Message.class);
                    messages.add(message);
                }
            }
        });
    }

    private void createAndUploadMessage(String message){
        if (message.equals("")) return;
        HashMap<String,Object> map = new HashMap<>();
        map.put("message",message);
        map.put("nick",User.current.nickname);
        map.put("UID",User.current.UID);
        map.put("stamp",formatCurrentDate());
        map.put("index",messages.size()+1);

        db.collection("Chats").document(chatId).collection("messages").add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        messageEdit.setText("");
                    }
                });
    }

    private String formatCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        Date d = new Date();
        String dateString = "";
        String date = DateFormat.getDateInstance().format(calendar.getTime());
        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(d);

        dateString = date + " " + time;
        return  dateString;
    }


}
