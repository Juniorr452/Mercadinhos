package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.pid.pid.R;
import com.mobile.pid.pid.login.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MembrosAdapter extends RecyclerView.Adapter<MembrosAdapter.MembrosViewHolder>
{
    private Context c;
    private List<Usuario> membros;

    public MembrosAdapter(Context c)
    {
        this.c = c;
        this.membros = new ArrayList<>();
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


    }

    @Override
    public int getItemCount() {
        return membros.size();
    }

    public class MembrosViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView foto;
        public TextView  nome;
        public TextView  professor;
        public ImageView excluir;

        public MembrosViewHolder(View itemView) {
            super(itemView);
        }
    }
}
