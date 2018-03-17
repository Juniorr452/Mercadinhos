package com.mobile.pid.pid;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AtualizarPerfilActivity extends AppCompatActivity
{
    // TODO: Alterar para os tipos apropriados + tarde
    // TODO: Carregar os dados do banco para os campos dps que terminar de fazer o layout.
    EditText etNome;
    EditText etSexo;
    EditText etData;

    DatabaseReference usuarioDatabaseRef;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_perfil);

        etNome = ((TextInputLayout) findViewById(R.id.TIL_atualizar_nome)).getEditText();
        etSexo = ((TextInputLayout) findViewById(R.id.TIL_atualizar_sexo)).getEditText();
        etData = ((TextInputLayout) findViewById(R.id.TIL_atualizar_data)).getEditText();

        user = FirebaseAuth.getInstance().getCurrentUser();
        usuarioDatabaseRef = FirebaseDatabase.getInstance().getReference().child("usuarios").child(user.getUid());
    }

    public void botaoAtualizarPerfil(View v)
    {
        String nome = etNome.getText().toString();
        String sexo = etSexo.getText().toString();
        String data = etData.getText().toString();

        atualizarPerfil(nome, sexo, data);
    }

    public void atualizarPerfil(String nome, String sexo, String dataNasc)
    {
        usuarioDatabaseRef.child("nome").setValue(nome);
        usuarioDatabaseRef.child("sexo").setValue(sexo);
        usuarioDatabaseRef.child("data_nasc").setValue(dataNasc);

        Toast.makeText(this, "Dados atualizados com sucesso", Toast.LENGTH_SHORT).show();
        finish();
    }
}
