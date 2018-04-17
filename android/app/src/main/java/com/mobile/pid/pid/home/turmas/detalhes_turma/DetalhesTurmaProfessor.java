package com.mobile.pid.pid.home.turmas.detalhes_turma;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.turmas.Turma;
import com.mobile.pid.pid.home.turmas.detalhes_turma.fragments.ChatsFragment;
import com.mobile.pid.pid.home.turmas.detalhes_turma.fragments.SolicitacoesFragment;
import com.mobile.pid.pid.home.turmas.detalhes_turma.fragments.TrabalhosFragment;

public class DetalhesTurmaProfessor extends AppCompatActivity {

    Turma turma;
    Toolbar toolbar_detalhes;
    ImageView capa;
    TextView nomeTurma;
    ImageView editar_nome;

    TextView qtdProfessores;
    TextView qtdAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_turma_professor);

        PagerAdapter detalhesPageAdapter  = new DetalheTurmasPageAdapter(this.getSupportFragmentManager());
        ViewPager detalhesViewPager       = findViewById(R.id.viewpager_turma);
        TabLayout turmasTabLayout         = findViewById(R.id.tab);
        Intent i                          = getIntent();
        toolbar_detalhes = findViewById(R.id.toolbar_detalhes);

        // Pegar os dados
        turma = i.getParcelableExtra("turma");

        toolbar_detalhes.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar_detalhes.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nomeTurma = findViewById(R.id.tv_turma_nome);
        capa      = findViewById(R.id.capa_detail);
        editar_nome = findViewById(R.id.edit_name_turma);

        // EDITAR NOME DA TURMA
        editar_nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetalhesTurmaProfessor.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_alterar_nome, null);
                builder.setView(mView);

                TextInputLayout TIL_alterar_nome = mView.findViewById(R.id.TIL_nome_turma);

                TIL_alterar_nome.getEditText().setText(turma.getNome());

                builder.setPositiveButton(R.string.change, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO alterar no banco todo o nome da turma
                    }
                });

                builder.setNegativeButton(R.string.cancel, null);

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#737373"));
            }
        });

        qtdProfessores = findViewById(R.id.qtd_professor);
        qtdAlunos      = findViewById(R.id.qtd_aluno);

        Glide.with(this).load(turma.getCapaUrl()).into(capa);

        nomeTurma.setText(turma.getNome());
        qtdProfessores.setText(turma.getQtdProfessores());
        qtdAlunos.setText(turma.getQtdAlunos());

        detalhesViewPager.setAdapter(detalhesPageAdapter);
        turmasTabLayout.setupWithViewPager(detalhesViewPager);
    }

    private class DetalheTurmasPageAdapter extends FragmentPagerAdapter
    {
        public DetalheTurmasPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
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
