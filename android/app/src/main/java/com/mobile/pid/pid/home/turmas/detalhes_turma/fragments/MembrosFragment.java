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
import com.mobile.pid.pid.home.turmas.Turma;
import com.mobile.pid.pid.objetos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MembrosFragment extends Fragment
{
    DatabaseReference firebaseDatabase;
    DatabaseReference alunosReference;
    DatabaseReference usuariosReference;
    DatabaseReference professorReference;

    Turma   turma;
    Usuario professor;

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
        alunosReference    = firebaseDatabase.child("turmas").child(turma.getId()).child("alunos");
        usuariosReference  = firebaseDatabase.child("usuarios");
        professorReference = usuariosReference.child(turma.getProfessorUid());

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

        membrosAdapter.ordenarMembros();
    }

    private void pegarMembrosTurma()
    {
        professorReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                professor = dataSnapshot.getValue(Usuario.class);
                professor.setUid(dataSnapshot.getKey());

                membrosAdapter.add(professor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        alunosReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                final List<Usuario> alunos = new ArrayList<>(30);

                for(final DataSnapshot data : dataSnapshot.getChildren())
                {
                    String alunoKey = data.getKey();

                    usuariosReference.child(alunoKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            Usuario aluno = dataSnapshot.getValue(Usuario.class);
                            aluno.setUid(dataSnapshot.getKey());
                            membrosAdapter.add(aluno);
                            //.add(aluno);
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