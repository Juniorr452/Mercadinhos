package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.time.Duration;

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
    public void onBindViewHolder(RecyclerViewHolder holder, int position)
    {

        Post p = posts.get(position);
        holder.usuario.setText(p.getUser());

        try
        {
            holder.postTime.setText(calcularTempo(p.getPostDataFormatado()));
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        //holder.postTime.setText(p.getPostDataFormatado());

        holder.texto.setText(p.getTexto());
        Glide.with(holder.foto.getContext())
                .load(p.getPhotoUrl())
                .into(holder.foto);
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

        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }

        public RecyclerViewHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.post_layout, container, false));

            foto       = itemView.findViewById(R.id.icon_user_feed);
            usuario    = itemView.findViewById(R.id.tv_user_feed);
            texto      = itemView.findViewById(R.id.tv_message_feed);
            postTime   = itemView.findViewById(R.id.postTime);

        }
    }
}
