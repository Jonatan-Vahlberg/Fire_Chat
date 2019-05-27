package com.jonatan_vahlberg.firechat.helper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FirebaseHelper {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void enterUserInFirebase(HashMap<String,Object> user){
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("FIRESTORE", "onSuccess: "+documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FIRESTORE", "onFailure: ",e );
                    }
                });
    }

    public static void updateUserInFirebase(){

    }

    public static void addUserChatsInFirebase(Long id){

    }
}
