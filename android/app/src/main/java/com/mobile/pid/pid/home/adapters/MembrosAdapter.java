package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.mobile.pid.pid.classes_e_interfaces.Dialogs;
import com.mobile.pid.pid.classes_e_interfaces.PidSort;
import com.mobile.pid.pid.classes_e_interfaces.Turma;
import com.mobile.pid.pid.classes_e_interfaces.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MembrosAdapter extends RecyclerView.Adapter<MembrosAdapter.MembrosViewHolder>
{
    private Context c;
    private List<Usuario> membros;
    private int qtdMembros;
    private Turma turma;

    private String uidUsuarioLogado;

    private boolean precisaOrdenar;

    public MembrosAdapter(Context c, Turma turma)
    {
        this.c = c;
        this.membros = new ArrayList<>();
        this.turma = turma;

        this.uidUsuarioLogado = FirebaseAuth.getInstance().getCurrentUser().getUid();

        this.qtdMembros     = Integer.MAX_VALUE;
        this.precisaOrdenar = true;
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
        final Usuario membro = membros.get(position);
        final String     uid = membro.getUid();

        if(turma.getProfessorUid().equals(uid))
        {
            holder.linearLayout.setBackgroundColor(c.getResources().getColor(R.color.colorPrimary));
            holder.professor.setVisibility(View.VISIBLE);
            holder.excluir.setVisibility(View.GONE);
            Log.d("adskaskdas", "aaa" + uid);
        }

        Glide.with(c).load(membro.getFotoUrl()).into(holder.foto);

        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.dialogUsuario(membro, c);
            }
        });

        holder.nome.setText(membro.getNome());

        if(!turma.getProfessorUid().equals(uidUsuarioLogado))
            holder.excluir.setVisibility(View.GONE);
        else
            holder.excluir.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    new AlertDialog.Builder(c)
                        .setTitle(R.string.warning)
                        .setMessage("Deseja excluir este membro da turma?")
                        .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                turma.desmatricularAluno(uid);
                            }
                        }).setNegativeButton(R.string.cancel, null)
                        .show();
                }
            });
    }

    @Override
    public int getItemCount() {
        return membros.size();
    }

    public void add(Usuario u)
    {
        membros.add(u);

        if(membros.size() >= qtdMembros)
        {
            precisaOrdenar = true;
            ordenarMembros();
        }

        notifyDataSetChanged();
    }

    public void setMembros(List<Usuario> membros)
    {
        this.membros   = membros;
        precisaOrdenar = true;
        ordenarMembros();
    }

    public void clear(){
        membros.clear();
        notifyDataSetChanged();
    }

    public void ordenarMembros()
    {
        if(precisaOrdenar)
        {
            PidSort.mergeSort(membros, new Comparator<Usuario>() {
                @Override
                public int compare(Usuario o1, Usuario o2) {
                    return o1.getNome().compareTo(o2.getNome());
                }
            });

            precisaOrdenar = false;

            notifyDataSetChanged();
        }
    }

    public void setQtdMembros(int qtdMembros) {
        this.qtdMembros = qtdMembros;
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
