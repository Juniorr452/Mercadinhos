package com.mobile.pid.pid.home.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.Post;
import com.mobile.pid.pid.home.PostAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment, AppCompatActivity {


    public FeedFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerViewfeed;
    private PostAdapter adapter;

    private List<Post> postList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        postList = new ArrayList<>();

        recyclerViewfeed = (RecyclerView) findViewById(R.id.recyclerViewFeed);
        recyclerViewfeed.setHasFixedSize(true);

        recyclerViewfeed.setLayoutManager(new LinearLayoutManager(this));

        postList.add(new Post(
                1,
                "jjonasramos",
                "Testando 1",
                R.drawable.icon_user
        ));

        postList.add(new Post(
                2,
                "eniojose",
                "Testando 2",
                R.drawable.icon_user
        ));

        postList.add(new Post(
                3,
                "kenniston",
                "Testando 3",
                R.drawable.icon_user
        ));

        adapter = new PostAdapter(this, postList);
        recyclerViewfeed.setAdapter(adapter);

        return inflater.inflate(R.layout.fragment_feed, container, false);
    }
}
