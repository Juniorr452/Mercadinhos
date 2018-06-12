package com.mobile.pid.pid.home.turmas.detalhes_turma.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.adapters.ChatMensagemAdapter;
import com.mobile.pid.pid.home.turmas.Turma;
import com.mobile.pid.pid.objetos.Usuario;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity
{
    private static final int PROFESSOR = 0;
    private static final int ALUNO = 1;

    private Turma        turma;
    private Chat         chat;
    private FirebaseUser user;
    private String       fotoPerfil;
    private boolean      professor;

    private ProgressBar  progressBar;

    private Toolbar     toolbar;
    private ImageButton adicionarArquivo;
    private EditText    etMensagem;
    private ImageButton enviar;

    private DatabaseReference  dbRoot;
    private DatabaseReference chatRef;

    private RecyclerView        recyclerView;
    private LinearLayoutManager layoutManager;
    private ChatMensagemAdapter chatMensagemAdapter;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent i = getIntent();

        progressBar = findViewById(R.id.pb_chat);

        user      = FirebaseAuth.getInstance().getCurrentUser();
        chat      = i.getParcelableExtra("chat");
        turma     = i.getParcelableExtra("turma");
        professor = i.getIntExtra("usuario", 1) == PROFESSOR;

        toolbar = findViewById(R.id.toolbar_chat);
        recyclerView = findViewById(R.id.recycler_view_chat);
        adicionarArquivo = findViewById(R.id.chat_adicionar_arquivo);
        etMensagem       = findViewById(R.id.et_chat);
        enviar           = findViewById(R.id.chat_enviar);

        dbRoot  = FirebaseDatabase.getInstance().getReference();
        chatRef = dbRoot.child("chats").child(chat.getId());

        layoutManager = new LinearLayoutManager(this);
        chatMensagemAdapter = new ChatMensagemAdapter(this, new ArrayList<ChatMensagem>());

        toolbar.setTitle(chat.getNome());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(chatMensagemAdapter);

        pegarFotoPerfil();
        chatRef.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                ChatMensagem cm = dataSnapshot.getValue(ChatMensagem.class);

                chatMensagemAdapter.add(cm);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void enviar(View v)
    {
        try
        {
            String uid  = user.getUid();
            String msg  = validarMensagem();

            ChatMensagem cm = new ChatMensagem(uid, fotoPerfil, msg, professor);

            chatRef.push().setValue(cm);
        }
        catch(NoSuchFieldException e)
        {
            // Nada
            /*new AlertDialog.Builder(this)
                    .setTitle("Erro")
                    .setMessage(e.getMessage())
                    .setPositiveButton("Ok", null)
                    .show();*/
        }
    }

    public String validarMensagem() throws NoSuchFieldException
    {
        String m = etMensagem.getText().toString();
        m.trim();

        if (m.equals(""))
            throw new NoSuchFieldException("Digite alguma coisa");

        etMensagem.setText("");

        return m;
    }

    public void pegarFotoPerfil()
    {
        String uid = user.getUid();

        DatabaseReference usuarioRef = dbRoot.child("usuarios").child(uid);

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario a = dataSnapshot.getValue(Usuario.class);

                fotoPerfil = a.getFotoUrl();

                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
