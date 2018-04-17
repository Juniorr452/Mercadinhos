package com.mobile.pid.pid.home.turmas.detalhes_turma;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.turmas.Turma;
import com.mobile.pid.pid.home.turmas.detalhes_turma.fragments.ChatsFragment;
import com.mobile.pid.pid.home.turmas.detalhes_turma.fragments.TrabalhosFragment;

public class DetalhesTurmaAluno extends AppCompatActivity
{
    Turma turma;
    Toolbar toolbar_detalhes;
    ImageView capa;
    TextView  nomeTurma;

    TextView qtdProfessores;
    TextView qtdAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_turma_aluno);

        PagerAdapter detalhesPageAdapter  = new DetalheTurmasPageAdapter(this.getSupportFragmentManager());
        ViewPager detalhesViewPager       = findViewById(R.id.viewpager_turma);
        TabLayout turmasTabLayout         = findViewById(R.id.tab);
        Intent i                          = getIntent();
        toolbar_detalhes = findViewById(R.id.toolbar_detalhes);

        toolbar_detalhes.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar_detalhes.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nomeTurma = findViewById(R.id.tv_turma_nome);
        capa      = findViewById(R.id.capa_detail);

        qtdProfessores = findViewById(R.id.qtd_professor);
        qtdAlunos      = findViewById(R.id.qtd_aluno);

        // Pegar os dados
        turma = i.getParcelableExtra("turma");

        Glide.with(this).load(turma.getCapaUrl()).into(capa);

        nomeTurma.setText(turma.getNome());
        qtdProfessores.setText(turma.getQtdProfessores());
        qtdAlunos.setText(turma.getQtdAlunos());

        detalhesViewPager.setAdapter(detalhesPageAdapter);
        turmasTabLayout.setupWithViewPager(detalhesViewPager);
    }/*

    private class DetalheTurmasPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return false;
        }
    }*/

    // Tabs and ViewPager - https://www.youtube.com/watch?v=zQekzaAgIlQ
    private class DetalheTurmasPageAdapter extends FragmentPagerAdapter
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
    }
}
