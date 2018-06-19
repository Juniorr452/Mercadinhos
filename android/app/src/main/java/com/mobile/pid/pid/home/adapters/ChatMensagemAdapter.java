package com.mobile.pid.pid.home.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.classes_e_interfaces.ChatMensagem;
import com.mobile.pid.pid.classes_e_interfaces.Dialogs;

import java.util.List;

public class ChatMensagemAdapter extends RecyclerView.Adapter<ChatMensagemAdapter.ChatMensagemViewHolder>
{
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<ChatMensagem> mensagens;
    private FirebaseUser user;
    private int qtdMensagens;

    public ChatMensagemAdapter(Activity activity, List<ChatMensagem> mensagens)
    {
        this.activity     = activity;
        this.mensagens    = mensagens;
        this.qtdMensagens = 0;

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public ChatMensagemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = layoutInflater.inflate(R.layout.item_chat_mensagem, parent, false);

        ChatMensagemViewHolder vh = new ChatMensagemViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ChatMensagemViewHolder holder, int position)
    {
        final ChatMensagem m = mensagens.get(position);

        if (m.getUid().equals(user.getUid()))
        {
            holder.ll.setGravity(Gravity.RIGHT);

            ((CardView) holder.imagemUsuarioOutro.getParent()).setVisibility(View.GONE);
            ((CardView) holder.imagemUsuario.getParent()).setVisibility(View.VISIBLE);

            Glide.with(activity)
                .load(m.getFotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imagemUsuario);
        }
        else
        {
            holder.ll.setGravity(Gravity.LEFT);

            ((CardView) holder.imagemUsuarioOutro.getParent()).setVisibility(View.VISIBLE);
            ((CardView) holder.imagemUsuario.getParent()).setVisibility(View.GONE);

            Glide.with(activity)
                .load(m.getFotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imagemUsuarioOutro);
        }

        if(m.isProfessor())
        {
            holder.llc.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.colorPrimary));
            holder.mensagem.setTextColor(ContextCompat.getColor(activity, R.color.white));
        }
        else
        {
            holder.llc.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.white));
            holder.mensagem.setTextColor(ContextCompat.getColor(activity, R.color.black));
        }

        if(m.getImagemUrl() != null)
        {
            Glide.with(activity).load(m.getImagemUrl()).into(holder.imagem);
            holder.imagem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialogs.mostrarImagem(activity, m.getImagemUrl());
                }
            });
            holder.mensagem.setVisibility(View.GONE);
        }
        else if(m.getMensagem() != null)
            holder.mensagem.setText(m.getMensagem());
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    public class ChatMensagemViewHolder extends RecyclerView.ViewHolder
    {
        // Linear Layout Odin (Pai de todos)
        LinearLayout ll;

        // Linear layout só da mensagem
        LinearLayout llc;

        ImageView imagemUsuario;
        ImageView imagemUsuarioOutro;
        ImageView imagem;
        TextView mensagem;

        public ChatMensagemViewHolder(View itemView)
        {
            super(itemView);

            ll                 = itemView.findViewById(R.id.ll_chat_msg);
            llc                = itemView.findViewById(R.id.ll_chat_msg_conteudo);
            imagem             = itemView.findViewById(R.id.chat_msg_imagem);
            mensagem           = itemView.findViewById(R.id.chat_msg_texto);
            imagemUsuario      = itemView.findViewById(R.id.chat_msg_usuario_imagem);
            imagemUsuarioOutro = itemView.findViewById(R.id.chat_msg_usuario_imagem_outro);
        }
    }

    public void add(ChatMensagem m)
    {
        mensagens.add(m);
        notifyItemInserted(qtdMensagens++);
    }

    public void clear()
    {
        mensagens.clear();
        qtdMensagens = 0;
        notifyDataSetChanged();
    }
}