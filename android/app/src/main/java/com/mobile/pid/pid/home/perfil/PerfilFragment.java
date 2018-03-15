package com.mobile.pid.pid.home.perfil;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.UsuarioLogado;
import com.mobile.pid.pid.home.perfil.fragments.CurtidasPerfilFragment;
import com.mobile.pid.pid.home.perfil.fragments.PostsFragment;
import com.mobile.pid.pid.home.perfil.fragments.SeguidoresFragment;
import com.mobile.pid.pid.home.perfil.fragments.SeguindoFragment;
import com.mobile.pid.pid.login.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */

// https://www.youtube.com/watch?v=BTYuLho5_rE COLLAPSING TOOLBAR
public class PerfilFragment extends Fragment {

    // componentes
    private CollapsingToolbarLayout collapsing_perfil;
    private TabLayout tabLayout_perfil;
    private ViewPager viewPager_perfil;
    private PagerAdapter pageAdapter_perfil;
    private ImageView imageView_user;

    //firebase
    private FirebaseAuth auth;
    private FirebaseUser user_logged;
    private DatabaseReference user_database;
    private String user_id;


    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, container ,false);

        pageAdapter_perfil   = new PerfilPageAdapter(getChildFragmentManager());
        viewPager_perfil     = view.findViewById(R.id.viewpager_perfil);
        tabLayout_perfil     = (TabLayout) view.findViewById(R.id.tab_perfil);
        imageView_user       = (ImageView) view.findViewById(R.id.image_user);

        // FIREBASE - PEGAR OS DADOS DO USUARIO LOGADO
        auth = FirebaseAuth.getInstance();
        user_logged = auth.getCurrentUser();
        user_id = user_logged.getUid();

        user_database = FirebaseDatabase.getInstance().getReference().child("usuarios").child(user_id);

        user_database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UsuarioLogado.user = dataSnapshot.getValue(Usuario.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // -------------------------------------------
        viewPager_perfil.setAdapter(pageAdapter_perfil);
        tabLayout_perfil.setupWithViewPager(viewPager_perfil);

        collapsing_perfil = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_tb);
        collapsing_perfil.setTitle(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        //collapsing_perfil.setTitle(UsuarioLogado.user.getNome()); //TODO PEGAR NOME DO USUARIO DO BANCO


        // AO CLICAR NA FOTO DO USUARIO, CRIA UM DIALOG MOSTRANDO ELA EM TAMANHO REAL.
        imageView_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(getLayoutInflater().inflate(R.layout.image_fullsize, null));

                ImageView image_user_fullsize = (ImageView) dialog.findViewById(R.id.image_user_fullsize);

                image_user_fullsize.setImageDrawable(imageView_user.getDrawable());
                dialog.show();

                image_user_fullsize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { dialog.dismiss(); }
                });
            }
        });


        return view;
    }

    private class PerfilPageAdapter extends FragmentPagerAdapter
    {
        public PerfilPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    return new PostsFragment();
                case 1:
                    return new CurtidasPerfilFragment();
                case 2:
                    return new SeguindoFragment();
                case 3:
                    return new SeguidoresFragment();
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position)
        {
            switch(position)
            {
                case 0:
                    return getString(R.string.tab_perfil_posts);
                case 1:
                    return getString(R.string.tab_perfil_curtidos);
                case 2:
                    return getString(R.string.following);
                case 3:
                    return getString(R.string.followers);
                default:
                    return null;
            }
        }
    }
}
