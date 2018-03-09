package com.mobile.pid.pid.home.perfil;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.pid.pid.R;

/**
 * A simple {@link Fragment} subclass.
 */

// https://www.youtube.com/watch?v=BTYuLho5_rE COLLAPSING TOOLBAR
public class PerfilFragment extends Fragment {

    private CollapsingToolbarLayout collapsing_tb;


    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container ,false);

        collapsing_tb = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_tb);
        collapsing_tb.setTitle("Jonas Ramos"); //TODO pegar nome do usuario e colocar aqui

        return view;
    }

}
