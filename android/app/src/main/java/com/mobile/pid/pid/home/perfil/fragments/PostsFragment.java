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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.adapters.PostAdapter;
import com.mobile.pid.pid.home.feed.Post;
import com.mobile.pid.pid.login.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment {

    private List<Post> posts;

    private RecyclerView recyclerView;

    private String usuario;
    private DatabaseReference postsRef;
    private ChildEventListener postsChildListener;

    private PostAdapter postAdapter;

    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        usuario = getArguments().getString("usuario");

        posts = new ArrayList<>();
        postAdapter = new PostAdapter(getActivity(), posts);

        postsRef = FirebaseDatabase.getInstance().getReference("posts").child(usuario);
        postsChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    recyclerView.getRecycledViewPool().clear();
                    postAdapter.notifyDataSetChanged();

                    Post post = dataSnapshot.getValue(Post.class);
                    post.setId(dataSnapshot.getKey());
                    postAdapter.add(post);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                postAdapter.notifyDataSetChanged();
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