package com.mobile.pid.pid.home.buscar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.adapters.BuscarAdapter;
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

    private DatabaseReference  turmasRef;
    private ChildEventListener turmasChildEventListener;

    private BuscarAdapter   buscarAdapter;
    private SugestaoAdapter sugestaoAdapter_turmas;
    private SugestaoAdapter sugestaoAdapter_usuarios;
    private ProgressBar     progressBar;
    private ScrollView      sv_usuarios_sugeridos;
    private RecyclerView    recyclerView_usuarios;
    private RecyclerView    recycle_busca;
    private Toolbar         toolbar;
    private RelativeLayout  sugestoes;
    private FrameLayout     busca;

    private String uid;

    public BuscarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        buscarAdapter = new BuscarAdapter(getActivity());

        sugestaoAdapter_usuarios = new SugestaoAdapter(getActivity());

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        carregarRecomendacoesUsuarios();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buscar, container, false);

        sv_usuarios_sugeridos = v.findViewById(R.id.sv_usuarios_sugeridos);
        progressBar           = v.findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.VISIBLE);
        sv_usuarios_sugeridos.setVisibility(View.GONE);

        // Recycler View
        recyclerView_usuarios = v.findViewById(R.id.rv_usuarios);
        recycle_busca         = v.findViewById(R.id.recycle_busca);
        //searchView = v.findViewById(R.id.search_view);

        //LinearLayoutManager llm2 = new LinearLayoutManager(getActivity());
        //llm2.setOrientation(LinearLayoutManager.HORIZONTAL);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        
        recyclerView_usuarios.setLayoutManager(gridLayoutManager);

        recyclerView_usuarios.setHasFixedSize(true);
        recycle_busca.setLayoutManager(new LinearLayoutManager(getActivity()));

        toolbar = v.findViewById(R.id.toolbar_buscar);
        sugestoes = v.findViewById(R.id.sugestoes);
        busca = v.findViewById(R.id.busca);

        recyclerView_usuarios.setAdapter(sugestaoAdapter_usuarios);
        recycle_busca.setAdapter(buscarAdapter);

        if(getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        }

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

                    sugestaoAdapter_usuarios.setSugestoes(usuariosRecomendados);
                    //sugestaoAdapter_usuarios.setSugestoes(new ArrayList<Object>(usuariosRecomendados));

                    progressBar.setVisibility(View.GONE);
                    sv_usuarios_sugeridos.setVisibility(View.VISIBLE);
                }
                else
                    Log.i(TAG, response.errorBody().toString());
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable erro)
            {
                Toast.makeText(getContext(), erro.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, erro.getMessage());
            }
        });
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_search, menu);

        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.menuSearch);

        SearchView searchView = (SearchView) item.getActionView();

        //searchView.setMaxWidth(getActivity().getWindowManager().getDefaultDisplay().getWidth());
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(final String query)
            {

                //TODO TA RETORNANDO SOMENTE AS TURMAS DUPLICADAS

                FirebaseDatabase.getInstance().getReference("usuarios").orderByChild("nome")
                    .addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {

                            buscarAdapter.clear();

                            if (dataSnapshot.exists())
                            {

                                for (DataSnapshot user : dataSnapshot.getChildren())
                                {
                                    Usuario u = user.getValue(Usuario.class);
                                    u.setUid(user.getKey());

                                    if(u.getNome().toLowerCase().contains(query.toLowerCase()))
                                        buscarAdapter.add(u);
                                }

                                sugestoes.setVisibility(View.GONE);
                                busca.setVisibility(View.VISIBLE);
                            }

                            sugestoes.setVisibility(View.GONE);
                            busca.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                FirebaseDatabase.getInstance().getReference("turmas").orderByChild("nome")
                    .addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            if (dataSnapshot.exists())
                            {

                                for (DataSnapshot turma : dataSnapshot.getChildren())
                                {
                                    Turma t = turma.getValue(Turma.class);
                                    t.setId(turma.getKey());

                                    if(t.getNome().toLowerCase().contains(query.toLowerCase()))
                                        buscarAdapter.add(t);

                                }

                                sugestoes.setVisibility(View.GONE);
                                busca.setVisibility(View.VISIBLE);
                            }

                            sugestoes.setVisibility(View.GONE);
                            busca.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                buscarAdapter.clear();

                sugestoes.setVisibility(View.VISIBLE);
                busca.setVisibility(View.GONE);
                return false;
            }
        });
    }
}
