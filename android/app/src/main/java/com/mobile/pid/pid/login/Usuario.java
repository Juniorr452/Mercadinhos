package com.mobile.pid.pid.login;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by junio on 04/03/2018.
 */

public class Usuario
{
    private static final String FOTO_PADRAO_URL = "https://firebasestorage.googleapis.com/v0/b/pi-ii-2920c.appspot.com/o/fotos_perfil%2Fpadrao.png?alt=media&token=2b50ce6b-9556-41ec-beb7-160ab3f371f7";

    private String nome;
    private String email;
    private String fotoUrl;

    private String sexo;
    private String dataNascimento;

    public Usuario() { }

    public Usuario(String nome, String email)
    {
        this.nome    = nome;
        this.email   = email;
        this.fotoUrl = FOTO_PADRAO_URL;
    }

    public Usuario(String nome, String email, String fotoUrl)
    {
        this.nome    = nome;
        this.email   = email;
        this.fotoUrl = fotoUrl;
    }

    void cadastrar()
    {
        DatabaseReference usuarioDatabaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(Uid());
        usuarioDatabaseReference.setValue(this);
    }

    public String Uid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
