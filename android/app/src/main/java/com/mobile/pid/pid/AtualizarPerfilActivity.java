package com.mobile.pid.pid;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobile.pid.pid.login.Usuario;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Locale;

public class AtualizarPerfilActivity extends AppCompatActivity
{
    private static final String TAG = "AtualizarPerfilActivity";

    private static final String MASCULINO = "Masculino";
    private static final String FEMININO  = "Feminino";

    // TODO: Alterar para os tipos apropriados + tarde
    // TODO: Carregar os dados do banco para os campos dps que terminar de fazer o layout.
    EditText etNome;
    RadioButton rbMasculino;
    RadioButton rbFeminino;
    Button btnData;

    String sexo;
    String dataNasc;

    DatabaseReference usuarioDatabaseRef;
    Usuario user;

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

        atualizarPerfilToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        atualizarPerfilToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        user = UsuarioLogado.user;
        usuarioDatabaseRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(user.getUid());

        // Colocar dados nos campos
        etNome.setText(UsuarioLogado.user.getNome());

        sexo = user.getSexo();
        if (sexo != null)
        {
            if (sexo.equals(MASCULINO))
                rbMasculino.setChecked(true);
            else
                rbFeminino.setChecked(true);
        }
        else
            rbMasculino.setChecked(true);

        dataNasc = user.getDataNascimento();
        if (dataNasc != null)
            btnData.setText(dataNasc);
    }

    public void botaoAtualizarPerfil(View v)
    {
        String nome = etNome.getText().toString();

        Log.d(TAG, "Sexo: " + sexo);
        Log.d(TAG, "Data: " + dataNasc);

        if (validarCampos(nome, dataNasc))
            atualizarPerfil(nome, sexo, dataNasc);
        else
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
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
                sexo = MASCULINO;
                break;

            case R.id.atualizar_perfil_radio_feminino:
                sexo = FEMININO;
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

        Toast.makeText(this, "Dados atualizados com sucesso", Toast.LENGTH_SHORT).show();
        finish();
    }
}
