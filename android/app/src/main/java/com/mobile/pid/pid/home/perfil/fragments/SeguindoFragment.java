package com.mobile.pid.pid.home.perfil.fragments;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.adapters.FollowAdapter;
import com.mobile.pid.pid.home.perfil.FollowItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeguindoFragment extends Fragment {

    private RecyclerView recyclerView_seguindo;
    private List<FollowItem> follow;
    private Button btn_follow;


    public SeguindoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seguindo, container, false);

        recyclerView_seguindo = (RecyclerView) view.findViewById(R.id.recycler_view_perfil_seguindo);
        btn_follow = (Button) view.findViewById(R.id.btn_follow);

        follow = new ArrayList<FollowItem>();

        follow.add(new FollowItem(null, "Jonas Ramos", "jonasramos"));


        recyclerView_seguindo.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_seguindo.setAdapter(new FollowAdapter(getActivity(), follow, 1));

        return view;
    }

    public List<FollowItem> getFollow() {
        return follow;
    }
}
