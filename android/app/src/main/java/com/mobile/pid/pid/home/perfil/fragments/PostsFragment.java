package com.mobile.pid.pid.home.perfil.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.adapters.PostAdapter;
import com.mobile.pid.pid.home.feed.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment {

    private List<Post> posts;

    private RecyclerView recyclerView;

    private FirebaseUser usuario;
    private DatabaseReference postsRef;
    private ChildEventListener postsChildListener;

    private PostAdapter postAdapter;

    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        posts = new ArrayList<>();
        postAdapter = new PostAdapter(getActivity(), posts);
        usuario = FirebaseAuth.getInstance().getCurrentUser();

        postsRef = FirebaseDatabase.getInstance().getReference("usuarios").child(usuario.getUid()).child("posts");
        postsChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                recyclerView.getRecycledViewPool().clear();
                postAdapter.notifyDataSetChanged();

                Post post = dataSnapshot.getValue(Post.class);
                post.setId(dataSnapshot.getKey());
                postAdapter.add(post);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                postAdapter.notifyDataSetChanged();
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

        postsRef.addChildEventListener(postsChildListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_perfil);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setAdapter(postAdapter);

        return view;
    }
}