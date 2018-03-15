package com.mobile.pid.pid.home.perfil;


import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.perfil.fragments.CurtidasPerfilFragment;
import com.mobile.pid.pid.home.perfil.fragments.PostsFragment;
import com.mobile.pid.pid.home.perfil.fragments.SeguidoresFragment;
import com.mobile.pid.pid.home.perfil.fragments.SeguindoFragment;


import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */

// https://www.youtube.com/watch?v=BTYuLho5_rE COLLAPSING TOOLBAR
public class PerfilFragment extends Fragment {

    private CollapsingToolbarLayout collapsing_tb;
    private TabLayout tabs;
    private ViewPager perfilViewPager;
    private PagerAdapter perfilPageAdapter;
    private ImageView image_collapse;


    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, container ,false);

        perfilPageAdapter    = new PerfilPageAdapter(getChildFragmentManager());
        perfilViewPager      = view.findViewById(R.id.viewpager_perfil);
        tabs                 = (TabLayout) view.findViewById(R.id.tab_perfil);
        image_collapse = (ImageView) view.findViewById(R.id.image_collapse);

        perfilViewPager.setAdapter(perfilPageAdapter);
        tabs.setupWithViewPager(perfilViewPager);

        collapsing_tb = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_tb);
        collapsing_tb.setTitle(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        
        image_collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO mostrar a imagem como um pop-up
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
