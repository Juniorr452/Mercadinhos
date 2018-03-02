package com.mobile.pid.pid.home;

/**
 * Created by jonasramos on 02/03/18.
 */

public class Post {

    private int id;
    private String user;
    private String post_message;
    private int image;

    public Post(int id, String user, String post_message, int image) {
        this.id = id;
        this.user = user;
        this.post_message = post_message;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getPost_message() {
        return post_message;
    }

    public int getImage() {
        return image;
    }
}
