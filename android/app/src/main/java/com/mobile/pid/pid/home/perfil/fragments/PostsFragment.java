package com.mobile.pid.pid.home.perfil.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.pid.pid.AtualizarPerfilActivity;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.adapters.PostAdapter;
import com.mobile.pid.pid.home.feed.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment {

    private RecyclerView recycler_view_perfil;
    private List<Post> posts;


    public PostsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        posts = new ArrayList<Post>();

        posts.add(new Post("jonas", "teste"));
        posts.add(new Post("jonas", "teste"));
        posts.add(new Post("jonas", "teste"));
        posts.add(new Post("jonas", "teste"));
        posts.add(new Post("jonas", "teste"));

        recycler_view_perfil = (RecyclerView) view.findViewById(R.id.recycler_view_perfil);
        recycler_view_perfil.setLayoutManager(new LinearLayoutManager(getActivity()));

        recycler_view_perfil.setAdapter(new PostAdapter(getActivity(), posts));


        return view;
    }
}