package com.mobile.pid.pid.home.feed;

/**
 * Created by jonasramos on 13/03/18.
 */

public class Post {

    private String user;
    private String text;
    private String uid;
    
    public Post() {}

    public Post(String uid, String user, String text) {
        this.uid = uid;
        this.user = user;
        this.text = text;
    }

    public Post(String uid, String text) {
        this.uid = uid;
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public String getText() { return text; }

    public void setUser(String user) {
        this.user = user;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
