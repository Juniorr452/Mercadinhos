package com.mobile.pid.pid.home.turmas.detalhes_turma.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.Dialogs;
import com.mobile.pid.pid.home.adapters.AvisosTurmaAdapter;
import com.mobile.pid.pid.home.turmas.AvisoTurma;
import com.mobile.pid.pid.home.turmas.Turma;

public class AvisosFragment extends Fragment
{
    private static final int PROFESSOR = 0;
    private static final int ALUNO = 1;

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private Turma turma;
    int   usuario;

    private AvisosTurmaAdapter avisosTurmaAdapter;

    private DatabaseReference  avisosReference;
    private ChildEventListener avisosListener;

    public AvisosFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_avisos, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        floatingActionButton = view.findViewById(R.id.fab_add_aviso);

        if(usuario == PROFESSOR)
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novoAviso();
            }
        });
        else
            floatingActionButton.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(avisosTurmaAdapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        turma = (Turma) getArguments().getSerializable("turma");
        usuario = getArguments().getInt("usuario");

        avisosTurmaAdapter = new AvisosTurmaAdapter(getContext());
        avisosReference = FirebaseDatabase.getInstance().getReference().child("avisos").child(turma.getId());

        avisosListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AvisoTurma aviso = dataSnapshot.getValue(AvisoTurma.class);

                avisosTurmaAdapter.add(aviso);
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
        };
    }

    @Override
    public void onStart() {
        super.onStart();

        avisosReference.addChildEventListener(avisosListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        avisosReference.removeEventListener(avisosListener);
    }

    public void novoAviso() {
        Dialogs.dialogAvisoTurma(getContext(), turma.getId());
    }
}
