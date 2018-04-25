package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.feed.Post;
import com.mobile.pid.pid.home.perfil.PerfilFragment;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jonasramos on 13/03/18.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.RecyclerViewHolder>{

    private Context context;
    private List<Post> posts;
    private LayoutInflater inflater;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        return new RecyclerViewHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position)
    {

        final Post p = posts.get(position);
        final String usuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
        holder.usuario.setText(p.getUser());

        try
        {
            holder.postTime.setText(calcularTempo(p.getPostDataFormatado()));
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        holder.texto.setText(p.getTexto());

        Glide.with(holder.foto.getContext())
                .load(p.getPhotoUrl())
                .into(holder.foto);

        // SE CLICAR NA FOTO DO USUARIO REDIRECIONA PRO PERFIL DA PESSOA
        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(context.getApplicationContext(), PerfilFragment.class);
                //i.putExtra("post", p);
                //i.putExtra("contexto", 0);
                //inflater.getContext().startActivity(i);
                //TODO QUANDO CLICAR NA FOTO DO USUARIO, IR PARA O PERFIL DELE
                Toast.makeText(context, "Ir para o perfil do usuario " + p.getUser(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    compoundButton.setButtonTintList(context.getResources().getColorStateList(R.color.red));

                    FirebaseDatabase.getInstance().getReference().child("posts").child(p.getId())
                            .child("likes").child(usuario).setValue(true);

                    // SALVAR NOS POSTS CURTIDOS DO USUARIO
                    FirebaseDatabase.getInstance().getReference("usuarios").child(usuario).child("posts_like")
                            .child(p.getId()).setValue(p);
                } else {
                    compoundButton.setButtonTintList(context.getResources().getColorStateList(R.color.gray_font));

                    FirebaseDatabase.getInstance().getReference("posts").child(p.getId())
                            .child("likes").child(usuario).removeValue();

                    FirebaseDatabase.getInstance().getReference("usuarios").child(usuario).child("posts_like")
                            .child(p.getId()).removeValue();
                }
            }
        });

        // CARREGAR OS POSTS CURTIDOS COM O NUMERO DE CURTIDAS
        FirebaseDatabase.getInstance().getReference().child("posts").child(p.getId())
                .child("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // CASO O CONTADOR DE CURTIDAS DO POST SEJA ZERO, ELE NAO MOSTRA O CONTADOR
                if(dataSnapshot.getChildrenCount() == 0)
                    holder.countLike.setVisibility(View.INVISIBLE);
                else {
                    holder.countLike.setVisibility(View.VISIBLE);
                    holder.countLike.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                }

                // MOSTRA NO FEED O POST CURTIDO CASO O USUARIO TENHA CURTIDO ANTERIORMENTE
                if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    holder.like.setButtonTintList(context.getResources().getColorStateList(R.color.red));
                    holder.like.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public String calcularTempo(String data) throws ParseException
    {
        Calendar         c   = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        Date dataAtual = c.getTime();
        Date dataPost  = sdf.parse(data);

        long duracao     = dataAtual.getTime() - dataPost.getTime();
        long horas       = duracao / (60 * 60 * 1000) % 24;
        long minutos     = duracao / (60 * 1000) % 60;
        long segundos    = duracao / 1000 % 60;

        if(horas < 24)
            if(horas == 0)
                if(minutos == 0)
                    if (segundos <= 0)
                        return "Agora";
                    else
                        return String.valueOf(segundos) + "s";
                else
                    return String.valueOf(minutos) + "m";
            else
                return String.valueOf(horas) + "h";
        else
            return String.valueOf(dataPost.getDay()) + "/" + String.valueOf(dataPost.getMonth()) + "/" + String.valueOf(dataPost.getYear());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void add(Post post) {
        this.posts.add(post);

        notifyDataSetChanged();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView foto;
        private TextView usuario;
        private TextView texto;
        private TextView postTime;
        private TextView countLike;
        private CheckBox like;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }

        public RecyclerViewHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.post_layout, container, false));

            foto       = itemView.findViewById(R.id.icon_user_feed);
            usuario    = itemView.findViewById(R.id.tv_user_feed);
            texto      = itemView.findViewById(R.id.tv_message_feed);
            postTime   = itemView.findViewById(R.id.postTime);
            countLike  = itemView.findViewById(R.id.count_like);
            like = itemView.findViewById(R.id.cb_like);

        }
    }
}
