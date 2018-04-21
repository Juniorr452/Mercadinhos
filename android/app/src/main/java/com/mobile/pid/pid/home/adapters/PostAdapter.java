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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

        Post p = posts.get(position);
        holder.usuario.setText(p.getUser());

        holder.postTime.setText(calcularTempo(p.getPostData()));

        holder.texto.setText(p.getTexto());
        Glide.with(holder.foto.getContext())
                .load(p.getPhotoUrl())
                .into(holder.foto);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String calcularTempo(String data) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        String[] aDataAtual = sdf.format(c.getTime()).split("[- :]");
        String[] aDataPost = data.split("[- :]");

        // DATA ATUAL
        int diaAtual  = Integer.parseInt(aDataAtual[0]);
        int mesAtual  = Integer.parseInt(aDataAtual[1]);
        int anoAtual  = Integer.parseInt(aDataAtual[2]);
        int horaAtual = Integer.parseInt(aDataAtual[3]);
        int minAtual  = Integer.parseInt(aDataAtual[4]);
        int segAtual  = Integer.parseInt(aDataAtual[5]);

        // DATA DO POST
        int diaPost  = Integer.parseInt(aDataPost[0]);
        int mesPost  = Integer.parseInt(aDataPost[1]);
        int anoPost  = Integer.parseInt(aDataPost[2]);
        int horaPost = Integer.parseInt(aDataPost[3]);
        int minPost  = Integer.parseInt(aDataPost[4]);
        int segPost  = Integer.parseInt(aDataPost[5]);

        int tempoAtual = horaAtual*3600 + minAtual*60 + segAtual;
        int tempoPost  = horaPost*3600 + minPost*60 + segPost;

        int segundosTotais = tempoAtual - tempoPost;

        Duration duracao =  Duration.ofSeconds(segundosTotais);
        long horas = duracao.toHours();
        long minutos = duracao.minusHours(horas).toMinutes();
        long segundos = duracao.minusHours(horas).minusMinutes(minutos).getSeconds();

        if(horas < 24)
            if(horas == 0)
                if(minutos == 0)
                    return String.valueOf(segundos) + "s";
                else
                    return String.valueOf(minutos) + "m";
            else
                return String.valueOf(horas) + "h";
        else
            return String.valueOf(diaPost) + "/" + String.valueOf(mesPost) + "/" + String.valueOf(anoPost);
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
