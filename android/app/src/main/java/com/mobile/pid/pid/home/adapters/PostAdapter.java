package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.feed.Post;

import java.io.File;
import java.util.List;

/**
 * Created by jonasramos on 13/03/18.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.RecyclerViewHolder>{

    private Context context;
    private List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new RecyclerViewHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        Post p = posts.get(position);
        holder.usuario.setText(p.getUser());
        holder.texto.setText(p.getTexto());
        Glide.with(holder.foto.getContext())
                .load(p.getPhotoUrl())
                .into(holder.foto);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void add(Post post) {
        this.posts.add(post);
        notifyDataSetChanged();
    }

    public void clear() {this.posts.clear();}

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView foto;
        private TextView usuario;
        private TextView texto;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }

        public RecyclerViewHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.post_layout, container, false));

            foto       = itemView.findViewById(R.id.icon_user_feed);
            usuario    = itemView.findViewById(R.id.tv_user_feed);
            texto      = itemView.findViewById(R.id.tv_message_feed);

        }
    }
}
