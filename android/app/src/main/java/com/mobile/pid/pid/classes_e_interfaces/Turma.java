package com.mobile.pid.pid.classes_e_interfaces;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mobile.pid.pid.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by junio on 07/03/2018.
 */

public class Turma implements Serializable
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

    public static Comparator<Turma> compararPorNome = new Comparator<Turma>()
    {
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

    public static Comparator<Turma> compararPorDia = new Comparator<Turma>()
    {
        Calendar calendar = Calendar.getInstance();

        int hoje = calendar.get(Calendar.DAY_OF_WEEK);

        @Override
        public int compare(Turma t1, Turma t2)
        {
            int diat1 = pegarDiaMaisProximo(t1);
            int diat2 = pegarDiaMaisProximo(t2);

            if(diat1 == diat2)
                return 0;

            diat1 = Math.abs(diat1 - hoje);
            diat2 = Math.abs(diat2 - hoje);

            return Integer.compare(diat1, diat2);
        }

        private int pegarDiaMaisProximo(Turma turma)
        {
            List<Integer> dias = new ArrayList<>(turma.getDiasDaSemana().values());

            int diaMaisProximo   = dias.get(0);
            int tamanhoListaDias = dias.size();

            if(diaMaisProximo < hoje)
                diaMaisProximo += 7;

            if(tamanhoListaDias == 1)
                return diaMaisProximo;

            for(int i = 1; i < tamanhoListaDias; i++)
            {
                int dia = dias.get(i);

                if(dia < hoje)
                    dia += 7;

                if(dia < diaMaisProximo)
                    diaMaisProximo = dia;
            }

            Log.i("pegarDiaMaisProximo", "Turma " + turma.getNome() + ": " + diaMaisProximo);
            return diaMaisProximo;
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

    public DatabaseReference atualizar(String nome, String pin, Map<String, Integer> diasDaSemana)
    {
        this.nome = nome;
        this.pin  = pin;
        this.diasDaSemana = diasDaSemana;

        DatabaseReference turmaRef = FirebaseDatabase.getInstance()
                .getReference("turmas")
                .child(id);

        turmaRef.child("nome").setValue(nome);
        turmaRef.child("pin").setValue(pin);
        turmaRef.child("diasDaSemana").setValue(diasDaSemana);

        return turmaRef;
    }

    public StorageTask atualizar(final Context c, String nome, String pin, Uri capaUri, Map<String, Integer> diasDaSemana)
    {
        final DatabaseReference turmaRef = atualizar(nome, pin, diasDaSemana);

        final ProgressDialog progressDialog = Dialogs.dialogEnviandoImagem(c);

        return enviarFotoCapa(capaUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                progressDialog.dismiss();

                if(task.isSuccessful())
                {
                    Turma.this.capaUrl = task.getResult().getDownloadUrl().toString();
                    turmaRef.child("capaUrl").setValue(Turma.this.capaUrl);
                }

                else
                    Dialogs.mensagem(c, R.string.warning, "Erro ao enviar imagem: " + task.getException().getMessage());
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                progressDialog.dismiss();
                Dialogs.mensagem(c, R.string.warning, "Erro ao enviar imagem: " + e.getMessage());
            }
        });
    }

    private UploadTask enviarFotoCapa(Uri capaUri)
    {
        StorageReference turmaStorageRef = FirebaseStorage.getInstance()
                .getReference("turmas")
                .child(id)
                .child("capa");

        return turmaStorageRef.putFile(capaUri);
    }

    public void excluir()
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference turmasCriadasRef = rootRef.child("userTurmasCriadas");
        DatabaseReference turmasMatriculadasRef = rootRef.child("userTurmasMatriculadas");

        String tid = getId();

        // Deletar turma.
        rootRef.child("turmas")
                .child(tid)
                .removeValue();

        // Deletar no turmas_criadas do professor.
        turmasCriadasRef.child(getProfessorUid())
                .child(tid)
                .removeValue();

        // TODO: Verificar se está funcionando
        // Deletar no turmas_matriculadas dos alunos.
        if(getAlunos() != null)
            for(String auid : getAlunos().keySet())
                turmasMatriculadasRef.child(auid)
                        .child(tid)
                        .removeValue();
    }

    public void desmatricularAluno(String uid)
    {
        if(!alunosUid.containsKey(uid))
            return;

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference alunosRef = rootRef
                .child("turmas")
                .child(id)
                .child("alunos")
                .child(uid);

        DatabaseReference alunoTurmaMatriculada = rootRef
                .child("userTurmasMatriculadas")
                .child(uid)
                .child(id);

        alunosRef.removeValue();
        alunoTurmaMatriculada.removeValue();
        alunosUid.remove(uid);
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