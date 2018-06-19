package com.mobile.pid.pid.classes_e_interfaces;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by jonasramos on 13/03/18.
 */

public class Post implements Parcelable{

    private String photoUrl;
    private String nomeUser;
    private String userId;
    private String texto;
    private String id;
    private Long postData;

    public Post() {

    }

    /*public Post(String id, String nomeUser, String photoUrl, String text, Map<String, String> postData) {
        this.id = id;
        this.nomeUser = nomeUser;
        this.photoUrl = photoUrl;
        this.texto = text;
        this.postData = postData;
    }*/

    public Post(String id, String userId, String text) {
        this.id = id;
        this.userId = userId;
        this.texto = text;
    }

    public Post(String id, String text) {
        this.id = id;
        this.texto = text;
    }

    public String getNomeUser() {
        return nomeUser;
    }

    public String getTexto() {
        return texto;
    }

    public void setNomeUser(String nomeUser) {
        this.nomeUser = nomeUser;
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
    public String getPostDataDiaMesAno() {
        return new SimpleDateFormat("dd/MM/yyyy").format(postData);
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

    @Exclude
    public String calcularTempo() throws ParseException
    {
        Calendar c   = Calendar.getInstance();
        String data = getPostDataFormatado();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        Date dataAtual = c.getTime();
        Date dataPost  = sdf.parse(data);

        long duracao     = dataAtual.getTime() - dataPost.getTime();
        long horas       = duracao / (60 * 60 * 1000);
        long minutos     = duracao / (60 * 1000) % 60;
        long segundos    = duracao / 1000 % 60;

        if(horas < 24)
            if(horas == 0)
                if(minutos == 0)
                    if (segundos <= 0)
                        return "Agora";
                    else
                        return String.valueOf(segundos) + "s";
                else
                    return String.valueOf(minutos) + "m";
            else
                return String.valueOf(horas) + "h";
        else
        {
            c.setTime(dataPost);
            return  String.format("%02d", c.get(Calendar.DAY_OF_MONTH)) + "/" +
                    String.format("%02d", (c.get(Calendar.MONTH) + 1)) + "/" +
                    c.get(Calendar.YEAR);
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.photoUrl);
        dest.writeString(this.nomeUser);
        dest.writeString(this.userId);
        dest.writeString(this.texto);
        dest.writeString(this.id);
        dest.writeLong(this.postData);
    }

    protected Post(Parcel in) {
        this.photoUrl = in.readString();
        this.nomeUser = in.readString();
        this.userId = in.readString();
        this.texto = in.readString();
        this.id = in.readString();
        this.postData = in.readLong();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
