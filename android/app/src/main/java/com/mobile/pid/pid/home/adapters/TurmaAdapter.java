package com.mobile.pid.pid.home.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.home.HomeActivity;
import com.mobile.pid.pid.home.feed.Post;
import com.mobile.pid.pid.home.perfil.AtualizarPerfilActivity;
import com.mobile.pid.pid.home.turmas.InfoUsuario;
import com.mobile.pid.pid.home.turmas.Turma;
import com.mobile.pid.pid.home.turmas.detalhes_turma.DetalhesTurma;
import com.mobile.pid.pid.login.Usuario;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

/**
 * Created by junio on 12/03/2018.
 */

public class TurmaAdapter extends RecyclerView.Adapter<TurmaAdapter.TurmaViewHolder>
{
    private static final int PROFESSOR = 0;
    private static final int ALUNO = 1;

    private List<Turma>    listaTurmas;
    private LayoutInflater layoutInflater;
    Activity activity;

    public TurmaAdapter(Activity activity, List<Turma> l)
    {
        this.activity  = activity;
        listaTurmas    = l;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public TurmaViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = layoutInflater.inflate(R.layout.item_turma, parent, false);

        TurmaViewHolder vh = new TurmaViewHolder(v);

        return vh;
    }

    // Setar os dados da lista às views
    @Override
    public void onBindViewHolder(final TurmaViewHolder holder, final int position)
    {
        final Turma           t = listaTurmas.get(position);
        final String        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final boolean professor = uid.equals(t.getProfessorUid());

        // Carregar imagem da capa
        Glide.with(holder.capa.getContext())
                .load(t.getCapaUrl())
                .into(holder.capa);

        // Carregar imagem do prof
        FirebaseDatabase.getInstance().getReference()
            .child("usuarios")
            .child(t.getProfessorUid())
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Usuario prof = dataSnapshot.getValue(Usuario.class);

                    Glide.with(holder.fotoProf).load(prof.getFotoUrl()).into(holder.fotoProf);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        holder.nome.setText(t.getNome());

        if(!professor)
            holder.excluir.setVisibility(View.INVISIBLE);
        else
            holder.excluir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(layoutInflater.getContext(), R.style.DialogTheme)
                        .setTitle("Excluir a turma?")
                        .setMessage("Deseja realmente excluir a turma?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {

                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                t.excluir();
                            }
                        })
                        .show();
                }
            });

        // DIAS DA SEMANA
        Map<String, Integer> dias = t.getDiasDaSemana();
        String dia = "";

        for (Map.Entry<String, Integer> entry: dias.entrySet())
            if(dia.isEmpty())
                dia = entry.getKey();
            else
                dia += " - " + entry.getKey();

        holder.dia.setText(dia);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Context c = holder.itemView.getContext();

                // Se o usuário está na turma.
                if (t.estaNaTurma(uid))
                {
                    // https://www.youtube.com/watch?v=BF4yvhpMPcg&t=494s
                    Intent i = new Intent(layoutInflater.getContext(), DetalhesTurma.class);

                    Pair[] views = new Pair[3];

                    views[0] = new Pair<View, String>(holder.cardViewFotoProf, holder.cardViewFotoProf.getTransitionName());
                    views[1] = new Pair<View, String>(holder.capa, holder.capa.getTransitionName());
                    views[2] = new Pair<View, String>(holder.nome, holder.nome.getTransitionName());

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, views);

                    i.putExtra("turma", t);

                    if(professor)
                        i.putExtra("usuario", PROFESSOR);
                    else
                        i.putExtra("usuario", ALUNO);

                    layoutInflater.getContext().startActivity(i, options.toBundle());
                }
                else
                {
                    // Se a turma não tiver PIN
                    if(t.getPin().equals(""))
                    {
                        new AlertDialog.Builder(c)
                                .setTitle(R.string.warning)
                                .setMessage(R.string.deseja_solicitacao)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i)
                                    {
                                        enviarSolicitacaoTurma(t, uid);

                                        new AlertDialog.Builder(c)
                                                .setMessage(R.string.solicitacao_sucesso)
                                                .setPositiveButton(R.string.Ok, null)
                                                .show();
                                    }
                                })
                                .setNegativeButton(R.string.no, null)
                                .show();
                    }
                    else
                    {
                        final View v         = layoutInflater.inflate(R.layout.dialog_pin, null);
                        final EditText pinEt = v.findViewById(R.id.dialog_pin);

                        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(c)
                                .setView(v)
                                .setPositiveButton(R.string.Ok, null)
                                .setNegativeButton(R.string.cancel, null)
                                .create();

                        dialog.show();

                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                String pin = pinEt.getText().toString();

                                AlertDialog.Builder alerta = new AlertDialog.Builder(c)
                                        .setTitle(R.string.warning)
                                        .setPositiveButton(R.string.Ok, null);

                                if(pin.equals(t.getPin()))
                                {
                                    enviarSolicitacaoTurma(t, uid);
                                    alerta.setMessage(R.string.solicitacao_sucesso);
                                    dialog.dismiss();
                                }
                                else
                                    alerta.setMessage(R.string.wrong_pin);

                                alerta.show();
                            }
                        });
                    }
                }
            }
        });
    }

    public void add(Turma t)
    {
        listaTurmas.add(0, t);
    }

    public void clear()
    {
        listaTurmas.clear();
        notifyDataSetChanged();
    }

    public List<Turma> getLista() { return listaTurmas; }

    @Override
    public int getItemCount() {
        return listaTurmas.size();
    }

    public class TurmaViewHolder extends RecyclerView.ViewHolder
    {
        public CardView  cardViewFotoProf;
        public ImageView fotoProf;
        public ImageView capa;
        public ImageView excluir;
        public TextView  nome;
        public TextView  dia;

        public TurmaViewHolder(final View itemView)
        {
            super(itemView);

            cardViewFotoProf = itemView.findViewById(R.id.cardview_foto_turma);
            fotoProf         = itemView.findViewById(R.id.icon_turma_professor);
            capa             = itemView.findViewById(R.id.turma_capa);
            excluir          = itemView.findViewById(R.id.turma_excluir);
            nome             = itemView.findViewById(R.id.turma_nome);
            dia              = itemView.findViewById(R.id.turma_dia);
        }
    }

    private void enviarSolicitacaoTurma(Turma t, String uid)
    {
        FirebaseDatabase.getInstance().getReference()
            .child("turmas")
            .child(t.getId())
            .child("solicitacoes")
            .child(uid).setValue(true);
    }
}
