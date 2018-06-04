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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.turmas.Turma;
import com.mobile.pid.pid.home.turmas.detalhes_turma.fragments.AvisosFragment;
import com.mobile.pid.pid.home.turmas.detalhes_turma.fragments.ChatsFragment;
import com.mobile.pid.pid.home.turmas.detalhes_turma.fragments.SolicitacoesFragment;
import com.mobile.pid.pid.login.Usuario;

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

    TextView tvQtdAlunos;
    TextView tvAlunos;

    private int USUARIO;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_turma);

        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference().child("usuarios");

        PagerAdapter detalhesPageAdapter  = new DetalheTurmasPageAdapter(this.getSupportFragmentManager());
        ViewPager detalhesViewPager       = findViewById(R.id.viewpager_turma);
        TabLayout turmasTabLayout         = findViewById(R.id.tab);
        Intent i                          = getIntent();
        toolbar_detalhes                  = findViewById(R.id.toolbar_detalhes);
        editarTurma                       = findViewById(R.id.edit_turma);
        imgProfessor                      = findViewById(R.id.icon_turma_professor);

        // Pegar os dados
        turma = (Turma) i.getSerializableExtra("turma");
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

        tvQtdAlunos = findViewById(R.id.qtd_aluno);
        tvAlunos    = findViewById(R.id.tv_detalhes_turma_alunos);

        Glide.with(this).load(turma.getCapaUrl()).into(capa);

        usuariosRef.child(turma.getProfessorUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Usuario professor = dataSnapshot.getValue(Usuario.class);
                Glide.with(getApplicationContext()).load(professor.getFotoUrl()).into(imgProfessor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        nomeTurma.setText(turma.getNome());

        int qtdAlunos = turma.getQtdAlunos();
        if(qtdAlunos < 2)
            tvAlunos.setText(R.string.aluno);

        tvQtdAlunos.setText(Integer.toString(qtdAlunos));

        detalhesViewPager.setAdapter(detalhesPageAdapter);
        detalhesViewPager.setOffscreenPageLimit(4);

        turmasTabLayout.setupWithViewPager(detalhesViewPager);
    }

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
            Bundle b = new Bundle();

            b.putSerializable("turma", turma);
            b.putInt("usuario", USUARIO);

            switch(position)
            {
                case 0:
                    Fragment chatsFragment = new ChatsFragment();
                    chatsFragment.setArguments(b);

                    return chatsFragment;
                case 1:
                    Fragment avisosFragment = new AvisosFragment();
                    avisosFragment.setArguments(b);

                    return avisosFragment;
                case 2:
                    Fragment solicitacoesFragment = new SolicitacoesFragment();
                    solicitacoesFragment.setArguments(b);

                    return solicitacoesFragment;
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
                    return getString(R.string.avisos);
                case 2:
                    return getString(R.string.solicitacoes);
                default:
                    return null;
            }
        }
    }
}
