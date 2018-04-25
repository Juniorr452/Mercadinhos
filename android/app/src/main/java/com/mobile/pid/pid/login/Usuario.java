package com.mobile.pid.pid.login;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by junio on 04/03/2018.
 */

public class Usuario implements Parcelable {
    private static final String FOTO_PADRAO_URL = "https://firebasestorage.googleapis.com/v0/b/pi-ii-2920c.appspot.com/o/usuarios%2Ffoto_padrao.png?alt=media&token=b7e19e92-04fa-49ba-a4bd-8b8cfd8c79df";

    @Exclude
    private String uid;

    private String nome;
    private String email;
    private String fotoUrl;

    private String sexo;
    private String dataNascimento;

    public Usuario() { }

    public Usuario(String uid, String nome, String email)
    {
        this.uid     = uid;
        this.nome    = nome;
        this.email   = email;
        this.fotoUrl = FOTO_PADRAO_URL;
    }

    public Usuario(String uid, String nome, String email, String fotoUrl)
    {
        this.uid     = uid;
        this.nome    = nome;
        this.email   = email;
        this.fotoUrl = fotoUrl;
    }

    void cadastrar()
    {
        DatabaseReference usuarioDatabaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(getUid());
        usuarioDatabaseReference.setValue(this);
    }

    @Exclude
    public String getUid(){
        return this.uid;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.nome);
        dest.writeString(this.email);
        dest.writeString(this.fotoUrl);
        dest.writeString(this.sexo);
        dest.writeString(this.dataNascimento);
    }

    protected Usuario(Parcel in) {
        this.uid = in.readString();
        this.nome = in.readString();
        this.email = in.readString();
        this.fotoUrl = in.readString();
        this.sexo = in.readString();
        this.dataNascimento = in.readString();
    }

    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel source) {
            return new Usuario(source);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
}
