package com.mobile.pid.pid.classes_e_interfaces;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

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
        return new SimpleDateFormat("dd-MM-yyyy - HH:mm:ss").format(data);
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
