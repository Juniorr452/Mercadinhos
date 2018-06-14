package com.mobile.pid.pid.home.perfil;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
//import android.support.design.widget.FloatingActionButton;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.perfil.fragments.CurtidasPerfilFragment;
import com.mobile.pid.pid.home.perfil.fragments.PostsFragment;
import com.mobile.pid.pid.home.perfil.fragments.SeguidoresFragment;
import com.mobile.pid.pid.home.perfil.fragments.SeguindoFragment;
import com.mobile.pid.pid.home.perfil.fragments.TurmasUsuarioFragment;
import com.mobile.pid.pid.login.RedesSociaisActivity;
import com.mobile.pid.pid.classes_e_interfaces.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */

// https://www.youtube.com/watch?v=BTYuLho5_rE COLLAPSING TOOLBAR
public class PerfilFragment extends Fragment {
    private static final String TAG = "PerfilFragment";
    private View view;


    // componentes
    private CollapsingToolbarLayout collapsing_perfil;
    private TabLayout tabLayout_perfil;
    private ViewPager viewPager_perfil;
    private PagerAdapter pageAdapter_perfil;
    private ImageView imageView_user;
    private FloatingActionMenu fab_menu;
    private FloatingActionButton fab_menu_edit;
    private FloatingActionButton fab_menu_signout;

    private Usuario user_logado;

    //firebase

    DatabaseReference db;
    private static String user_id;
    Usuario user;
    FirebaseAuth auth;

    private TextView count_followers;
    private TextView count_following;

    private DatabaseReference userRef;
    private DatabaseReference seguindoRef;
    private DatabaseReference seguidoresRef;

    private ValueEventListener userListener;
    private ValueEventListener seguidoresListener;
    private ValueEventListener seguindoListener;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference("usuarios").child(user_id);
        seguidoresRef = FirebaseDatabase.getInstance().getReference("userSeguidores").child(user_id);
        seguindoRef = FirebaseDatabase.getInstance().getReference("userSeguindo").child(user_id);

        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(Usuario.class);
                collapsing_perfil.setTitle(user.getNome());
                Glide.with(imageView_user).load(user.getFotoUrl()).into(imageView_user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        seguidoresListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    count_followers.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                else
                    count_followers.setText("0");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        seguindoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    count_following.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                else
                    count_following.setText("0");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    public void onStart(){
        super.onStart();

        userRef.addValueEventListener(userListener);
        seguidoresRef.addValueEventListener(seguidoresListener);
        seguindoRef.addValueEventListener(seguindoListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        userRef.removeEventListener(userListener);
        seguidoresRef.removeEventListener(seguidoresListener);
        seguindoRef.removeEventListener(seguindoListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_perfil, container, false);

        pageAdapter_perfil = new PerfilPageAdapter(getChildFragmentManager(), getContext());
        viewPager_perfil = view.findViewById(R.id.viewpager_perfil);
        tabLayout_perfil = view.findViewById(R.id.tab_perfil);
        imageView_user = view.findViewById(R.id.image_user);
        collapsing_perfil = view.findViewById(R.id.collapsing_tb);
        fab_menu = view.findViewById(R.id.fab_menu);
        fab_menu_edit = view.findViewById(R.id.fab_menu_edit);
        fab_menu_signout = view.findViewById(R.id.fab_menu_signout);
        count_followers = view.findViewById(R.id.count_followers);
        count_following = view.findViewById(R.id.count_following);


        viewPager_perfil.setAdapter(pageAdapter_perfil);
        tabLayout_perfil.setupWithViewPager(viewPager_perfil);

        // AO CLICAR NA FOTO DO USUARIO, CRIA UM DIALOG MOSTRANDO ELA EM TAMANHO REAL.
        imageView_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(getLayoutInflater().inflate(R.layout.image_fullsize, null));

                ImageView image_user_fullsize = (ImageView) dialog.findViewById(R.id.image_user_fullsize);

                Glide.with(getActivity()).load(user.getFotoUrl()).into(image_user_fullsize);
                dialog.show();

                image_user_fullsize.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        fab_menu_edit.setColorNormalResId(R.color.colorAccent);
        fab_menu_signout.setColorNormalResId(R.color.colorAccent);

        fab_menu_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_menu.close(true);
                startActivity(new Intent(getActivity(), AtualizarPerfilActivity.class));
            }
        });

        fab_menu_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context c = getActivity();

                fab_menu.close(true);
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(c, RedesSociaisActivity.class));
                getActivity().finish();
            }
        });

        viewPager_perfil.setOffscreenPageLimit(1);

        return view;
    }

    private String formatNumber(long number) {

        String numberString = "";
        if (Math.abs(number / 1000000) > 1)
            numberString = String.valueOf(number / 1000000).toString() + "M";
        else if (Math.abs(number / 1000) > 1)
            numberString = String.valueOf(number / 1000).toString() + "K";
        else
            numberString = String.valueOf(number);

        return numberString;
    }

    public static class PerfilPageAdapter extends FragmentPagerAdapter {
        private Context c;

        public PerfilPageAdapter(FragmentManager fm, Context c) {
            super(fm);
            this.c = c;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();
            bundle.putString("usuario", user_id);

            switch (position) {
                case 0:
                    PostsFragment p = new PostsFragment();
                    p.setArguments(bundle);
                    return p;
                case 1:
                    CurtidasPerfilFragment c = new CurtidasPerfilFragment();
                    c.setArguments(bundle);
                    return c;
                case 2:
                    TurmasUsuarioFragment t = new TurmasUsuarioFragment();
                    t.setArguments(bundle);
                    return t;
                case 3:
                    SeguindoFragment s = new SeguindoFragment();
                    s.setArguments(bundle);
                    return s;
                case 4:
                    SeguidoresFragment sf = new SeguidoresFragment();
                    sf.setArguments(bundle);
                    return sf;
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return c.getString(R.string.tab_perfil_posts);
                case 1:
                    return c.getString(R.string.tab_perfil_curtidos);
                case 2:
                    return c.getString(R.string.nav_classes);
                case 3:
                    return c.getString(R.string.following);
                case 4:
                    return c.getString(R.string.followers);
                default:
                    return null;
            }
        }

    }
}
