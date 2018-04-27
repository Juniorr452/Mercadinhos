package com.mobile.pid.pid.home.feed;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by jonasramos on 13/03/18.
 */

public class Post implements Parcelable{

    private String photoUrl;
    private String user;
    private String userId;
    private String texto;
    private String id;
    private Long postData;

    public Post() {

    }

    /*public Post(String id, String user, String photoUrl, String text, Map<String, String> postData) {
        this.id = id;
        this.user = user;
        this.photoUrl = photoUrl;
        this.texto = text;
        this.postData = postData;
    }*/

    public Post(String id, String userId, String user, String photoUrl, String text) {
        this.id = id;
        this.userId = userId;
        this.user = user;
        this.photoUrl = photoUrl;
        this.texto = text;
    }

    public Post(String id, String text) {
        this.id = id;
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

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Exclude
    public String getPostDataFormatado() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(postData);
    }

    @Exclude
    public Long getPostDataLong() {
        return postData;
    }

    public Map<String, String> getPostData() {
        return ServerValue.TIMESTAMP;
    }

    public void setPostData(Long postData) {
        this.postData = postData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.photoUrl);
        dest.writeString(this.user);
        dest.writeString(this.userId);
        dest.writeString(this.texto);
        dest.writeString(this.id);
        dest.writeLong(this.postData);
    }

    protected Post(Parcel in) {
        this.photoUrl = in.readString();
        this.user = in.readString();
        this.userId = in.readString();
        this.texto = in.readString();
        this.id = in.readString();
        this.postData = in.readLong();
    }
}
