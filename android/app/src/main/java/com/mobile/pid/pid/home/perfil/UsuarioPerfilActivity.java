package com.mobile.pid.pid.home.perfil;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import com.mobile.pid.pid.login.Usuario;

public class UsuarioPerfilActivity extends AppCompatActivity {

    Usuario user;

    private ImageView foto;
    private TextView countPosts;
    private TextView countSeguidores;
    private TextView countSeguindo;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CollapsingToolbarLayout collapsing;
    private PagerAdapter adapter;
    private Toolbar toolbar;
    private FloatingActionButton fabSeguir;
    private static String usuario = "";

    DatabaseReference db;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_perfil);

        Intent i = getIntent();
         usuario = i.getStringExtra("usuario");

        foto = findViewById(R.id.image_user);
        countPosts = findViewById(R.id.count_posts);
        countSeguidores = findViewById(R.id.count_followers);
        countSeguindo = findViewById(R.id.count_following);
        collapsing = findViewById(R.id.collapsing_tb);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab);
        toolbar = findViewById(R.id.toolbar);
        fabSeguir = findViewById(R.id.fab_seguir);

        String usuarioLogado = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference("userSeguindo").child(usuarioLogado).child(usuario)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            setFollow();
                        } else {
                            setUnfollow();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        fabSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fabSeguir.getTag().equals("SEGUINDO")) {
                    setFollow();

                    Snackbar.make(findViewById(android.R.id.content), "Você está seguindo " + user.getNome(), Snackbar.LENGTH_SHORT).setAction("ClOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                } else {
                    setUnfollow();

                    Snackbar.make(findViewById(android.R.id.content), "Você deixou de seguir " + user.getNome(), Snackbar.LENGTH_SHORT).setAction("ClOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
                }
            }
        });


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new PerfilPageAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        db = FirebaseDatabase.getInstance().getReference("usuarios").child(usuario);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(Usuario.class);
                Glide.with(UsuarioPerfilActivity.this).load(user.getFotoUrl()).into(foto);
                collapsing.setTitle(user.getNome());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("posts").child(usuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countPosts.setText(formatNumber(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("userSeguidores").child(usuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countSeguidores.setText(formatNumber(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("userSeguindo").child(usuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countSeguindo.setText(formatNumber(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public static class PerfilPageAdapter extends FragmentPagerAdapter
    {
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
        public Fragment getItem(int position)
        {

            Bundle bundle = new Bundle();
            bundle.putString("usuario", usuario);


            switch(position)
            {
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
        public CharSequence getPageTitle(int position)
        {
            switch(position)
            {
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

    public void setFollow() {
        fabSeguir.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_green_dark));
        fabSeguir.setImageResource(R.drawable.seguindo);
        fabSeguir.setTag("SEGUIR");
    }

    public void setUnfollow() {
        fabSeguir.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
        fabSeguir.setImageResource(R.drawable.icon_seguir);
        fabSeguir.setTag("SEGUINDO");
    }
}
