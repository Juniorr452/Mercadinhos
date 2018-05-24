package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.Dialogs;
import com.mobile.pid.pid.home.turmas.Turma;
import com.mobile.pid.pid.login.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SugestaoAdapter extends RecyclerView.Adapter<SugestaoAdapter.SugestaoHolder> {

    private Context context;
    private List<Object> sugestaoItem;

    public SugestaoAdapter(Context context) {
        this.context = context;
        sugestaoItem = new ArrayList<>();
    }


    @NonNull
    @Override
    public SugestaoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sugestao_buscar, parent, false);

        return new SugestaoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SugestaoHolder holder, int position) {

        if(sugestaoItem.get(position) instanceof Usuario) {
            Usuario user = (Usuario) sugestaoItem.get(position);
            holder.nome.setText(user.getNome());
            Glide.with(holder.foto).load(user.getFotoUrl()).into(holder.foto);
        } else {
            Turma turma = (Turma) sugestaoItem.get(position);
            holder.nome.setText(turma.getNome());
            Glide.with(holder.foto).load(turma.getCapaUrl()).into(holder.foto);
        }

    }

    @Override
    public int getItemCount() {
        return sugestaoItem.size();
    }

    public void add(Object obj) {
        sugestaoItem.add(obj);
        notifyDataSetChanged();
    }

    public class SugestaoHolder extends RecyclerView.ViewHolder {

        TextView nome;
        ImageView foto;

        String usuario = FirebaseAuth.getInstance().getCurrentUser().getUid();

        public SugestaoHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.nome);
            foto = itemView.findViewById(R.id.foto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(sugestaoItem.get(getPosition()) instanceof Usuario) {
                        Usuario user = (Usuario) sugestaoItem.get(getPosition());
                        new Dialogs().dialogUsuario(user, context);
                    } else {
                        Turma turma = (Turma) sugestaoItem.get(getPosition());
                        new Dialogs().dialogTurma(turma, context);
                    }
                }
            });
        }
    }
}
