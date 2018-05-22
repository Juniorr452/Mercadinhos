package com.mobile.pid.pid.home.buscar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
import com.mobile.pid.pid.login.UsuarioService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuscarFragment extends Fragment
{
    private static final String TAG = "BuscarFragment";

    private static final String URL_BASE_RETROFIT = "https://us-central1-pi-ii-2920c.cloudfunctions.net/";

    private UsuarioService usuarioService;

    private DatabaseReference turmasRef;
    private ChildEventListener turmasChildEventListener;

    private TurmaAdapter turmaAdapter;
    private SugestaoAdapter sugestaoAdapter_turmas;
    private SugestaoAdapter sugestaoAdapter_usuarios;
    private RecyclerView recyclerView_turmas;
    private RecyclerView recyclerView_usuarios;
    private List<Turma> turmasCriadas;

    private String uid;

    // TODO: Código turmas criadas
    public BuscarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        turmasCriadas = new ArrayList<>();
        turmaAdapter = new TurmaAdapter(getActivity(), turmasCriadas);
        sugestaoAdapter_turmas = new SugestaoAdapter(getActivity());
        sugestaoAdapter_usuarios = new SugestaoAdapter(getActivity());

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

        /*FirebaseDatabase.getInstance().getReference("usuarios").addChildEventListener(new ChildEventListener() {
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
        });*/

    }

    @Override
    public void onStart() {
        super.onStart();

        carregarRecomendacoesUsuarios();
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

        return v;
    }


    public void carregarRecomendacoesUsuarios()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URL_BASE_RETROFIT)
                .build();

        usuarioService = retrofit.create(UsuarioService.class);

        Call<List<Usuario>> requisicao = usuarioService.getRecomendacoesUsuarios(uid);

        requisicao.enqueue(new Callback<List<Usuario>>()
        {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response)
            {
                if(response.isSuccessful())
                {
                    List<Usuario> usuariosRecomendados = response.body();

                    sugestaoAdapter_usuarios.setSugestoes(new ArrayList<Object>(usuariosRecomendados));
                }
                else
                    Log.i(TAG, "Merda");
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable erro)
            {
                Toast.makeText(getContext(), erro.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, erro.getMessage());
            }
        });
    }
}
