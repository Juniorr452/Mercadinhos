package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.Dialogs;
import com.mobile.pid.pid.home.turmas.Turma;
import com.mobile.pid.pid.login.Usuario;

import java.util.ArrayList;
import java.util.List;

public class SugestaoAdapter extends RecyclerView.Adapter<SugestaoAdapter.SugestaoHolder> {

    private Context context;
    private List<Object> sugestoes;

    public SugestaoAdapter(Context context) {
        this.context = context;
        sugestoes = new ArrayList<>();
    }


    @NonNull
    @Override
    public SugestaoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sugestao_buscar, parent, false);

        return new SugestaoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SugestaoHolder holder, int position) {

        if(sugestoes.get(position) instanceof Usuario) {
            Usuario user = (Usuario) sugestoes.get(position);
            holder.nome.setText(user.getNome());
            Glide.with(holder.foto).load(user.getFotoUrl()).into(holder.foto);
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            Turma turma = (Turma) sugestoes.get(position);
            holder.nome.setText(turma.getNome());
            Glide.with(holder.foto).load(turma.getCapaUrl()).into(holder.foto);
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }

    }

    @Override
    public int getItemCount() {
        return sugestoes.size();
    }

    public void add(Object obj) {
        sugestoes.add(obj);
        notifyDataSetChanged();
    }

    public void setSugestoes(List<Object> sugestoes){
        this.sugestoes = sugestoes;
        notifyDataSetChanged();
    }

    public class SugestaoHolder extends RecyclerView.ViewHolder {

        TextView nome;
        ImageView foto;
        LinearLayout linearLayout;

        String usuario = FirebaseAuth.getInstance().getCurrentUser().getUid();

        public SugestaoHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.nome);
            foto = itemView.findViewById(R.id.foto);
            linearLayout = itemView.findViewById(R.id.linear);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(sugestoes.get(getPosition()) instanceof Usuario) {
                        Usuario user = (Usuario) sugestoes.get(getPosition());
                        new Dialogs().dialogUsuario(user, context);
                    } else {
                        Turma turma = (Turma) sugestoes.get(getPosition());
                        new Dialogs().dialogTurma(turma, context);
                    }
                }
            });
        }
    }
}
