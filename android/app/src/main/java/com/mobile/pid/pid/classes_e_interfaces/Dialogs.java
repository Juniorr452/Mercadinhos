package com.mobile.pid.pid.classes_e_interfaces;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.mobile.pid.pid.home.feed.FeedFunctions;
import com.mobile.pid.pid.home.perfil.AtualizarPerfilActivity;
import com.mobile.pid.pid.home.perfil.UsuarioPerfilActivity;

import java.io.File;
import java.util.Date;

public class Dialogs
{
    public static final int RC_CAMERA       = 0;
    public static final int RC_PHOTO_PICKER = 1;

    public static void desmatricularAluno(Context c, final Turma t, final String uid)
    {
        new android.support.v7.app.AlertDialog.Builder(c, R.style.DialogTheme)
            .setTitle("Sair da turma?")
            .setMessage("Deseja realmente sair da turma?")
            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    t.desmatricularAluno(uid);
                }
            })
            .setNegativeButton("Não", null)
            .show();
    }

    public static void excluirTurma(Context c, final Turma t)
    {
        new android.support.v7.app.AlertDialog.Builder(c, R.style.DialogTheme)
            .setTitle("Excluir a turma?")
            .setMessage("Deseja realmente excluir a turma?")
            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    t.excluir();
                }
            })
            .setNegativeButton("Não", null)
            .show();
    }

    public static void mensagem(Context c, String titulo, String mensagem)
    {
        new AlertDialog.Builder(c)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton(R.string.Ok, null)
                .show();
    }

    public static void mensagem(Context c, int titulo, int mensagem)
    {
        new AlertDialog.Builder(c)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton(R.string.Ok, null)
                .show();
    }

    public static void mensagem(Context c, int titulo, String mensagem)
    {
        new AlertDialog.Builder(c)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton(R.string.Ok, null)
                .show();
    }

    public static ProgressDialog dialogEnviandoImagem(Context c)
    {
        final ProgressDialog progressDialog = new ProgressDialog(c, R.style.DialogTheme);
        progressDialog.setTitle("Enviando imagem...");
        progressDialog.setMessage("Aguarde...");
        progressDialog.show();

        return progressDialog;
    }

    // AO CLICAR NA FOTO DO USUARIO, CRIA UM DIALOG MOSTRANDO ELA EM TAMANHO REAL.
    public static void mostrarImagem(Activity activity, String fotoUrl)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(activity.getLayoutInflater().inflate(R.layout.image_fullsize, null));
        ImageView image_user_fullsize = dialog.findViewById(R.id.image_user_fullsize);

        Glide.with(activity).load(fotoUrl).into(image_user_fullsize);
        dialog.show();

        image_user_fullsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    // https://cursos.alura.com.br/forum/topico-usando-a-camera-22771
    public static void dialogSelecionarImagem(final Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);

        View mView = activity.getLayoutInflater().inflate(R.layout.dialog_selecionar_foto, null);
        builder.setView(mView);

        final ImageView camera = mView.findViewById(R.id.camera);
        final ImageView galeria = mView.findViewById(R.id.galeria);

        final AlertDialog dialog = builder.create();
        dialog.show();

        camera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    //File caminhoFoto = new File(activity.getFilesDir(), "imagens");
                    //File arquivoFoto = new File(caminhoFoto, getNomeArquivo() + ".jpg");

                    //Uri uri = FileProvider.getUriForFile(activity, "com.pid.fileprovider", arquivoFoto);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    //intent.putExtra("uri", uri);
                    //intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    activity.startActivityForResult(intent, RC_CAMERA);
                }
                catch(Exception e)
                {
                    new AlertDialog.Builder(activity)
                        .setTitle(R.string.warning)
                        .setMessage("Erro: " + e.getMessage())
                        .setPositiveButton(R.string.Ok, null)
                        .show();
                }

                dialog.dismiss();
            }
        });

        galeria.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

                activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.completar_acao)), RC_PHOTO_PICKER);
                dialog.dismiss();
            }
        });
    }

    public static void dialogAvisoTurma(final Context context, final String tid)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_criar_aviso_turma, null);
        final EditText etAviso = view.findViewById(R.id.et_aviso);

        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.DialogTheme)
            .setView(view)
            .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Toast.makeText(context, "Aviso postado com sucesso", Toast.LENGTH_SHORT).show();

                    new AvisoTurma(etAviso.getText().toString()).postarAviso(tid);
                }
            })
            .setNegativeButton(R.string.cancel, null)
            .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        // Não tá funfando
        final TextWatcher textWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2)
            {
                if(s.length() > 1)
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                else
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        etAviso.addTextChangedListener(textWatcher);
    }

    public static void dialogUsuario(final Usuario u, final Context context)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_perfil_usuario, null);
        final String usuario = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final AlertDialog.Builder mBuilderUsuario = new AlertDialog.Builder(context, R.style.DialogTheme);
        mBuilderUsuario.setView(view);

        final Button seguir = view.findViewById(R.id.seguir);
        final TextView nome = view.findViewById(R.id.nome);
        final ImageView foto = view.findViewById(R.id.foto);
        final TextView count_seguindo = view.findViewById(R.id.seguindo);
        final TextView count_seguidores = view.findViewById(R.id.seguidores);

        final AlertDialog dialogUsuario = mBuilderUsuario.create();
        dialogUsuario.show();

        /////////////////////////////// SETAR AS INFORMACOES DO DIALOG ////////////////////////////
        if(u.getUid().equals(usuario)) {
            seguir.setVisibility(View.GONE);
        } else {
            FirebaseDatabase.getInstance().getReference("userSeguindo").child(usuario).child(u.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()) {
                                seguir.setText(context.getText(R.string.following));
                            } else {
                                seguir.setText(context.getText(R.string.follow));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        nome.setText(u.getNome());
        Glide.with(context)
                .load(u.getFotoUrl())
                .into(foto);

        FirebaseDatabase.getInstance().getReference("userSeguindo").child(u.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            count_seguindo.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                        } else {
                            count_seguindo.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("userSeguidores").child(u.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            count_seguidores.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                        } else {
                            count_seguidores.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        //////////////////////////////////////////////////////////////////////////

        seguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DatabaseReference dbSeguidores = FirebaseDatabase.getInstance().getReference("userSeguidores");
                final DatabaseReference dbSeguindo = FirebaseDatabase.getInstance().getReference("userSeguindo");
                final String usuario = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if(seguir.getText().toString().equals(context.getText(R.string.following))) {
                    dialogUsuario.dismiss();

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(context, R.style.DialogTheme);
                    mBuilder.setTitle(R.string.confirmacao)
                            .setMessage(context.getText(R.string.unfollow_message) + " "+ u.getNome() + "?")
                            .setPositiveButton(R.string.confirmar, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogUsuario.show();
                                    seguir.setText(R.string.follow);

                                    // EXCLUI NOS "SEGUINDO" DO USUARIO LOGADO
                                    dbSeguindo.child(usuario).child(u.getUid()).removeValue();
                                    // EXCLUI NOS "SEGUIDORES" DO USUARIO
                                    dbSeguidores.child(u.getUid()).child(usuario).removeValue();
                                    // REMOVE TODOS OS POSTS DO USUARIO UNFOLLOW
                                    FeedFunctions.excluirPostsFollow(usuario, u.getUid());

                                    seguir.setText(context.getText(R.string.follow));
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogUsuario.show();
                                }
                            });

                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.gray_font));
                } else {
                    seguir.setText(context.getText(R.string.following));

                    // ADICIONA NOS "SEGUINDO" DO USUARIO LOGADO
                    dbSeguindo.child(usuario).child(u.getUid()).setValue(true);
                    // ADICIONA NOS "SEGUIDORES" DO USUARIO
                    dbSeguidores.child(u.getUid()).child(usuario).setValue(true);
                }
            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!u.getUid().equals(usuario)) {
                    Intent i = new Intent(context, UsuarioPerfilActivity.class);
                    i.putExtra("usuario", u.getUid());
                    context.startActivity(i);
                }

            }
        });
    }

    public void dialogTurma(final Turma t, final Context context)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_detalhe_turma, null);
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final AlertDialog.Builder mBuilderTurma = new AlertDialog.Builder(context, R.style.DialogTheme);
        mBuilderTurma.setView(view);

        final Button solicitar = view.findViewById(R.id.solicitar);
        final TextView nome = view.findViewById(R.id.nome);
        final ImageView capa = view.findViewById(R.id.capa);
        final ImageView foto =  view.findViewById(R.id.foto);
        final TextView count_alunos = view.findViewById(R.id.count_alunos);

        final AlertDialog dialogTurma = mBuilderTurma.create();
        dialogTurma.show();

        nome.setText(t.getNome());
        Glide.with(context)
                .load(t.getCapaUrl())
                .into(capa);

        carregarFotoProfessor(foto, t);

        count_alunos.setText(String.valueOf(t.getQtdAlunos()));

        if(t.estaNaTurma(uid))
            solicitar.setVisibility(View.GONE);
        else if(t.enviouSolicitacao(uid))
            desabilitarBotaoSolicitar(solicitar);
        else
            solicitar.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    // Se a turma não tiver PIN
                    if(t.getPin().equals(""))
                    {
                        new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme)
                            .setTitle(R.string.warning)
                            .setMessage(R.string.deseja_solicitacao)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    enviarSolicitacaoTurma(t, uid);
                                    desabilitarBotaoSolicitar(solicitar);

                                    new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme)
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
                        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        final View v         = layoutInflater.inflate(R.layout.dialog_pin, null);
                        final EditText pinEt = v.findViewById(R.id.dialog_pin);

                        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context, R.style.DialogTheme)
                                .setView(v)
                                .setPositiveButton(R.string.Ok, null)
                                .setNegativeButton(R.string.cancel, null)
                                .create();

                        dialog.show();

                        dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                String pin = pinEt.getText().toString();

                                android.support.v7.app.AlertDialog.Builder alerta = new android.support.v7.app.AlertDialog.Builder(context, R.style.DialogTheme)
                                        .setTitle(R.string.warning)
                                        .setPositiveButton(R.string.Ok, null);

                                if(pin.equals(t.getPin()))
                                {
                                    enviarSolicitacaoTurma(t, uid);
                                    desabilitarBotaoSolicitar(solicitar);
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
            });
    }

    private void carregarFotoProfessor(final ImageView foto, Turma t) {
        FirebaseDatabase.getInstance().getReference("usuarios").child(t.getProfessorUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario u = dataSnapshot.getValue(Usuario.class);
                        Glide.with(foto).load(u.getFotoUrl()).into(foto);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void enviarSolicitacaoTurma(Turma t, String uid)
    {
        FirebaseDatabase.getInstance().getReference()
                .child("turmas")
                .child(t.getId())
                .child("solicitacoes")
                .child(uid).setValue(true);

        t.adicionarSolicitacao(uid);
    }

    private void desabilitarBotaoSolicitar(Button solicitar)
    {
        Context context = solicitar.getContext();

        solicitar.setText(context.getText(R.string.solicitado));
        solicitar.setClickable(false);
        solicitar.setTextColor(context.getResources().getColor(R.color.gray_font));
    }

    // https://stackoverflow.com/questions/21388586/get-uri-from-camera-intent-in-android
    private static String getNomeArquivo() {
        return DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".jpg";
    }
}