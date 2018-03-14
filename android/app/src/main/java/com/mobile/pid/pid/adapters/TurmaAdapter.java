package com.mobile.pid.pid.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.feed.Turma;

import java.util.List;

/**
 * Created by junio on 12/03/2018.
 */

// TODO: Estudar essa p**** direito
public class TurmaAdapter extends RecyclerView.Adapter<TurmaAdapter.TurmaViewHolder>
{
    private List<Turma>    listaTurmas;
    private LayoutInflater layoutInflater;

    public TurmaAdapter(Context c, List<Turma> l)
    {
        listaTurmas    = l;
        layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public void onBindViewHolder(TurmaViewHolder holder, int position)
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

    public class TurmaViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView capa;
        public TextView  nome;
        public TextView  dia;

        public TurmaViewHolder(View itemView)
        {
            super(itemView);

            capa = itemView.findViewById(R.id.turma_capa);
            nome = itemView.findViewById(R.id.turma_nome);
            dia  = itemView.findViewById(R.id.turma_dia);
        }
    }
}
