package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.turmas.detalhes_turma.chat.ChatMensagem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMensagemAdapter extends RecyclerView.Adapter<ChatMensagemAdapter.ChatMensagemViewHolder>
{
    private Context ctx;
    private LayoutInflater layoutInflater;
    private List<ChatMensagem> mensagens;
    private FirebaseUser user;

    public ChatMensagemAdapter(Context c, List<ChatMensagem> mensagens)
    {
        this.ctx       = c;
        this.mensagens = mensagens;

        layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            holder.imagemUsuarioOutro.setVisibility(View.GONE);
            Glide.with(ctx)
                    .load(m.getFotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imagemUsuarioOutro);
        }
        else
        {
            holder.imagemUsuario.setVisibility(View.GONE);
            Glide.with(ctx)
                    .load(m.getFotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imagemUsuario);
        }


        if(m.isProfessor())
        {
            holder.llc.setBackgroundTintList(ContextCompat.getColorStateList(ctx, R.color.colorPrimary));
            holder.mensagem.setTextColor(ContextCompat.getColor(ctx, R.color.white));
        }

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
            imagemUsuario      = itemView.findViewById(R.id.chat_msg_usuario_imagem);
            imagemUsuarioOutro = itemView.findViewById(R.id.chat_msg_usuario_imagem_outro);
            imagem             = itemView.findViewById(R.id.chat_msg_imagem);
            mensagem           = itemView.findViewById(R.id.chat_msg_texto);
        }
    }

    public void add(ChatMensagem m)
    {
        mensagens.add(m);
        notifyDataSetChanged();
    }

    public void clear()
    {
        mensagens.clear();
        notifyDataSetChanged();
    }
}