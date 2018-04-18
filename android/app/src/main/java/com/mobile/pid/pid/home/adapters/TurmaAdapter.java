package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.turmas.Turma;
import com.mobile.pid.pid.home.turmas.detalhes_turma.DetalhesTurmaAluno;
import com.mobile.pid.pid.home.turmas.detalhes_turma.DetalhesTurmaProfessor;

import java.util.List;
import java.util.Map;

/**
 * Created by junio on 12/03/2018.
 */

// TODO: Estudar essa p**** direito
public class TurmaAdapter extends RecyclerView.Adapter<TurmaAdapter.TurmaViewHolder>
{
    public static final int COD_TURMAS_MATRICULADAS = 0;
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
        notifyItemInserted(0);
    }

    public void clear() {
        listaTurmas.clear();
    }

    public List<Turma> getLista() { return listaTurmas; }

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

                    Turma  t = listaTurmas.get(getPosition());
                    Intent i = null;

                    switch(COD_CONTEXT) {
                        case COD_TURMAS_CRIADAS:
                            i = new Intent(layoutInflater.getContext(), DetalhesTurmaProfessor.class);
                            break;
                        case COD_TURMAS_MATRICULADAS:
                            i = new Intent(layoutInflater.getContext(), DetalhesTurmaAluno.class);
                            break;
                        default:
                            break;
                    }

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
