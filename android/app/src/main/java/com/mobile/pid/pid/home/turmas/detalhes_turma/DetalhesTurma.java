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
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.turmas.Turma;
import com.mobile.pid.pid.home.turmas.detalhes_turma.fragments.ChatsFragment;
import com.mobile.pid.pid.home.turmas.detalhes_turma.fragments.SolicitacoesFragment;
import com.mobile.pid.pid.home.turmas.detalhes_turma.fragments.TrabalhosFragment;

public class DetalhesTurma extends AppCompatActivity
{

    private static final int PROFESSOR = 0;
    private static final int ALUNO = 1;

    Turma turma;;
    Toolbar toolbar_detalhes;
    ImageView capa;
    TextView  nomeTurma;
    ImageView editarTurma;
    ImageView imgProfessor;

    TextView qtdProfessores;
    TextView qtdAlunos;

    private int USUARIO;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_turma);

        PagerAdapter detalhesPageAdapter  = new DetalheTurmasPageAdapter(this.getSupportFragmentManager());
        ViewPager detalhesViewPager       = findViewById(R.id.viewpager_turma);
        TabLayout turmasTabLayout         = findViewById(R.id.tab);
        Intent i                          = getIntent();
        toolbar_detalhes                  = findViewById(R.id.toolbar_detalhes);
        editarTurma                       = findViewById(R.id.edit_turma);
        imgProfessor                      = findViewById(R.id.icon_turma_professor);

        // Pegar os dados
        turma = i.getParcelableExtra("turma");
        USUARIO = (int) i.getExtras().get("usuario");

        switch (USUARIO) {
            case PROFESSOR:
                editarTurma.setVisibility(View.VISIBLE);
                break;
            case ALUNO:
                editarTurma.setVisibility(View.GONE);
                break;
            default:
                break;
        }

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

        Glide.with(this).load(turma.getCapaUrl()).into(capa);
        Glide.with(this).load(turma.getProfessor().getFotoUrl()).into(imgProfessor);

        nomeTurma.setText(turma.getNome());
        qtdAlunos.setText(turma.getQtdAlunos());

        detalhesViewPager.setAdapter(detalhesPageAdapter);
        turmasTabLayout.setupWithViewPager(detalhesViewPager);
    }
    /*
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
            switch (USUARIO) {
                case PROFESSOR:
                    return 3;
                case ALUNO:
                    return 2;
                default:
                    return 0;
            }
        }

        @Override
        public Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    Bundle b = new Bundle();
                    b.putString("tid", turma.getUid());

                    Fragment chatsFragment = new ChatsFragment();
                    chatsFragment.setArguments(b);

                    return chatsFragment;

                case 1:
                    return new TrabalhosFragment();
                case 2:
                    return new SolicitacoesFragment();
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
                case 2:
                    return getString(R.string.solicitacoes);
                default:
                    return null;
            }
        }
    }
}
