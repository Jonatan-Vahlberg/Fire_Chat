package com.jonatan_vahlberg.firechat.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jonatan_vahlberg.firechat.Chat;
import com.jonatan_vahlberg.firechat.LoadingFragment;
import com.jonatan_vahlberg.firechat.MainActivity;
import com.jonatan_vahlberg.firechat.User;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {

    private static FragmentTransaction transaction;

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static FirebaseAuth auth = FirebaseAuth.getInstance();


    public static void enterUserInFirestore(HashMap<String,Object> user,String uid){
        db.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FIREBASE CREATION", "onSuccess: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FIREBASE CREATION", "onFailure: ",e );
                    }
                });
    }

    public static void updateUserInFirestore(){

    }

    public static void addUserChatsInFirestore(Long id){

    }

    public static  boolean authenticateUser(final Activity activity, String email, String password){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {

                        if(activity != null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(task.isSuccessful()){
                                        LoadingFragment load = new LoadingFragment();
                                        load.transitionToMain();
                                    }

                                }
                            });
                        }

                    }
                });
        return true;
    }

    public static void autoAuthenticateUser(String email,String password){

        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                        }
                    }
                });
    }

    public static boolean isUserLoggedIn(){
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            Log.d("FIREBASE LOGIN", "isUserLoggedIn: "+currentUser.getEmail());

            return true;
        }
        return  false;
    }

    public static void getUserCoupledDataFromFirestore(final String uid){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot data = task.getResult();
                            if(data.exists()){
                                Log.d("FIREBASE GET", "onComplete: "+data.getData());
                                String nickname = data.getString("nick");
                                String email = data.getString("email");
                                User.current.setUser(uid,email,nickname);

                                db.collection("users/"+uid+"/chats").get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                                    Chat chat = documentSnapshot.toObject(Chat.class);
                                                    chat.setId(documentSnapshot.getId());
                                                    User.current.chats.add(chat);
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });



    }
}
