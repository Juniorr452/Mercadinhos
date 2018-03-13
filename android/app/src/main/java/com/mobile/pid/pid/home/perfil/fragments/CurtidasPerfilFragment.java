package com.mobile.pid.pid.home.perfil.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.pid.pid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurtidasPerfilFragment extends Fragment {

    private RecyclerView recyclerView_turmas_perfil;


    public CurtidasPerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_curtidas_perfil, container, false);

        recyclerView_turmas_perfil = (RecyclerView) view.findViewById(R.id.recycler_view_perfil);

        return view;
    }

}
