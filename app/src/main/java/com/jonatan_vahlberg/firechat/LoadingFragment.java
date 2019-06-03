package com.jonatan_vahlberg.firechat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.jonatan_vahlberg.firechat.helper.FirebaseHelper;

public class LoadingFragment extends Fragment{
    private FragmentTransaction transaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_loading,container,false);
        if(FirebaseHelper.isUserLoggedIn()){
            transitionToMain();
        }
        return v;
    }

    public void transitionToMain(){
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_holder,new LoadingFragment());
        transaction.commit();
    }

}
