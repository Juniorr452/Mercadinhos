package com.mobile.pid.pid.home.perfil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobile.pid.pid.R;
import com.mobile.pid.pid.classes_e_interfaces.Dialogs;
import com.mobile.pid.pid.classes_e_interfaces.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.Calendar;

public class AtualizarPerfilActivity extends AppCompatActivity
{
    private static final String TAG = "AtualizarPerfilActivity";

    ProgressBar  progressBar;
    LinearLayout conteudo;

    EditText etNome;
    RadioButton rbMasculino;
    RadioButton rbFeminino;
    Button btnData;
    ImageView imageView_user_blur;
    ImageView imageView_user;

    Uri imagemUri;

    String sexo;
    String dataNasc;

    // FIREBASE
    FirebaseUser user;
    FirebaseAuth auth;
    String user_id;

    DatabaseReference usuarioDatabaseRef;
    StorageReference usuarioStorageRef;

    private ValueEventListener userListener;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_perfil);

        Toolbar atualizarPerfilToolbar = findViewById(R.id.toolbar_atualizar_perfil);

        progressBar = findViewById(R.id.pb_atualizar_perfil);
        conteudo    = findViewById(R.id.ll_atualizar_perfil);

        etNome      = ((TextInputLayout) findViewById(R.id.TIL_atualizar_nome)).getEditText();
        rbMasculino = findViewById(R.id.atualizar_perfil_radio_masculino);
        rbFeminino  = findViewById(R.id.atualizar_perfil_radio_feminino);
        btnData     = findViewById(R.id.btn_atualizar_data);
        imageView_user_blur = findViewById(R.id.imageView_user_blur);
        imageView_user = findViewById(R.id.imageView_user);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Atualizando...");
        progressDialog.setMessage("Atualizando seu perfil...");

        conteudo.setVisibility(View.GONE);

        atualizarPerfilToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        atualizarPerfilToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        user_id = user.getUid();

        usuarioDatabaseRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(user_id);
        usuarioStorageRef  = FirebaseStorage.getInstance().getReference().child("usuarios").child(user_id).child("fotoPerfil");

        // Pegar os dados do usuário
        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario user_logado = dataSnapshot.getValue(Usuario.class);
                sexo = user_logado.getSexo();
                dataNasc = user_logado.getDataNascimento();
                etNome.setText(user_logado.getNome());

                if (sexo != null)
                {
                    if (sexo.equals(getString(R.string.male)))
                        rbMasculino.setChecked(true);
                    else
                        rbFeminino.setChecked(true);
                }
                else
                    rbMasculino.setChecked(false);

                if (dataNasc != null)
                    btnData.setText(dataNasc);

                // adiciona a foto com efeito embaçado
                Glide.with(getApplicationContext()).load(user_logado.getFotoUrl())
                        .apply(new RequestOptions()
                                .override(20,20)
                                .error(android.R.drawable.dark_header))
                        .into(imageView_user_blur);

                Glide.with(getApplicationContext()).load(user_logado.getFotoUrl()).into(imageView_user);

                progressBar.setVisibility(View.GONE);
                conteudo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        imageView_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialogs.dialogSelecionarImagem(AtualizarPerfilActivity.this);
            }
        });

        usuarioDatabaseRef.addListenerForSingleValueEvent(userListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        usuarioDatabaseRef.removeEventListener(userListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            Log.d(TAG, "Deu");
            switch (requestCode)
            {
                case Dialogs.RC_CAMERA:
                    Bitmap b = (Bitmap) data.getExtras().get("data");

                    imageView_user.setImageBitmap(b);
                case Dialogs.RC_PHOTO_PICKER:
                    imagemUri = data.getData();
                    carregarImagemNasViews(imagemUri);
                    break;
            }
        }
        else
        {
            Log.d(TAG, resultCode + " Não deu " + data.getAction());
        }
    }

    private void carregarImagemNasViews(Object imagem)
    {
        Glide.with(this).load(imagem).into(imageView_user);
        Glide.with(this).load(imagem)
                .apply(new RequestOptions()
                        .override(20, 20)
                        .error(android.R.drawable.dark_header))
                .into(imageView_user_blur);
    }

    public void botaoAtualizarPerfil(View v)
    {
        String nome = etNome.getText().toString();

        Log.d(TAG, R.string.gender + sexo);
        Log.d(TAG, R.string.date + dataNasc);

        if (validarCampos(nome, dataNasc))
            atualizarPerfil(nome.trim(), sexo.trim(), dataNasc.trim());
        else
            Toast.makeText(this, R.string.fill_fields, Toast.LENGTH_SHORT).show();
    }

    public boolean validarCampos(String nome, String data)
    {
        if(nome.equals("") || data.equals(""))
            return false;

        return true;
    }

    public void onRadioItemChanged(View v)
    {
        int id = v.getId();

        switch(id)
        {
            case R.id.atualizar_perfil_radio_masculino:
                sexo = getString(R.string.male);
                break;

            case R.id.atualizar_perfil_radio_feminino:
                sexo = getString(R.string.female);
                break;
        }
    }

    // https://www.youtube.com/watch?v=5qdnoRHfAYU
    public void escolherData(View v)
    {
        Calendar c = Calendar.getInstance();

        // Pegar data atual
        int ano = c.get(Calendar.YEAR) - 20;
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AtualizarPerfilActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mesDoAno, int dia)
            {
                String mes = new DateFormatSymbols().getMonths()[mesDoAno];

                dataNasc = dia + " de " + mes + " de " + ano;
                btnData.setText(dataNasc);
            }
        }, ano, mes, dia);

        datePickerDialog.show();
    }

    public void atualizarPerfil(String nome, String sexo, String dataNasc)
    {
        progressDialog.show();

        usuarioDatabaseRef.child("nome").setValue(nome);
        usuarioDatabaseRef.child("sexo").setValue(sexo);
        usuarioDatabaseRef.child("dataNascimento").setValue(dataNasc);

        final UserProfileChangeRequest.Builder dadosPAtt = new UserProfileChangeRequest.Builder().setDisplayName(nome);

        // Upar e atualizar foto, se existir.
        if (imagemUri != null)
            usuarioStorageRef.putFile(imagemUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        String fotoUrl = task.getResult().getDownloadUrl().toString();
                        usuarioDatabaseRef.child("fotoUrl").setValue(fotoUrl);

                        dadosPAtt.setPhotoUri(Uri.parse(fotoUrl));
                    }
                }
            });

        // Atualizar no Auth
        user.updateProfile(dadosPAtt.build()).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                progressDialog.dismiss();

                if (task.isSuccessful())
                {
                    Toast.makeText(AtualizarPerfilActivity.this
                            , R.string.updated_data, Toast.LENGTH_SHORT).show();

                    finish();
                }
                else
                    Log.e(TAG, task.getException().toString());

            }
        });
    }
}
