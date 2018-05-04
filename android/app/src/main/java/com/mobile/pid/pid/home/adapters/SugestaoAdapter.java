package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.pid.pid.R;
import com.mobile.pid.pid.login.Usuario;

import java.util.List;

public class SugestaoAdapter extends RecyclerView.Adapter<SugestaoAdapter.SugestaoHolder> {

    private Context context;
    private List<Usuario> usuarios;
    private int COD_CONTEXTO;
    private static final int USUARIO = 0;
    private static final int TURMA = 1;

    public SugestaoAdapter(Context context, int COD_CONTEXTO) {
        this.context = context;
        this.COD_CONTEXTO = COD_CONTEXTO;
    }


    @NonNull
    @Override
    public SugestaoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sugestao_buscar, parent, false);

        return new SugestaoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SugestaoHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SugestaoHolder extends RecyclerView.ViewHolder {

        public SugestaoHolder(View itemView) {
            super(itemView);
        }
    }
}
