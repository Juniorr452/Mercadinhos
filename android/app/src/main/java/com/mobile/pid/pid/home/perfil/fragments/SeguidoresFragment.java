package com.mobile.pid.pid.home.perfil.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.adapters.FollowAdapter;
import com.mobile.pid.pid.home.perfil.FollowItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeguidoresFragment extends Fragment {

    private RecyclerView recyclerView_seguidores;
    private List<FollowItem> follow;


    public SeguidoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_seguidores, container, false);

        recyclerView_seguidores = (RecyclerView) view.findViewById(R.id.recycler_view_perfil_seguidores);

        follow = new ArrayList<FollowItem>();

        follow.add(new FollowItem(null, "Jonas Ramos", "jonasramos"));


        recyclerView_seguidores.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_seguidores.setAdapter(new FollowAdapter(getActivity(), follow, 2));

        return view;
    }

    public List<FollowItem> getFollow() {
        return follow;
    }

    private List<FollowItem> sort(List<FollowItem> lista) {



        return lista;
    }
}
