package com.mobile.pid.pid.home.perfil.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.adapters.TurmaAdapter;
import com.mobile.pid.pid.home.turmas.Turma;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TurmasUsuarioFragment extends Fragment
{
    private ProgressBar progressBar;
    private FrameLayout conteudo;
    private ImageView sadFace;
    private TextView sadMessage;

    private DatabaseReference turmasUsuarioRef;

    private TurmaAdapter turmaAdapter;
    private RecyclerView recyclerView;
    private List<Turma> turmasUsuario;

    public TurmasUsuarioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        turmasUsuario = new ArrayList<>();
        turmaAdapter = new TurmaAdapter(getActivity(), turmasUsuario);

        final String uid = getArguments().getString("usuario"); //FirebaseAuth.getInstance().getCurrentUser().getUid();

        turmasUsuarioRef = FirebaseDatabase.getInstance().getReference().child("userTurmasCriadas").child(uid);

        turmasUsuarioRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    recyclerView.getRecycledViewPool().clear();
                    turmaAdapter.clear();

                    for(DataSnapshot dataTurma : dataSnapshot.getChildren())
                    {
                        String tuid = dataTurma.getKey();
                        FirebaseDatabase.getInstance().getReference()
                            .child("turmas")
                            .child(tuid)
                            .addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshotTurma)
                                {
                                    Turma t = dataSnapshotTurma.getValue(Turma.class);
                                    t.setId(dataSnapshotTurma.getKey());

                                    turmaAdapter.add(t);
                                    turmaAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
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
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_turmas_usuario, container, false);

        recyclerView = v.findViewById(R.id.rv_turmas_usuario);
        progressBar  = v.findViewById(R.id.pb_turmas_usuario);
        conteudo     = v.findViewById(R.id.fl_turmas_usuario);
        sadFace      = v.findViewById(R.id.sad_face);
        sadMessage   = v.findViewById(R.id.sad_message);
        conteudo.setVisibility(View.GONE);

        // Recycler View
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(turmaAdapter);

        return v;
    }
}