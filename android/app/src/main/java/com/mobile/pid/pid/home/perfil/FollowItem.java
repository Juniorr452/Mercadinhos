package com.mobile.pid.pid.home.perfil;

import android.media.Image;

import com.google.firebase.database.Exclude;

/**
 * Created by jonasramos on 19/03/18.
 */

public class FollowItem {

    private String foto;
    private String uid;
    private String nome;

    public FollowItem () {}

    public FollowItem(String uid, String foto, String nome) {
        this.uid = uid;
        this.foto = foto;
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
