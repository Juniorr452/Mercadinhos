package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.turmas.AvisoTurma;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AvisosTurmaAdapter extends RecyclerView.Adapter<AvisosTurmaAdapter.AvisosHolder>
{
    Context context;
    List<AvisoTurma> avisos;

    public AvisosTurmaAdapter(Context context)
    {
        this.context = context;
        this.avisos = new ArrayList<>();
    }

    @NonNull
    @Override
    public AvisosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = layoutInflater.inflate(R.layout.item_aviso_turma, parent, false);

        return new AvisosHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AvisosHolder holder, int position)
    {
        AvisoTurma aviso = avisos.get(position);

        holder.data.setText(aviso.getDataFormatada());
        holder.aviso.setText(aviso.getAviso());
    }

    @Override
    public int getItemCount() {
        return avisos.size();
    }

    public void add(AvisoTurma aviso)
    {
        avisos.add(aviso);
        notifyDataSetChanged();
    }

    public void clear()
    {
        avisos.clear();
        notifyDataSetChanged();
    }

    public class AvisosHolder extends RecyclerView.ViewHolder
    {
        TextView data;
        TextView aviso;

        public AvisosHolder(View itemView)
        {
            super(itemView);

            data  = itemView.findViewById(R.id.aviso_turma_data);
            aviso = itemView.findViewById(R.id.aviso_turma_aviso);
        }
    }
}
