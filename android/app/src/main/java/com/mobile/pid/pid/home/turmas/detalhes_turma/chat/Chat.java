package com.mobile.pid.pid.home.turmas.detalhes_turma.chat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class Chat implements Parcelable
{
    private String id;
    private String nome;

    public Chat() { }

    public Chat(String nome)
    {
        this.nome = nome;
    }

    public Chat(String id, String nome)
    {
        this.id   = id;
        this.nome = nome;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.nome);
    }

    protected Chat(Parcel in) {
        this.id = in.readString();
        this.nome = in.readString();
    }

    public static final Parcelable.Creator<Chat> CREATOR = new Parcelable.Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel source) {
            return new Chat(source);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };
}
