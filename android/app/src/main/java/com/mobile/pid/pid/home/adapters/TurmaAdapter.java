package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.turmas.Turma;
import com.mobile.pid.pid.home.turmas.detalhes_turma.DetalhesTurma;

import java.util.List;

/**
 * Created by junio on 12/03/2018.
 */

// TODO: Estudar essa p**** direito
public class TurmaAdapter extends RecyclerView.Adapter<TurmaAdapter.TurmaViewHolder>
{
    public static final int COD_BUSCAR_FRAGMENT = 0;
    public static final int COD_TURMAS_CRIADAS = 1;

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
        Turma t = listaTurmas.get(position);

        Glide.with(holder.capa.getContext())
                .load(t.getCapaUrl())
                .into(holder.capa);

        holder.nome.setText(t.getNome());

        // TODO: Dias
        holder.dia.setText("Dia");
    }

    public void add(Turma t)
    {
        listaTurmas.add(0, t);
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return listaTurmas.size();
    }

    public class TurmaViewHolder extends RecyclerView.ViewHolder //implements View.OnClickListener, View.OnLongClickListener
    {
        public ImageView capa;
        public TextView  nome;
        public TextView  dia;

        public TurmaViewHolder(final View itemView)
        {
            super(itemView);

            capa = itemView.findViewById(R.id.turma_capa);
            nome = itemView.findViewById(R.id.turma_nome);
            dia  = itemView.findViewById(R.id.turma_dia);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent i = new Intent(layoutInflater.getContext(), DetalhesTurma.class);
                    Turma  t = listaTurmas.get(getPosition());

                    i.putExtra("turma", t);

                    layoutInflater.getContext().startActivity(i);
                }
            });
        }
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
