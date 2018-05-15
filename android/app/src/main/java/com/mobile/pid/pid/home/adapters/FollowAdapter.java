package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.feed.Post;
import com.mobile.pid.pid.home.perfil.FollowItem;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonasramos on 19/03/18.
 */

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.RecyclerViewHolder> {

    public static final int SEGUINDO_CONTEXT = 1;
    public static final int SEGUIDORES_CONTEXT = 0;

    private Context context;
    private List<FollowItem> follow;
    private int context_cod;

    private final String FOLLOW_STRING;
    private final String FOLLOWING_STRING;

    public FollowAdapter(Context context, int context_cod) {
        this.context = context;
        this.follow = new ArrayList<>();
        this.context_cod = context_cod;
        FOLLOW_STRING = context.getResources().getString(R.string.follow);
        FOLLOWING_STRING = context.getResources().getString(R.string.following);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_follow, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final FollowItem item = follow.get(position);
        final String usuarioLogado = FirebaseAuth.getInstance().getCurrentUser().getUid();

        holder.usuario.setText(item.getNome());
        Glide.with(context).load(item.getFoto()).into(holder.foto);

        if(context_cod == 0) {
            FirebaseDatabase.getInstance().getReference("userSeguindo").child(usuarioLogado)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists() && dataSnapshot.hasChild(item.getUid())) {
                                holder.botaoSeguir.setChecked(true);
                            } else {
                                holder.botaoSeguir.setChecked(false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        holder.botaoSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.botaoSeguir.isChecked()) {
                    // ACRESCENTA NOS SEGUIDORES DO USUARIO
                    FirebaseDatabase.getInstance().getReference("userSeguidores").child(item.getUid())
                            .child(usuarioLogado).setValue(true);

                    // ACRESCENTA NOS USUARIOS QUE ESTOU SEGUINDO
                    FirebaseDatabase.getInstance().getReference("userSeguindo").child(usuarioLogado)
                            .child(item.getUid()).setValue(true);

                    if(context_cod == SEGUINDO_CONTEXT) {
                        follow.add(item);
                        notifyDataSetChanged();
                    }

                    holder.botaoSeguir.setChecked(true);
                } else {
                    // REMOVE DOS SEGUIDORES DO USUARIO
                    FirebaseDatabase.getInstance().getReference("userSeguidores").child(item.getUid())
                            .child(usuarioLogado).removeValue();

                    // REMOVE DOS USUARIOS QUE ESTOU SEGUINDO
                    FirebaseDatabase.getInstance().getReference("userSeguindo").child(usuarioLogado)
                            .child(item.getUid()).removeValue();

                    // REMOVE TODOS OS POSTS DO USUARIO DO MEU FEED
                    FirebaseDatabase.getInstance().getReference("feed").child(usuarioLogado)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()) {
                                        for(DataSnapshot post : dataSnapshot.getChildren()) {
                                            Post p = post.getValue(Post.class);

                                            if(p.getUserId().equals(item.getUid()))
                                                post.getRef().removeValue();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                    if(context_cod == SEGUINDO_CONTEXT) {
                        follow.remove(item);
                        notifyDataSetChanged();
                    }

                    holder.botaoSeguir.setChecked(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return follow.size();
    }

    public void add(FollowItem item) {
        follow.add(item);
        notifyDataSetChanged();
    }

    public void remove(FollowItem item) {
        for (FollowItem followItem: follow) {
            if(followItem.getUid().equals(item.getUid())) {
                follow.remove(followItem);
                notifyDataSetChanged();
            }
        }
    }

    public void clear() {
        follow.clear();
        notifyDataSetChanged();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView foto;
        private TextView usuario;
        private CheckBox botaoSeguir;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            foto        = itemView.findViewById(R.id.icon_user_follow);
            usuario     = itemView.findViewById(R.id.textView_user_name);
            botaoSeguir = itemView.findViewById(R.id.btn_follow);
        }
    }
}

