package com.mobile.pid.pid.home.turmas.detalhes_turma.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.adapters.ChatsAdapter;
import com.mobile.pid.pid.objetos.Turma;
import com.mobile.pid.pid.home.turmas.detalhes_turma.chat.Chat;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment
{
    private static final String TAG = "ChatsFragment";

    private Turma turma;

    private ProgressBar progressBar;
    private FrameLayout conteudo;
    private ImageView   sadFace;
    private TextView    sadMessage;

    private DatabaseReference chatsRef;

    private RecyclerView recyclerView;
    private ChatsAdapter chatsAdapter;

    private FloatingActionButton fabCriarChat;

    private ValueEventListener chatsListener;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_chats, container, false);

        progressBar  = v.findViewById(R.id.pb_chats);
        conteudo     = v.findViewById(R.id.fl_chats);
        sadFace      = v.findViewById(R.id.sad_face_chats);
        sadMessage   = v.findViewById(R.id.sad_message_chats);
        fabCriarChat = v.findViewById(R.id.fab_criar_chat);
        recyclerView = v.findViewById(R.id.rv_chats);
        fabCriarChat = v.findViewById(R.id.fab_criar_chat);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView = v.findViewById(R.id.rv_chats);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(chatsAdapter);

        conteudo.setVisibility(View.INVISIBLE);

        fabCriarChat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final View d = getLayoutInflater().inflate(R.layout.dialog_criar_chat, null);

                final TextView chatExibidoComo = d.findViewById(R.id.chat_exibido_como);
                final EditText chatNome = d.findViewById(R.id.dialog_chat_nome);

                final TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {
                        String nome = getString(R.string.chat_exibido_como) + " " + getNomeChat(s.toString());
                        chatExibidoComo.setText(nome);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };

                chatNome.addTextChangedListener(textWatcher);

                DialogInterface.OnClickListener criarConta = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        String nome = chatNome.getText().toString();
                        Chat      c = new Chat(getNomeChat(nome));

                        chatsRef.push().setValue(c);
                    }
                };

                new AlertDialog.Builder(getContext(), R.style.DialogTheme)
                        .setView(d)
                        .setPositiveButton("Criar", criarConta)
                        .setNegativeButton(R.string.cancel, null)
                        .create()
                        .show();
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        turma        = (Turma) getArguments().getSerializable("turma");
        int usuario  = getArguments().getInt("usuario");
        chatsAdapter = new ChatsAdapter(getActivity(), new ArrayList<Chat>(), turma, usuario);
        chatsRef     = FirebaseDatabase.getInstance().getReference()
                        .child("turmas")
                        .child(turma.getId())
                        .child("chats");

        chatsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    recyclerView.getRecycledViewPool().clear();
                    chatsAdapter.clear();

                    for(DataSnapshot dataChats : dataSnapshot.getChildren())
                    {
                        Chat c = dataChats.getValue(Chat.class);
                        c.setId(dataChats.getKey());

                        chatsAdapter.add(c);
                    }

                    sadFace.setVisibility(View.GONE);
                    sadMessage.setVisibility(View.GONE);
                }
                else
                {
                    sadFace.setVisibility(View.VISIBLE);
                    sadMessage.setVisibility(View.VISIBLE);
                }

                progressBar.setVisibility(View.GONE);
                conteudo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        chatsRef.addValueEventListener(chatsListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        chatsRef.removeEventListener(chatsListener);
    }

    public String getNomeChat(String nome){
        return nome.replace(' ', '-');
    }
}
