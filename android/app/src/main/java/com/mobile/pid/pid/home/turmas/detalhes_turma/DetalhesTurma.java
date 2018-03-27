package com.mobile.pid.pid.home.turmas.detalhes_turma;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.turmas.detalhes_turma.fragments.ChatsFragment;
import com.mobile.pid.pid.home.turmas.detalhes_turma.fragments.TrabalhosFragment;

public class DetalhesTurma extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_turma);

        //PagerAdapter turmasPageAdapter    = new DetalheTurmasPageAdapter();
        ViewPager turmasViewPager      = findViewById(R.id.viewpager_turma);
        TabLayout turmasTabLayout      = findViewById(R.id.tab);

        //turmasViewPager.setAdapter(turmasPageAdapter);
        //turmasTabLayout.setupWithViewPager(turmasViewPager);


    }

    // Tabs and ViewPager - https://www.youtube.com/watch?v=zQekzaAgIlQ
    /*private class DetalheTurmasPageAdapter extends FragmentPagerAdapter
    {
        public DetalheTurmasPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    return new ChatsFragment();
                case 1:
                    return new TrabalhosFragment();
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
                    return getString(R.string.chats);
                case 1:
                    return getString(R.string.trabalhos);
                default:
                    return null;
            }
        }
    }*/
}
