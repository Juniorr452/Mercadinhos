package com.mobile.pid.pid.home.feed;

/**
 * Created by jonasramos on 13/03/18.
 */

public class Post {

    private String user;
    private String text;

    public Post(String user, String text) {
        this.user = user;
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }
}
