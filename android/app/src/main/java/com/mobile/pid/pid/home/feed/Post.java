package com.mobile.pid.pid.home.feed;

import java.util.Map;

/**
 * Created by jonasramos on 13/03/18.
 */

public class Post {

    private String photoUrl;
    private String user;
    private String texto;
    private String uid;
    private String postData;

    public Post() {

    }

    public Post(String uid, String user, String photoUrl, String text, String postData) {
        this.uid = uid;
        this.user = user;
        this.photoUrl = photoUrl;
        this.texto = text;
        this.postData = postData;
    }

    public Post(String uid, String text) {
        this.uid = uid;
        this.texto = text;
    }

    public String getUser() {
        return user;
    }

    public String getTexto() {
        return texto;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTexto(String text) {
        this.texto = text;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }
}
