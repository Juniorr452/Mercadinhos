package com.mobile.pid.pid.home.turmas.detalhes_turma.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.adapters.SolicitacaoAdapter;
import com.mobile.pid.pid.objetos.Turma;
import com.mobile.pid.pid.objetos.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SolicitacoesFragment extends Fragment
{
    private static String TAG = "SolicitaçõesFragment";

    private RecyclerView rv;
    private SolicitacaoAdapter adapter;

    private DatabaseReference turmasCriadasRef;

    private DatabaseReference usuariosRef;
    private DatabaseReference solicRef;
    private ValueEventListener solicListener;
    private Turma turma;

    final List<Usuario> solicitacoes = new ArrayList<>();

    public SolicitacoesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v    = inflater.inflate(R.layout.fragment_solicitacoes, container, false);
        Context c = getContext();

        rv    = v.findViewById(R.id.rv_solicitacoes);


        turmasCriadasRef = FirebaseDatabase.getInstance().getReference().child("userTurmasCriadas").child(turma.getProfessorUid());

        adapter = new SolicitacaoAdapter(c, turma, solicitacoes);

        rv.setLayoutManager(new LinearLayoutManager(c));
        rv.setAdapter(adapter);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i  = getActivity().getIntent();
        turma = (Turma) i.getSerializableExtra("turma");

        usuariosRef = FirebaseDatabase.getInstance().getReference().child("usuarios");
        solicRef = FirebaseDatabase.getInstance().getReference("turmas").child(turma.getId()).child("solicitacoes");

        solicListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                adapter.clear();

                if(dataSnapshot.exists()) {
                    for(DataSnapshot data : dataSnapshot.getChildren()) {

                        String uid = data.getKey();

                        usuariosRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                Usuario u = dataSnapshot.getValue(Usuario.class);
                                u.setUid(dataSnapshot.getKey());
                                adapter.add(u);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


    }

    @Override
    public void onStart()
    {
        super.onStart();

        solicRef.addValueEventListener(solicListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        solicRef.removeEventListener(solicListener);
    }
}
