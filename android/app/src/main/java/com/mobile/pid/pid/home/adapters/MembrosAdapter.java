package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.turmas.Turma;
import com.mobile.pid.pid.login.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MembrosAdapter extends RecyclerView.Adapter<MembrosAdapter.MembrosViewHolder>
{
    private Context c;
    private List<Usuario> membros;
    private Turma turma;
    private Usuario professor;

    private String uidUsuarioLogado;

    public MembrosAdapter(Context c, Turma turma)
    {
        this.c = c;
        this.membros = new ArrayList<>();
        this.turma = turma;

        this.uidUsuarioLogado = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public MembrosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.item_membro, parent, false);

        return new MembrosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MembrosViewHolder holder, int position)
    {
        Usuario membro = membros.get(position);
        String     uid = membro.getUid();

        if(turma.getProfessorUid().equals(uid))
        {
            holder.linearLayout.setBackgroundColor(c.getResources().getColor(R.color.colorPrimary));
            holder.professor.setVisibility(View.VISIBLE);
            Log.d("adskaskdas", "aaa" + uid);
        }

        Glide.with(c).load(membro.getFotoUrl()).into(holder.foto);

        holder.nome.setText(membro.getNome());

        if(!turma.getProfessorUid().equals(uidUsuarioLogado))
            holder.excluir.setVisibility(View.GONE);
        else
            holder.excluir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: OnClick
                }
            });
    }

    @Override
    public int getItemCount() {
        return membros.size();
    }

    public void setProfessor(Usuario professor){
        this.professor = professor;
    }

    public void add(Usuario u){
        membros.add(u);
        notifyDataSetChanged();
    }

    public void setMembros(List<Usuario> membros){
        this.membros = membros;

        ordenarMembros();
    }

    public void clear(Usuario u){
        membros.clear();
        notifyDataSetChanged();
    }

    private void ordenarMembros()
    {
        membros.add(0, professor);
        notifyDataSetChanged();
    }

    public class MembrosViewHolder extends RecyclerView.ViewHolder
    {
        public LinearLayout linearLayout;
        public ImageView foto;
        public TextView  nome;
        public TextView  professor;
        public ImageView excluir;

        public MembrosViewHolder(View itemView)
        {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.ll_membro);
            foto         = itemView.findViewById(R.id.membro_foto);
            nome         = itemView.findViewById(R.id.membro_nome);
            professor    = itemView.findViewById(R.id.membro_professor);
            excluir      = itemView.findViewById(R.id.membro_excluir);
        }
    }
}
