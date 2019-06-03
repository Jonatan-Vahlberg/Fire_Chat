package com.jonatan_vahlberg.firechat;

import android.content.Context;
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

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private FragmentTransaction transaction;

    private EditText emailField, passwordField, nickField;
    private ProgressBar bar;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPreferences = this.getActivity().getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        View v = inflater.inflate(R.layout.content_register,container,false);
        emailField = v.findViewById(R.id.fragment_register_email_field);
        passwordField = v.findViewById(R.id.fragment_register_password_field);
        nickField = v.findViewById(R.id.fragment_register_nick_field);

        Button loginButton, registerButton;

        loginButton = v.findViewById(R.id.fragment_register_login_btn);
        loginButton.setOnClickListener(this);

        registerButton = v.findViewById(R.id.fragment_register_register_btn);
        registerButton.setOnClickListener(this);

        bar = v.findViewById(R.id.fragment_register_progress);

        return v;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.fragment_register_login_btn:
                loginPressed();
                break;
            case R.id.fragment_register_register_btn:
                registerPressed();
        }
    }



    private void loginPressed() {
        transaction =  getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder,new LoginFragment());
        transaction.commit();
    }

    private void registerPressed() {
        showLoading();
        final  String email = emailField.getText().toString();
        final  String password = passwordField.getText().toString();
        final  String nick = nickField.getText().toString();

        if(email.equals("") || password.equals("") || nick.equals("")){
            return;
        }
        else{
            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                bar.setVisibility(View.GONE);
                                addCredentials(password,email);
                                fragmentReplace(new MainFragment());
                                String uid = task.getResult().getUser().getUid();
                                HashMap<String,Object> map = new HashMap<>();
                                map.put("email",email);
                                map.put("nick",nick);
                                map.put("chats",new HashMap<String,Number>());
                                FirebaseHelper.enterUserInFirestore(map,uid);

                                User.current.setUser(uid,email,nick);
                            }
                            else{
                                Log.e("FIREBASE", "onComplete: USER NOT ENTRED",task.getException() );
                            }
                        }
                    });
        }
    }

    private void fragmentReplace(Fragment fragment){
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder,fragment);
        transaction.commit();
    }

    private  void showLoading(){
        bar.setVisibility(View.VISIBLE);
    }

    private void addCredentials(String password, String email){
        String key = KeyHelper.createEncryptKey(password);
        editor.putString(KeyHelper.PASSWORD_KEY,key);
        editor.putString(KeyHelper.EMAIL_KEY,email);
        editor.apply();
    }
}
