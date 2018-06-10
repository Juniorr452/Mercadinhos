package com.mobile.pid.pid.home.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.Dialogs;
import com.mobile.pid.pid.home.feed.Post;
import com.mobile.pid.pid.login.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jonasramos on 13/03/18.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.RecyclerViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position)
    {
        final Post p = posts.get(position);
        final String usuarioLogado = FirebaseAuth.getInstance().getCurrentUser().getUid();


        try
        {
            holder.postTime.setText(calcularTempo(p.getPostDataFormatado()));
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        holder.texto.setText(p.getTexto());

        FirebaseDatabase.getInstance().getReference("usuarios").child(p.getUserId())
            .addValueEventListener(new ValueEventListener() 
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) 
                {
                    Usuario user = dataSnapshot.getValue(Usuario.class);

                    Glide.with(context.getApplicationContext())
                            .load(user.getFotoUrl())
                            .into(holder.foto);

                    holder.usuario.setText(user.getNome());

                    p.setPhotoUrl(user.getFotoUrl());
                    p.setNomeUser(user.getNome());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        // CLICAR NA FOTO DO USUARIO REDIRECIONA PRO PERFIL DA PESSOA
        holder.foto.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View view) 
            {
                Usuario user = new Usuario(p.getUserId(), p.getNomeUser(), null, p.getPhotoUrl());

                if(p.getUserId().equals(usuarioLogado)) 
                {
                    //TODO ENVIAR PARA O PERFIL FRAGMENT
                    new Dialogs().dialogUsuario(user, context);
                } else
                    new Dialogs().dialogUsuario(user, context);

            }
        });

        holder.like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() 
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) 
            {

                if(holder.like.isChecked()){
                    holder.like.setButtonTintList(context.getResources().getColorStateList(R.color.red));

                    FirebaseDatabase.getInstance().getReference("postLikes").child(p.getId())
                            .child(usuarioLogado).setValue(true);

                    // SALVAR NOS POSTS CURTIDOS DO USUARIO
                    FirebaseDatabase.getInstance().getReference("userLikes").child(usuarioLogado)
                            .child(p.getId()).setValue(p);
                } 
                else 
                {
                    holder.like.setButtonTintList(context.getResources().getColorStateList(R.color.gray_font));

                    FirebaseDatabase.getInstance().getReference("postLikes").child(p.getId())
                            .child(usuarioLogado).removeValue();

                    FirebaseDatabase.getInstance().getReference("userLikes").child(usuarioLogado)
                            .child(p.getId()).removeValue();
                }
            }
        });

        // CARREGAR OS POSTS CURTIDOS COM O NUMERO DE CURTIDAS
        FirebaseDatabase.getInstance().getReference().child("postLikes").child(p.getId())
                .addValueEventListener(new ValueEventListener() {
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
                if(dataSnapshot.hasChild(usuarioLogado)) {
                    holder.like.setButtonTintList(context.getResources().getColorStateList(R.color.red));
                    holder.like.setChecked(true);
                } else {
                    holder.like.setButtonTintList(context.getResources().getColorStateList(R.color.gray_font));
                    holder.like.setChecked(false);
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
        long horas       = duracao / (60 * 60 * 1000);
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
        {
            c.setTime(dataPost);
            return  String.format("%02d", c.get(Calendar.DAY_OF_MONTH)) + "/" +
                    String.format("%02d", (c.get(Calendar.MONTH) + 1)) + "/" +
                    c.get(Calendar.YEAR);
        }

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

        public RecyclerViewHolder(final View itemView) {
            super(itemView);

            foto       = itemView.findViewById(R.id.icon_user_feed);
            usuario    = itemView.findViewById(R.id.tv_user_feed);
            texto      = itemView.findViewById(R.id.tv_message_feed);
            postTime   = itemView.findViewById(R.id.postTime);
            countLike  = itemView.findViewById(R.id.count_like);
            like = itemView.findViewById(R.id.cb_like);
            
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final Post p = posts.get(getPosition());

                    if(p.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                        AlertDialog dialog = new AlertDialog.Builder(context)
                                .setTitle(R.string.confirmacao)
                                .setMessage(R.string.excluir_post)
                                .setPositiveButton(R.string.confirmar, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        FirebaseDatabase.getInstance().getReference("posts").child(p.getId()).child("likes")
                                                .addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        // REMOVE O POST CURTIDO DE TODOS OS USUARIOS QUE CURTIRAM
                                                        if(dataSnapshot.exists()) {
                                                            for(DataSnapshot data : dataSnapshot.getChildren()) {
                                                                FirebaseDatabase.getInstance().getReference("usuarios").child(data.getKey())
                                                                        .child("posts_like").child(p.getId()).removeValue();

                                                                data.getRef().removeValue();
                                                            }
                                                        }

                                                        // REMOVE O POST DO USUARIO QUE CRIOU
                                                        FirebaseDatabase.getInstance().getReference("usuarios")
                                                                .child(p.getUserId()).child("posts").child(p.getId()).removeValue();
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                    }
                                })
                                .setNegativeButton(R.string.cancel, null)
                                .create();

                        dialog.show();

                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.gray_font));

                        return true;
                    }

                    return false;
                }
            });
        }
    }

    public void removePost(Post p) {
        for (Post post: posts) {
            if(post.getId().equals(p.getId())) {
                posts.remove(post);
                notifyDataSetChanged();
            }
        }
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }
}
