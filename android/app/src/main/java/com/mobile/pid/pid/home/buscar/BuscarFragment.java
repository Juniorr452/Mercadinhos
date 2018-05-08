package com.mobile.pid.pid.home.buscar;


import android.os.Bundle;
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
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.adapters.SugestaoAdapter;
import com.mobile.pid.pid.home.adapters.TurmaAdapter;
import com.mobile.pid.pid.home.turmas.Turma;
import com.mobile.pid.pid.login.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuscarFragment extends Fragment
{
    private static final String TAG = "BuscarFragment";

    private DatabaseReference turmasRef;
    private ChildEventListener turmasChildEventListener;

    private TurmaAdapter turmaAdapter;
    private SugestaoAdapter sugestaoAdapter_turmas;
    private SugestaoAdapter sugestaoAdapter_usuarios;
    private RecyclerView recyclerView_turmas;
    private RecyclerView recyclerView_usuarios;
    private List<Turma> turmasCriadas;

    // TODO: Código turmas criadas
    public BuscarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        turmasCriadas = new ArrayList<>();
        turmaAdapter = new TurmaAdapter(getActivity(), turmasCriadas, 0);
        sugestaoAdapter_turmas = new SugestaoAdapter(getActivity());
        sugestaoAdapter_usuarios = new SugestaoAdapter(getActivity());

        turmasRef = FirebaseDatabase.getInstance().getReference().child("turmas");
        turmasChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Turma t = dataSnapshot.getValue(Turma.class);
                t.setId(dataSnapshot.getKey());
                sugestaoAdapter_turmas.add(t);
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

        turmasRef.addChildEventListener(turmasChildEventListener);

        FirebaseDatabase.getInstance().getReference("usuarios").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Usuario u = dataSnapshot.getValue(Usuario.class);
                u.setUid(dataSnapshot.getKey());
                sugestaoAdapter_usuarios.add(u);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buscar, container, false);

        // Recycler View
        recyclerView_turmas = v.findViewById(R.id.rv_turmas);
        recyclerView_usuarios = v.findViewById(R.id.rv_usuarios);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        LinearLayoutManager llm2 = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        llm2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_turmas.setLayoutManager(llm);
        recyclerView_usuarios.setLayoutManager(llm2);

        recyclerView_turmas.setAdapter(sugestaoAdapter_turmas);
        recyclerView_usuarios.setAdapter(sugestaoAdapter_usuarios);

        turmaAdapter.setUid("123123");

        return v;
    }
}
