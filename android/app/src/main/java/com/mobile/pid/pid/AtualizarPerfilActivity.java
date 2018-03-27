package com.mobile.pid.pid;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.pid.pid.login.Usuario;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class AtualizarPerfilActivity extends AppCompatActivity
{
    private static final String TAG = "AtualizarPerfilActivity";

    // TODO: Alterar para os tipos apropriados + tarde
    // TODO: Carregar os dados do banco para os campos dps que terminar de fazer o layout.
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // TODO ATUALIZAR NOME DO PERFIL NO AUTH
        // adiciona a foto com efeito embaçado
        Glide.with(this).load(user.getPhotoUrl()).override(20,20).error(android.R.drawable.dark_header).into(imageView_user_blur);
        Glide.with(this).load(user.getPhotoUrl()).into(imageView_user);

        usuarioDatabaseRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(user_id);

        usuarioDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_perfil);

        Toolbar atualizarPerfilToolbar = findViewById(R.id.toolbar_atualizar_perfil);

        etNome      = ((TextInputLayout) findViewById(R.id.TIL_atualizar_nome)).getEditText();
        rbMasculino = findViewById(R.id.atualizar_perfil_radio_masculino);
        rbFeminino  = findViewById(R.id.atualizar_perfil_radio_feminino);
        btnData     = findViewById(R.id.btn_atualizar_data);
        imageView_user_blur = findViewById(R.id.imageView_user_blur);
        imageView_user = findViewById(R.id.imageView_user);

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

        imageView_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = new String[] {"Tirar foto", "Escolher na galeria"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AtualizarPerfilActivity.this, android.R.layout.select_dialog_item, items);
                AlertDialog.Builder builder = new AlertDialog.Builder(AtualizarPerfilActivity.this);
                builder.setTitle(R.string.select_image);

                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0) {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/jpeg");
                            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                            startActivityForResult(Intent.createChooser(intent, getString(R.string.completar_acao)), 1);
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }
        // TODO FAZER PEGAR A FOTO QUE FOI SELECIONADA OU FOI TIRADA
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK)
        {
            //SE TIRAR FOTO
        } else {
            imagemUri = data.getData();
            imageView_user.setImageURI(imagemUri);
            imageView_user_blur.setImageURI(imagemUri);
        }
    }

    public void botaoAtualizarPerfil(View v)
    {
        String nome = etNome.getText().toString();

        Log.d(TAG, R.string.gender + sexo);
        Log.d(TAG, R.string.date + dataNasc);

        if (validarCampos(nome, dataNasc))
            atualizarPerfil(nome, sexo, dataNasc);
        else
            Toast.makeText(this, R.string.fill_fields, Toast.LENGTH_SHORT).show();
    }

    public boolean validarCampos(String nome, String data)
    {
        if(nome == null || data == null)
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
        usuarioDatabaseRef.child("nome").setValue(nome);
        usuarioDatabaseRef.child("sexo").setValue(sexo);
        usuarioDatabaseRef.child("dataNascimento").setValue(dataNasc);

        Toast.makeText(AtualizarPerfilActivity.this
                , R.string.updated_data, Toast.LENGTH_SHORT).show();
        finish();
    }
}
