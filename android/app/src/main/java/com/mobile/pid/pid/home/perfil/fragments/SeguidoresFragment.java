package com.mobile.pid.pid.home.perfil.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.adapters.FollowAdapter;
import com.mobile.pid.pid.home.perfil.FollowItem;
import com.mobile.pid.pid.login.Usuario;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeguidoresFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<FollowItem> follow;
    private FollowAdapter followAdapter;
    private FollowItem item;
    private String usuario;

    private ValueEventListener followListener;
    private DatabaseReference followRef;


    public SeguidoresFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usuario = getArguments().getString("usuario");

        followAdapter = new FollowAdapter(getActivity(), 0);

        followRef = FirebaseDatabase.getInstance().getReference("userSeguidores").child(usuario);

        followListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recyclerView.getRecycledViewPool().clear();
                followAdapter.clear();

                if(dataSnapshot.exists()) {

                    for(DataSnapshot users : dataSnapshot.getChildren()) {
                        final String uid = users.getKey();

                        FirebaseDatabase.getInstance().getReference("usuarios").child(uid)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Usuario user = dataSnapshot.getValue(Usuario.class);
                                        item = new FollowItem(uid, user.getFotoUrl(), user.getNome());
                                        followAdapter.add(item);
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
        followRef.addValueEventListener(followListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        followRef.removeEventListener(followListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_seguidores, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_perfil_seguidores);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(followAdapter);

        return view;
    }
}
