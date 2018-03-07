package com.mobile.pid.pid.feed.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.login.RedesSociaisActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment
{
    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);

        AppCompatButton sair = v.findViewById(R.id.sair_button);

        sair.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Activity a = getActivity();

                FirebaseAuth.getInstance().signOut();
                a.startActivity(new Intent(a, RedesSociaisActivity.class));
                a.finish();
            }
        });

        return v;
    }
}
