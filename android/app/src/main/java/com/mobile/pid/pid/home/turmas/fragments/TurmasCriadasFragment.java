package com.mobile.pid.pid.home.turmas.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;

import com.mobile.pid.pid.home.adapters.TurmaAdapter;
import com.mobile.pid.pid.home.turmas.NovaTurmaActivity;
import com.mobile.pid.pid.home.turmas.Turma;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TurmasCriadasFragment extends Fragment
{
    private static final String TAG = "TurmasCriadasFragment";

    private ProgressBar progressBar;
    private FrameLayout conteudo;
    private ImageView sadFace;
    private TextView sadMessage;

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
        turmaAdapter = new TurmaAdapter(getActivity(), turmasCriadas, 1);

        // Pegar os dados de turmas criadas pelo usuário no db
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        turmasCriadasRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(uid).child("turmas_criadas");

        turmasCriadasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists())
                {
                    for(DataSnapshot dataTurma : dataSnapshot.getChildren())
                    {
                        Turma t = dataSnapshot.getValue(Turma.class);
                        t.setUid(dataSnapshot.getKey());

                        turmaAdapter.add(t);
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_turmas_criadas, container, false);

        recyclerView = v.findViewById(R.id.rv_turmas_criadas);
        progressBar = v.findViewById(R.id.pb_turmas_criadas);
        conteudo    = v.findViewById(R.id.fl_turmas_criadas);
        sadFace     = v.findViewById(R.id.sad_face);
        sadMessage  = v.findViewById(R.id.sad_message);
        conteudo.setVisibility(View.GONE);

        // Recycler View
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(turmaAdapter);

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
