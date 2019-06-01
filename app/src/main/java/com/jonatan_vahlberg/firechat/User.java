package com.jonatan_vahlberg.firechat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    public static  final User current =  new User();

    public String UID = "";

    public String nickname;

    private String email;
    public ArrayList<Chat> chats = new ArrayList<>();
    public Map<String,Object> chatIds = new HashMap<>();


    private User() {

    }

    public void setUser(String uid, String email, String nick) {
        this.UID = uid;
        this.email = email;
        this.nickname = nick;
    }
}
