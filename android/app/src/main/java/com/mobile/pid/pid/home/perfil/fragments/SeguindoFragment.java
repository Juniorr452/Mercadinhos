package com.mobile.pid.pid.home.perfil.fragments;

import android.os.Bundle;

import android.support.annotation.Nullable;
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
import com.mobile.pid.pid.home.adapters.FollowAdapter;
import com.mobile.pid.pid.objetos.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeguindoFragment extends Fragment {

    private RecyclerView recyclerView;
    private FollowAdapter followAdapter;


    private DatabaseReference db;
    private ValueEventListener seguindoListener;
    private String usuario;


    public SeguindoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usuario = getArguments().getString("usuario");
        followAdapter = new FollowAdapter(getActivity(), 1);

        db = FirebaseDatabase.getInstance().getReference("userSeguindo").child(usuario);

        seguindoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recyclerView.getRecycledViewPool().clear();
                followAdapter.clear();

                if(dataSnapshot.exists()) {

                    for(DataSnapshot data : dataSnapshot.getChildren()) {

                        final String uid = data.getKey();

                        FirebaseDatabase.getInstance().getReference("usuarios").child(uid)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Usuario user = dataSnapshot.getValue(Usuario.class);
                                        user.setUid(uid);
                                        followAdapter.add(user);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        db.addValueEventListener(seguindoListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        db.removeEventListener(seguindoListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seguindo, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_perfil_seguindo);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(followAdapter);

        return view;
    }
}
