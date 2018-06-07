package com.mobile.pid.pid.home.turmas.detalhes_turma.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.mobile.pid.pid.home.adapters.SolicitacaoAdapter;
import com.mobile.pid.pid.home.turmas.Turma;
import com.mobile.pid.pid.login.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SolicitacoesFragment extends Fragment
{
    private static String TAG = "SolicitaçõesFragment";

    RecyclerView rv;
    SolicitacaoAdapter adapter;

    DatabaseReference turmasCriadasRef;
    Turma turma;

    final List<Usuario> solicitacoes = new ArrayList<>();

    public SolicitacoesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v    = inflater.inflate(R.layout.fragment_solicitacoes, container, false);
        Intent i  = getActivity().getIntent();
        Context c = getContext();

        rv    = v.findViewById(R.id.rv_solicitacoes);
        turma = (Turma) i.getSerializableExtra("turma");

        turmasCriadasRef = FirebaseDatabase.getInstance().getReference().child("userTurmasCriadas").child(turma.getProfessorUid());

        adapter = new SolicitacaoAdapter(c, turma, solicitacoes);

        rv.setLayoutManager(new LinearLayoutManager(c));
        rv.setAdapter(adapter);

        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        adapter.clear();

        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference().child("usuarios");

        for(String uid : turma.getSolicitacoes().keySet())
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
