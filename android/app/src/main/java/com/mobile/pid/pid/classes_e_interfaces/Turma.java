package com.mobile.pid.pid.classes_e_interfaces;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    private Map<String, Boolean> membrosUid;
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

        this.membrosUid = new HashMap<>(1);
        membrosUid.put(professorUid, true);
    }

    // Inserir os dados da turma no banco de dados
    // Erro aqui ao usar o setValue com a Turma t
    public void cadastrar(Activity activity, ProgressDialog progressDialog, String turmaId)
    {
        DatabaseReference dbRoot = FirebaseDatabase.getInstance().getReference();
        DatabaseReference turmasCriadasDatabaseReference = dbRoot.child("userTurmasCriadas");
        DatabaseReference turmasDatabaseReference        = dbRoot.child("turmas");

        // Cadastrar a turma
        turmasDatabaseReference.child(turmaId).setValue(this);

        // Cadastrar referência no turmas_criadas
        turmasCriadasDatabaseReference.child(professorUid).child(turmaId).setValue(true);

        progressDialog.dismiss();
        Toast.makeText(activity, "Turma cadastrada com sucesso", Toast.LENGTH_SHORT).show();
        activity.finish();
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

        // Enviar imagem
        final StorageReference turmaStorageRef = FirebaseStorage.getInstance()
                .getReference("turmas")
                .child(id)
                .child("capa");

        return turmaStorageRef.putFile(capaUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                progressDialog.dismiss();

                if(task.isSuccessful())
                {
                    turmaStorageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            Turma.this.capaUrl = task.getResult().toString();

                            turmaRef.child("capaUrl").setValue(Turma.this.capaUrl);
                        }
                    });
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
        if(getMembros() != null)
            for(String auid : getMembros().keySet())
                turmasMatriculadasRef.child(auid)
                        .child(tid)
                        .removeValue();
    }

    public void desmatricularAluno(String uid)
    {
        if(!membrosUid.containsKey(uid))
            return;

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference membrosRef = rootRef
                .child("turmas")
                .child(id)
                .child("membros")
                .child(uid);

        DatabaseReference alunoTurmaMatriculada = rootRef
                .child("userTurmasMatriculadas")
                .child(uid)
                .child(id);

        membrosRef.removeValue();
        alunoTurmaMatriculada.removeValue();
        membrosUid.remove(uid);
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

    public Map<String, Boolean> getMembros() {
        return membrosUid;
    }

    @Nullable
    public void setMembros(Map<String, Boolean> membrosUid) {
        this.membrosUid = membrosUid;
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
    public int getQtdMembros(){
        if (membrosUid == null)
            return 0;
        else
            return membrosUid.size() - 1;
    }

    public boolean estaNaTurma(String uid)
    {
        if (membrosUid != null)
            if(membrosUid.containsKey(uid))
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