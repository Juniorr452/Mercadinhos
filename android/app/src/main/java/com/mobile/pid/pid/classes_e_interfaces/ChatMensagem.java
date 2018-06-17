package com.mobile.pid.pid.classes_e_interfaces;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobile.pid.pid.R;

public class ChatMensagem
{
    private DatabaseReference chatRef;

    // ID do usuário que enviou.
    private String  uid;
    private String  fotoUrl;
    private String  mensagem;
    private String  imagemUrl;
    private boolean professor;

    public ChatMensagem() { }

    public ChatMensagem(String uid, String fotoUrl, String mensagem, boolean professor, DatabaseReference chatRef)
    {
        this.uid       = uid;
        this.chatRef   = chatRef;
        this.fotoUrl   = fotoUrl;
        this.mensagem  = mensagem;
        this.professor = professor;
    }

    public void enviar() {
        chatRef.push().setValue(this);
    }

    public void enviarComFoto(final Context c, Uri fotoUri, String tid, String nome)
    {
        StorageReference chatStorageRef = FirebaseStorage.getInstance()
            .getReference("turmas")
            .child(tid)
            .child("imagens")
            .child(nome);

        final ProgressDialog progressDialog = new ProgressDialog(c);
        progressDialog.setTitle("Enviando imagem...");
        progressDialog.setMessage("Aguarde...");
        progressDialog.show();

        chatStorageRef.putFile(fotoUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    ChatMensagem.this.imagemUrl = task.getResult().getDownloadUrl().toString();
                    enviar();

                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                progressDialog.dismiss();

                new AlertDialog.Builder(c)
                    .setTitle(R.string.warning)
                    .setMessage("Erro ao enviar mensagem, tente novamente")
                    .show();
            }
        });
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoPerfil) {
        this.fotoUrl = fotoPerfil;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public boolean isProfessor() {
        return professor;
    }

    public void setProfessor(boolean professor) {
        this.professor = professor;
    }
}
