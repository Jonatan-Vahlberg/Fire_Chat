package com.jonatan_vahlberg.firechat;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
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


    }
}
