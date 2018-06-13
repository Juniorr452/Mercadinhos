package com.mobile.pid.pid.home.turmas.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.mobile.pid.pid.home.PidSort;
import com.mobile.pid.pid.home.adapters.TurmaAdapter;
import com.mobile.pid.pid.home.turmas.NovaTurmaActivity;
import com.mobile.pid.pid.objetos.Turma;

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

    private TurmaAdapter turmaAdapter;
    private RecyclerView recyclerView;
    private List<Turma> turmasCriadas;

    private FloatingActionButton fabAdicionarTurma;

    private ValueEventListener turmasListener;

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
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        turmasCriadasRef = FirebaseDatabase.getInstance().getReference().child("userTurmasCriadas").child(uid);

        turmasListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
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
        };

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ordenar_turmas, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO: Sort Turmas criadas

        switch (item.getItemId())
        {
            case R.id.ordenar_alfabetica:
                progressBar.setVisibility(View.VISIBLE);
                conteudo.setVisibility(View.GONE);

                PidSort.insertionSort(turmasCriadas, Turma.compararPorNome);
                turmaAdapter.notifyDataSetChanged();
                break;

            case R.id.ordenar_data:
                progressBar.setVisibility(View.VISIBLE);
                conteudo.setVisibility(View.GONE);

                //turmaAdapter.ordenar(pidSort.ordenarTurmaDia());
                //Toast.makeText(getContext(), String.valueOf(pidSort.ordenarTurmaAlfabeto().size()), Toast.LENGTH_SHORT).show();
                break;
            default:
                return false;
        }

        progressBar.setVisibility(View.GONE);
        conteudo.setVisibility(View.VISIBLE);

        return true;

    }

    @Override
    public void onStart() {
        super.onStart();
        turmasCriadasRef.addValueEventListener(turmasListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        turmasCriadasRef.removeEventListener(turmasListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy < 0 && !fabAdicionarTurma.isShown())
                    fabAdicionarTurma.show();
                else if(dy > 0 && fabAdicionarTurma.isShown())
                    fabAdicionarTurma.hide();
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
