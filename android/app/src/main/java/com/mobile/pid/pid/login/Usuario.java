package com.mobile.pid.pid.login;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by junio on 04/03/2018.
 */

public class Usuario
{
    private static final String FOTO_PADRAO_URL = "https://firebasestorage.googleapis.com/v0/b/pi-ii-2920c.appspot.com/o/fotos_perfil%2Fpadrao.png?alt=media&token=2b50ce6b-9556-41ec-beb7-160ab3f371f7";

    private String Uid;
    private String nome;
    private String email;
    private String fotoUrl;

    public Usuario() { }

    public Usuario(String Uid, String nome, String email)
    {
        this.Uid     = Uid;
        this.nome    = nome;
        this.email   = email;
        this.fotoUrl = FOTO_PADRAO_URL;
    }

    public Usuario(String Uid, String nome, String email, String fotoUrl)
    {
        this.Uid     = Uid;
        this.nome    = nome;
        this.email   = email;
        this.fotoUrl = fotoUrl;
    }

    void cadastrar()
    {
        DatabaseReference usuarioDatabaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(Uid);
        usuarioDatabaseReference.setValue(this);
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
