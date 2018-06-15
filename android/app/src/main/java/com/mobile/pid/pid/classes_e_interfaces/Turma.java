package com.mobile.pid.pid.classes_e_interfaces;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
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
        public int compare(Turma o1, Turma o2)
        {
            if(o1 == null)
                return 1;

            if(o2 == null)
                return -1;

            return o1.getNome().compareTo(o2.getNome());
        }
    };

    // TODO: Comparar por dia
    public static Comparator<Turma> compararPorDia = new Comparator<Turma>() {
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

    public void atualizar(String nome, String pin, Map<String, Integer> diasDaSemana)
    {
        DatabaseReference turmaRef = FirebaseDatabase.getInstance()
                .getReference("turmas")
                .child(id);

        turmaRef.child("nome").setValue(nome);
        turmaRef.child("pin").setValue(pin);
        turmaRef.child("diasDaSemana").setValue(diasDaSemana);
    }

    public void excluir()
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
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
}