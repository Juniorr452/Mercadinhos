package com.mobile.pid.pid.home.turmas;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by junio on 07/03/2018.
 */

// TODO: Completar classe Turma

public class Turma implements Parcelable // Parcelable Necessário pra passar ele pra outra activity https://youtu.be/ROQ4T47nMhI?t=619
{
    @Exclude
    private String id;

    private String capaUrl;
    private String nome;
    private String pin;

    private Map<String, Integer> diasDaSemana;

    private String professorUid;
    private List<String> alunosUid;

    public Turma() {}

    public Turma(String nome, String pin, String capaUrl, String professorUid, Map<String, Integer> diasDaSemana)
    {
        this.capaUrl      = capaUrl;
        this.nome         = nome;
        this.pin          = pin;
        this.diasDaSemana = diasDaSemana;
        this.professorUid = professorUid;
    }

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

    public String getPin(){
        return this.pin;
    }

    public void setPin(String pin){
        this.pin = pin;
    }

    public String getCapaUrl() {
        return this.capaUrl;
    }

    public String getProfessorUid() {
        return professorUid;
    }

    public void setProfessorUid(String professorUid) {
        this.professorUid = professorUid;
    }

    public List<String> getAlunosUid() {
        return alunosUid;
    }

    public void setAlunosUid(List<String> alunosUid) {
        this.alunosUid = alunosUid;
    }

    public Map<String, Integer> getDiasDaSemana() {
        return this.diasDaSemana;
    }

    @Exclude
    public String getQtdAlunos(){
        if (alunosUid == null)
            return "0";
        else
            return Integer.toString(alunosUid.size());
    }

    public boolean estaNaTurma(String uid)
    {
        if(uid.equals(professorUid))
            return true;

        if (alunosUid != null)
            for(String auid : alunosUid)
                if(uid.equals(auid))
                    return true;

        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.capaUrl);
        dest.writeString(this.nome);
        dest.writeString(this.pin);
        dest.writeInt(this.diasDaSemana.size());
        for (Map.Entry<String, Integer> entry : this.diasDaSemana.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeValue(entry.getValue());
        }
        dest.writeString(this.professorUid);
        dest.writeStringList(this.alunosUid);
    }

    protected Turma(Parcel in) {
        this.id = in.readString();
        this.capaUrl = in.readString();
        this.nome = in.readString();
        this.pin = in.readString();
        int diasDaSemanaSize = in.readInt();
        this.diasDaSemana = new HashMap<String, Integer>(diasDaSemanaSize);
        for (int i = 0; i < diasDaSemanaSize; i++) {
            String key = in.readString();
            Integer value = (Integer) in.readValue(Integer.class.getClassLoader());
            this.diasDaSemana.put(key, value);
        }
        this.professorUid = in.readString();
        this.alunosUid = in.createStringArrayList();
    }

    public static final Creator<Turma> CREATOR = new Creator<Turma>() {
        @Override
        public Turma createFromParcel(Parcel source) {
            return new Turma(source);
        }

        @Override
        public Turma[] newArray(int size) {
            return new Turma[size];
        }
    };
}