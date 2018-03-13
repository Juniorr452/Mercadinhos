package com.mobile.pid.pid.home.feed;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.pid.pid.R;

import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

// https://www.youtube.com/watch?v=4mwnhvRzRfw LINK PRA USAR RECYCLER/CARD/FRAGMENT
public class FeedFragment extends Fragment {

    private List<Post> posts;


    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        posts = new ArrayList<Post>();

        posts.add(new Post("jonas", "teste"));
        posts.add(new Post("jonas", "teste"));
        posts.add(new Post("jonas", "teste"));
        posts.add(new Post("jonas", "teste"));
        posts.add(new Post("jonas", "teste"));

        RecyclerView recycler_view = view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        recycler_view.setAdapter(new FeedAdapter(getActivity(), posts));

        return view;

    }
}
