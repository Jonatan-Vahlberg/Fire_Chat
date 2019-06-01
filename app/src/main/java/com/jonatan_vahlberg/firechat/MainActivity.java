package com.jonatan_vahlberg.firechat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.google.firebase.auth.FirebaseAuth;
import com.jonatan_vahlberg.firechat.helper.FirebaseHelper;
import com.jonatan_vahlberg.firechat.helper.KeyHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FragmentTransaction fragmentTransaction;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map<String,Object> user = new HashMap<>();
        user.put("email","admin@altor.mail");
        user.put("chats", new ArrayList<Long>());

        AnimationDrawable animationDrawable = (AnimationDrawable) findViewById(R.id.home_background).getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        sharedPreferences = getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE);
        if(FirebaseHelper.isUserLoggedIn()){
            fragmentTransaction.replace(R.id.fragment_holder,new MainFragment());
            User.current.UID = FirebaseAuth.getInstance().getUid();
            FirebaseHelper.getUserCoupledDataFromFirestore(User.current.UID);
        }
        else{
            if(sharedPreferences.contains(KeyHelper.PASSWORD_KEY ) && sharedPreferences.contains(KeyHelper.EMAIL_KEY)){
                String email, password;
                email = sharedPreferences.getString(KeyHelper.EMAIL_KEY,"");
                password = sharedPreferences.getString(KeyHelper.PASSWORD_KEY,"");
                password = KeyHelper.decryptKey(password);

                FirebaseHelper.autoAuthenticateUser(email,password);
                fragmentTransaction.replace(R.id.fragment_holder, new MainFragment());
            }
            else{
                fragmentTransaction.add(R.id.fragment_holder,new LoginFragment());
            }

        }
        fragmentTransaction.commit();



    }


}
