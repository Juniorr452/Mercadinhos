package com.mobile.pid.pid.home.perfil.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.adapters.PostAdapter;
import com.mobile.pid.pid.home.feed.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurtidasPerfilFragment extends Fragment {

    private PostAdapter postAdapter;
    private String usuario;

    private RecyclerView recyclerView;
    private List<Post> posts;

    private Query likesRef;
    private ChildEventListener likesListener;



    public CurtidasPerfilFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usuario = getArguments().getString("usuario");

        posts = new ArrayList<>();
        postAdapter = new PostAdapter(getActivity(), posts);

        likesRef = FirebaseDatabase.getInstance().getReference("userLikes").child(usuario).orderByChild("postData");

        /*likesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postAdapter.clear();
                postAdapter.notifyDataSetChanged();

                if(dataSnapshot.exists()) {
                    for(DataSnapshot data : dataSnapshot.getChildren()) {
                        Post post = data.getValue(Post.class);
                        post.setId(data.getKey());
                        postAdapter.add(post);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };*/

        likesListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                recyclerView.getRecycledViewPool().clear();
                recyclerView.setItemViewCacheSize(0);
                postAdapter.notifyDataSetChanged();

                //TODO DATA DO POST TA VINDO ERRADA
                Post post = dataSnapshot.getValue(Post.class);
                post.setId(dataSnapshot.getKey());
                postAdapter.add(post);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                recyclerView.getRecycledViewPool().clear();

                Post p = dataSnapshot.getValue(Post.class);
                p.setId(dataSnapshot.getKey());
                postAdapter.removePost(p);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        postAdapter.clear();
        likesRef.addChildEventListener(likesListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        likesRef.removeEventListener(likesListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_curtidas_perfil, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_turmas_perfil);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(postAdapter);

        return view;
    }

}
