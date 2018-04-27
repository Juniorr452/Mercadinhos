package com.mobile.pid.pid.home.turmas.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mobile.pid.pid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TurmasMatriculadasFragment extends Fragment
{
    ProgressBar progress;
    FloatingActionButton fab;
    // Turmas Matriculadas Código
    public TurmasMatriculadasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_turmas_matriculadas, container, false);

        progress = view.findViewById(R.id.pb_turmas_matriculadas);
        fab      = view.findViewById(R.id.fab_adicionar_turma);

        progress.setVisibility(View.GONE);
        return view;
    }
}
