package com.jonatan_vahlberg.firechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_main,container,false);

        View chats = v.findViewById(R.id.fragment_main_chats_card);
        chats.setOnClickListener(this);
        TextView chatsText = chats.findViewById(R.id.default_card_text);
        chatsText.setText("Chats");

        View profile = v.findViewById(R.id.fragment_main_profile_card);
        profile.setOnClickListener(this);
        TextView profileText = profile.findViewById(R.id.default_card_text);
        profileText.setText("Profile");
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_main_chats_card:
                Intent intent = new Intent(getContext(),ChatsActivity.class);
                startActivity(intent);
                return;
            case R.id.fragment_main_profile_card:
                Intent intent1 = new Intent(getContext(),ProfileActivity.class);
                startActivity(intent1);
                return;
            default:
                return;
        }
    }
}
