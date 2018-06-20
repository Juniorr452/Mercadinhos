package com.mobile.pid.pid.home.turmas.detalhes_turma.fragments;

import android.os.Bundle;
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
import com.mobile.pid.pid.home.adapters.MembrosAdapter;
import com.mobile.pid.pid.classes_e_interfaces.Turma;
import com.mobile.pid.pid.classes_e_interfaces.Usuario;

public class MembrosFragment extends Fragment
{
    DatabaseReference firebaseDatabase;
    DatabaseReference membrosReference;
    DatabaseReference usuariosReference;

    Turma turma;

    RecyclerView recyclerView;

    MembrosAdapter membrosAdapter;

    public MembrosFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        turma = (Turma) getArguments().getSerializable("turma");

        firebaseDatabase   = FirebaseDatabase.getInstance().getReference();
        membrosReference   = firebaseDatabase.child("turmas").child(turma.getId()).child("membros");
        usuariosReference  = firebaseDatabase.child("usuarios");

        membrosAdapter = new MembrosAdapter(getContext(), turma);

        pegarMembrosTurma();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_membros, container, false);

        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(membrosAdapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView.getRecycledViewPool().clear();
        membrosAdapter.ordenarMembros();
    }

    private void pegarMembrosTurma()
    {
        membrosReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                recyclerView.getRecycledViewPool().clear();
                membrosAdapter.setQtdMembros((int) dataSnapshot.getChildrenCount());
                membrosAdapter.clear();

                for(final DataSnapshot data : dataSnapshot.getChildren())
                {
                    final String membroKey = data.getKey();

                    usuariosReference.child(membroKey).addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            Usuario membro = dataSnapshot.getValue(Usuario.class);
                            membro.setUid(membroKey);
                            membrosAdapter.add(membro);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}