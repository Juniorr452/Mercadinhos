package com.mobile.pid.pid.home.turmas;

import android.support.v7.app.AlertDialog;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.mobile.pid.pid.objetos.Post;

import java.text.SimpleDateFormat;
import java.util.Map;

public class AvisoTurma
{
    private String aviso;
    private Long   data;

    public AvisoTurma(){

    }

    public AvisoTurma(String aviso) {
        this.aviso = aviso;
    }

    public AvisoTurma(Post p){
        this.aviso = p.getTexto();
        this.data  = p.getPostDataLong();
    }

    public Map<String, String> getData() {
        return ServerValue.TIMESTAMP;
    }

    @Exclude
    public String getDataFormatada(){
        return new SimpleDateFormat("dd-MM-yyyy").format(data);
    }

    public void setData(Long data) {
        this.data = data;
    }

    public String getAviso() {
        return aviso;
    }

    public void setAviso(String aviso) {
        this.aviso = aviso;
    }

    public void postarAviso(String tid){
        final DatabaseReference avisosReference = FirebaseDatabase.getInstance().getReference()
                .child("avisos")
                .child(tid);

        avisosReference.push().setValue(this);
    }
}
