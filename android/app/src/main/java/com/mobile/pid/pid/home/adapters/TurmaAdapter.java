package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.turmas.InfoUsuario;
import com.mobile.pid.pid.home.turmas.Turma;
import com.mobile.pid.pid.home.turmas.detalhes_turma.DetalhesTurma;

import java.util.List;
import java.util.Map;

/**
 * Created by junio on 12/03/2018.
 */

// TODO: Estudar essa p**** direito
public class TurmaAdapter extends RecyclerView.Adapter<TurmaAdapter.TurmaViewHolder>
{

    private static final int COD_TURMAS_MATRICULADAS = 0;
    private static final int COD_TURMAS_CRIADAS = 1;
    private static final int PROFESSOR = 0;
    private static final int ALUNO = 1;

    private List<Turma>    listaTurmas;
    private LayoutInflater layoutInflater;
    private int COD_CONTEXT;
    private String Uid;


    public TurmaAdapter(Context c, List<Turma> l, int COD_CONTEXT)
    {
        listaTurmas    = l;
        layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.COD_CONTEXT = COD_CONTEXT;
    }

    @Override
    public TurmaViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = layoutInflater.inflate(R.layout.item_turma, parent, false);

        TurmaViewHolder vh = new TurmaViewHolder(v);

        return vh;
    }

    // Setar os dados da lista às views
    @Override
    public void onBindViewHolder(TurmaViewHolder holder, final int position)
    {
        final Turma t = listaTurmas.get(position);

        Glide.with(holder.capa.getContext())
                .load(t.getCapaUrl())
                .into(holder.capa);

        holder.nome.setText(t.getNome());

        holder.opcoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(layoutInflater.getContext())
                    .setTitle("Excluir a turma?")
                    .setMessage("Deseja realmente excluir a turma?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            DatabaseReference rootRef     = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference usuariosRef = rootRef.child("usuarios");
                            String tuid = t.getUid();

                            // Deletar turma.
                            rootRef.child("turmas")
                                .child(tuid)
                                .removeValue();

                            // Deletar no turmas_criadas do professor.
                            usuariosRef.child(t.getProfessor().getUid())
                                .child("turmas_criadas")
                                .child(tuid)
                                .removeValue();

                            // Deletar no turmas_matriculadas dos alunos.
                            /*for(InfoUsuario a : t.getAlunos())
                                usuariosRef.child(a.getUid())
                                    .child("turmas_matriculadas")
                                    .child(tuid)
                                    .removeValue();*/
                        }
                    })
                    .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
            }
        });

        // DIAS DA SEMANA
        Map<String, Integer> dias = t.getDiasDaSemana();
        String dia = "";

        for (Map.Entry<String, Integer> entry: dias.entrySet())
            if(dia.isEmpty())
                dia = entry.getKey();
            else
                dia += " - " + entry.getKey();

        holder.dia.setText(dia);
    }

    public void add(Turma t)
    {
        listaTurmas.add(0, t);
    }

    public List<Turma> getLista() { return listaTurmas; }

    @Override
    public int getItemCount() {
        return listaTurmas.size();
    }

    public class TurmaViewHolder extends RecyclerView.ViewHolder //implements View.OnClickListener, View.OnLongClickListener
    {
        public ImageView capa;
        public ImageView opcoes;
        public TextView  nome;
        public TextView  dia;

        public TurmaViewHolder(final View itemView)
        {
            super(itemView);

            capa   = itemView.findViewById(R.id.turma_capa);
            opcoes = itemView.findViewById(R.id.turma_opcoes);
            nome   = itemView.findViewById(R.id.turma_nome);
            dia    = itemView.findViewById(R.id.turma_dia);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    Turma  t = listaTurmas.get(getPosition());
                    Intent i = new Intent(layoutInflater.getContext(), DetalhesTurma.class);

                    i.putExtra("turma", t);

                    switch(COD_CONTEXT)
                    {
                        case COD_TURMAS_CRIADAS:
                            i.putExtra("usuario", PROFESSOR);
                            break;
                        case COD_TURMAS_MATRICULADAS:
                            i.putExtra("usuario", ALUNO);
                            break;
                        default:
                            break;
                    }

                    layoutInflater.getContext().startActivity(i);
                }
            });
        }
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
