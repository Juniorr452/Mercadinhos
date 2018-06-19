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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.classes_e_interfaces.Dialogs;
import com.mobile.pid.pid.classes_e_interfaces.Post;
import com.mobile.pid.pid.classes_e_interfaces.Usuario;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ComentariosAdapter extends RecyclerView.Adapter<ComentariosAdapter.ComentariosHolder>{

    private Context context;
    private List<Post> comentarios;

    public ComentariosAdapter(Context context) {
        this.context = context;
        this.comentarios = new ArrayList<>();
    }

    @NonNull
    @Override
    public ComentariosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comentario, parent, false);

        return new ComentariosHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ComentariosHolder holder, int position) {
        final Post p = comentarios.get(position);

        // CARREGAR DATA DO COMENTARIO
        try {
            holder.data.setText(p.calcularTempo());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // CARREGAR FOTO E NOME DO USUARIO
        FirebaseDatabase.getInstance().getReference("usuarios").child(p.getUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario u = dataSnapshot.getValue(Usuario.class);
                        Glide.with(context.getApplicationContext()).load(u.getFotoUrl()).into(holder.foto);
                        holder.nome.setText(u.getNome());

                        p.setPhotoUrl(u.getFotoUrl());
                        p.setNomeUser(u.getNome());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        // SE CLICAR NA FOTO DO USUARIO
        holder.foto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Usuario user = new Usuario(p.getUserId(), p.getNomeUser(), null, p.getPhotoUrl());

                new Dialogs().dialogUsuario(user, context);

            }
        });

        holder.texto.setText(p.getTexto());
    }

    public void add(Post p) {
        comentarios.add(p);
        notifyDataSetChanged();
    }

    public void clear() {
        comentarios.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public class ComentariosHolder extends RecyclerView.ViewHolder {

        ImageView foto; // OK
        TextView nome; // OK
        TextView data; // OK
        TextView texto; // OK

        public ComentariosHolder(View itemView) {
            super(itemView);

            foto  = itemView.findViewById(R.id.foto_comentario);
            nome  = itemView.findViewById(R.id.nome_comentario);
            data  = itemView.findViewById(R.id.postTime_comentario);
            texto = itemView.findViewById(R.id.texto_comentario);
        }
    }
}
