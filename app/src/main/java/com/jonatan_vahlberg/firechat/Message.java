package com.jonatan_vahlberg.firechat;

class Message {

    private String UID;
    private String nick;
    private String message;
    private String stamp;
    private int index;

    public Message(String uid, String nickname, String message, String timeStamp){
        this.UID = uid;
        this.nick = nickname;
        this.message = message;
        this.stamp = timeStamp;
    }

    public Message(){
        //PUBLIC no-arg constructor needed
    }

    public String getUID() {
        return UID;
    }

    public String getNick() {
        return nick;
    }

    public String getMessage() {
        return message;
    }

    public String getStamp(){
        return  stamp;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
