package com.mobile.pid.pid.objetos;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by junio on 07/03/2018.
 */

public class Turma implements Serializable // Parcelable Necessário pra passar ele pra outra activity https://youtu.be/ROQ4T47nMhI?t=619
{
    @Exclude
    private String id;

    private String capaUrl;
    private String nome;
    private String pin;

    private Map<String, Integer> diasDaSemana;

    private String professorUid;

    private Map<String, Boolean> alunosUid;
    private Map<String, Boolean> solicitacoes;

    public static Comparator<Turma> compararPorNome = new Comparator<Turma>() {
        @Override
        public int compare(Turma o1, Turma o2) {
            return o1.getNome().compareTo(o2.getNome());
        }
    };

    public Turma() {}

    public Turma(String nome, String pin, String capaUrl, String professorUid, Map<String, Integer> diasDaSemana)
    {
        this.capaUrl      = capaUrl;
        this.nome         = nome;
        this.pin          = pin;
        this.diasDaSemana = diasDaSemana;
        this.professorUid = professorUid;
    }

    public void excluir()
    {
        DatabaseReference rootRef     = FirebaseDatabase.getInstance().getReference();
        DatabaseReference turmasCriadasRef = rootRef.child("userTurmasCriadas");
        DatabaseReference turmasMatriculadasRef = rootRef.child("userTurmasMatriculadas");

        String tuid = getId();

        // Deletar turma.
        rootRef.child("turmas")
                .child(tuid)
                .removeValue();

        // Deletar no turmas_criadas do professor.
        turmasCriadasRef.child(getProfessorUid())
                .child(tuid)
                .removeValue();

        // TODO: Verificar se está funcionando
        // Deletar no turmas_matriculadas dos alunos.
        if(getAlunos() != null)
            for(String auid : getAlunos().keySet())
                turmasMatriculadasRef.child(auid)
                        .child(tuid)
                        .removeValue();
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

    public Map<String, Boolean> getAlunos() {
        return alunosUid;
    }

    @Nullable
    public void setAlunos(Map<String, Boolean> alunosUid) {
        this.alunosUid = alunosUid;
    }

    public Map<String, Integer> getDiasDaSemana() {
        return this.diasDaSemana;
    }

    @Nullable
    public Map<String, Boolean> getSolicitacoes() {
        if(this.solicitacoes == null)
            this.solicitacoes = new HashMap<>();
        return this.solicitacoes;
    }

    @Nullable
    public void setSolicitacoes(Map<String, Boolean> solicitacoes) {
        this.solicitacoes = solicitacoes;
    }

    @Exclude
    public int getQtdAlunos(){
        if (alunosUid == null)
            return 0;
        else
            return alunosUid.size();
    }

    public boolean estaNaTurma(String uid)
    {
        if(uid.equals(professorUid))
            return true;

        if (alunosUid != null)
            for(String auid : alunosUid.keySet())
                if(uid.equals(auid))
                    return true;

        return false;
    }

    public void adicionarSolicitacao(String uid)
    {
        if(solicitacoes == null)
            solicitacoes = new HashMap<>();

        solicitacoes.put(uid, true);
    }

    public boolean enviouSolicitacao(String uid)
    {
        if (solicitacoes == null)
            return false;

        if(solicitacoes.containsKey(uid))
            return true;

        return false;
    }


    /*@Override
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

        if(alunosUid != null)
        {
            dest.writeInt(this.alunosUid.size());
            for (Map.Entry<String, Boolean> aluno : this.alunosUid.entrySet()) {
                dest.writeString(aluno.getKey());
                dest.writeValue(aluno.getValue());
            }
        }

        if(solicitacoes != null)
        {
            dest.writeInt(this.solicitacoes.size());
            for (Map.Entry<String, Boolean> solicitacao : this.solicitacoes.entrySet()) {
                dest.writeString(solicitacao.getKey());
                dest.writeValue(solicitacao.getValue());
            }
        }
    }*/

    /*protected Turma(Parcel in) {
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

        int alunosUidSize = in.readInt();
        this.alunosUid = new HashMap<String, Boolean>(alunosUidSize);
        for (int i = 0; i < alunosUidSize; i++) {
            String key = in.readString();
            Boolean value = (Boolean) in.readValue(Boolean.class.getClassLoader());
            this.alunosUid.put(key, value);
        }

        int solicitacoesSize = in.readInt();
        this.solicitacoes = new HashMap<String, Boolean>(solicitacoesSize);
        for (int i = 0; i < solicitacoesSize; i++) {
            String key = in.readString();
            Boolean value = (Boolean) in.readValue(Boolean.class.getClassLoader());
            this.solicitacoes.put(key, value);
        }
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
    };*/
}