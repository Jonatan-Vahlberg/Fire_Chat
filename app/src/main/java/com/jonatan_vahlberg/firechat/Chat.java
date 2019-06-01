package com.jonatan_vahlberg.firechat;

public class Chat {

    private String id;
    private String chat_id;
    private String chat_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChat_id() {
        return chat_id;
    }


    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getChat_name() {
        return chat_name;
    }

    public void  setChat_name(String chat_name){
        this.chat_name = chat_name;
    }

    public Chat(){

    }

    public Chat(String id,String chat_id,String  chat_name){
        this.id = id;
        this.chat_id = chat_id;
        this.chat_name = chat_name;
    }


}
