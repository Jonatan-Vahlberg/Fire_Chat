package com.jonatan_vahlberg.firechat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jonatan_vahlberg.firechat.helper.FirebaseHelper;
import com.jonatan_vahlberg.firechat.helper.KeyHelper;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private FragmentTransaction transaction;
    EditText emailField, passwordField;
    ProgressBar bar;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPreferences = this.getActivity().getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE);

        View v = inflater.inflate(R.layout.content_login,container,false);
        editor = sharedPreferences.edit();

        emailField = v.findViewById(R.id.fragment_login_email_field);
        passwordField = v.findViewById(R.id.fragment_login_password_field);

        Button loginButton, registerButton;

        loginButton = v.findViewById(R.id.fragment_login_login_btn);
        loginButton.setOnClickListener(this);

        registerButton = v.findViewById(R.id.fragment_login_register_btn);
        registerButton.setOnClickListener(this);

        bar = v.findViewById(R.id.fragment_login_progress);

        return v;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.fragment_login_login_btn:
                loginPressed();
                break;
            case R.id.fragment_login_register_btn:
                registerPressed();
        }
    }



    private void loginPressed() {
        final String email,password;
        showLoading();
        email = emailField.getText().toString();
        password = passwordField.getText().toString();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            bar.setVisibility(View.GONE);
                            addCredentials(password,email);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_holder,new MainFragment());
                            transaction.commit();
                        }

                    }
                });
    }

    private void registerPressed() {
        transaction =  getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder,new RegisterFragment());
        transaction.commit();
    }

    public void showLoading() {
        Log.d("HURRAY", "onComplete: HURRAY");
        bar.setVisibility(View.VISIBLE);
    }

    private void addCredentials(String password, String email){
        String key = KeyHelper.createEncryptKey(password);
        editor.putString(KeyHelper.PASSWORD_KEY,key);
        editor.putString(KeyHelper.EMAIL_KEY,email);
        editor.apply();
    }
}
