package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.Dialogs;
import com.mobile.pid.pid.home.turmas.Turma;
import com.mobile.pid.pid.objetos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class BuscarAdapter extends RecyclerView.Adapter<BuscarAdapter.BuscarHolder> {

    private Context context;
    private List<Object> items;

    public BuscarAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<Object>();
    }

    @NonNull
    @Override
    public BuscarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_busca, parent, false);
        return new BuscarHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuscarHolder holder, int position) {

        if(items.get(position) instanceof Usuario) {
            Usuario usuario = (Usuario) items.get(position);

            Glide.with(holder.foto).load(usuario.getFotoUrl()).into(holder.foto);
            holder.nome.setText(usuario.getNome());
            holder.tipo.setVisibility(View.GONE);
        } else {
            Turma turma = (Turma) items.get(position);

            Glide.with(holder.foto).load(turma.getCapaUrl()).into(holder.foto);
            holder.nome.setText(turma.getNome());
            holder.tipo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add(Object u) {
        items.add(u);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public class BuscarHolder extends RecyclerView.ViewHolder {

        ImageView foto;
        TextView nome;
        ImageView tipo;

        public BuscarHolder(View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.foto);
            nome = itemView.findViewById(R.id.nome);
            tipo = itemView.findViewById(R.id.tipo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(items.get(getPosition()) instanceof Usuario) {
                        Usuario u = (Usuario) items.get(getPosition());
                        new Dialogs().dialogUsuario(u, context);
                    } else {
                        Turma t = (Turma) items.get(getPosition());
                        new Dialogs().dialogTurma(t, context);
                    }
                }
            });

        }
    }
}
