package com.mobile.pid.pid.home.perfil;

import android.media.Image;

/**
 * Created by jonasramos on 19/03/18.
 */

public class FollowItem {

    private String foto;
    private String nome;

    public FollowItem () {}

    public FollowItem(String foto, String nome) {
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
}
