package com.mobile.pid.pid.home.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by junio on 12/03/2018.
 */

public class TurmaAdapter extends RecyclerView.Adapter<TurmaAdapter.TurmaViewHolder>
{
    private static final int PROFESSOR = 0;
    private static final int ALUNO = 1;

    private List<Turma>    listaTurmas;
    private LayoutInflater layoutInflater;

    public TurmaAdapter(Context c, List<Turma> l)
    {
        listaTurmas    = l;
        layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                                DatabaseReference rootRef     = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference turmasCriadasRef = rootRef.child("userTurmasCriadas");
                                DatabaseReference turmasMatriculadasRef = rootRef.child("userTurmasMatriculadas");


                                String tuid = t.getId();

                                // Deletar turma.
                                rootRef.child("turmas")
                                    .child(tuid)
                                    .removeValue();

                                // Deletar no turmas_criadas do professor.
                                turmasCriadasRef.child(t.getProfessorUid())
                                    .child(tuid)
                                    .removeValue();

                                // TODO: Verificar se está funcionando
                                // Deletar no turmas_matriculadas dos alunos.
                                if(t.getAlunos() != null)
                                    for(String auid : t.getAlunos().keySet())
                                        turmasMatriculadasRef.child(auid)
                                            .child(tuid)
                                            .removeValue();
                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

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
                    Intent i = new Intent(layoutInflater.getContext(), DetalhesTurma.class);

                    i.putExtra("turma", t);

                    if(professor)
                        i.putExtra("usuario", PROFESSOR);
                    else
                        i.putExtra("usuario", ALUNO);

                    layoutInflater.getContext().startActivity(i);
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

    public class TurmaViewHolder extends RecyclerView.ViewHolder //implements View.OnClickListener, View.OnLongClickListener
    {
        public ImageView fotoProf;
        public ImageView capa;
        public ImageView excluir;
        public TextView  nome;
        public TextView  dia;

        public TurmaViewHolder(final View itemView)
        {
            super(itemView);

            fotoProf = itemView.findViewById(R.id.icon_turma_professor);
            capa   = itemView.findViewById(R.id.turma_capa);
            excluir = itemView.findViewById(R.id.turma_excluir);
            nome   = itemView.findViewById(R.id.turma_nome);
            dia    = itemView.findViewById(R.id.turma_dia);
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
