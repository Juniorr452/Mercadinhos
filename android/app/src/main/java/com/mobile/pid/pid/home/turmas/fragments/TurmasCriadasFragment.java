package com.mobile.pid.pid.home.turmas.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobile.pid.pid.R;

import com.mobile.pid.pid.adapters.TurmaAdapter;
import com.mobile.pid.pid.turmas.NovaTurmaActivity;
import com.mobile.pid.pid.turmas.Turma;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TurmasCriadasFragment extends Fragment
{
    private static final String TAG = "TurmasCriadasFragment";

    private DatabaseReference turmasCriadasRef;
    private ChildEventListener turmasCriadasChildEventListener;

    private TurmaAdapter turmaAdapter;
    private RecyclerView recyclerView;
    private List<Turma> turmasCriadas;

    private FloatingActionButton fabAdicionarTurma;

    // TODO: Código turmas criadas
    public TurmasCriadasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        turmasCriadas = new ArrayList<>();
        turmaAdapter = new TurmaAdapter(getActivity(), turmasCriadas);

        // Pegar os dados de turmas criadas pelo usuário no db
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        turmasCriadasRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(uid).child("turmas_criadas");
        turmasCriadasChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                turmaAdapter.add(dataSnapshot.getValue(Turma.class));
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

        turmasCriadasRef.addChildEventListener(turmasCriadasChildEventListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_turmas_criadas, container, false);

        // Recycler View
        recyclerView = v.findViewById(R.id.rv_turmas_criadas);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(turmaAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        // FAB
        fabAdicionarTurma = v.findViewById(R.id.fab_adicionar_turma);
        fabAdicionarTurma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), NovaTurmaActivity.class));
            }
        });
        return v;
    }
}
