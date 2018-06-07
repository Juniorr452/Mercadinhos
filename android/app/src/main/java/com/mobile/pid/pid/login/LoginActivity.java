package com.mobile.pid.pid.login;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mobile.pid.pid.R;

public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = "LoginActivity";

    private FirebaseAuth auth;

    ProgressDialog progressDialog;

    private EditText email;
    private EditText senha;

    private ImageView btn_voltar;
    //private TextView tv_remember;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = ((TextInputLayout) findViewById(R.id.TILemail)).getEditText();
        senha = ((TextInputLayout) findViewById(R.id.TILpassword)).getEditText();
        btn_voltar = (ImageView) findViewById(R.id.btn_voltar);

        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.entrando));
        progressDialog.setMessage(getString(R.string.fazendo_login));
        /*tv_remember = (TextView) findViewById(R.id.tv_remember);

        tv_remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder= new AlertDialog.Builder(LoginActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_esqueci_senha, null);

                final TextInputLayout TIL_dialog_email_remember = (TextInputLayout) mView.findViewById(R.id.TIL_dialog_email_remember);
                Button btn_cancel_remember = (Button) mView.findViewById(R.id.btn_cancel_remember);
                Button btn_send_email = (Button) mView.findViewById(R.id.btn_send_email);

                btn_send_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(LoginActivity.this, R.string.success_send_email, Toast.LENGTH_SHORT).show();
                    }
                });

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                btn_cancel_remember.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });*/

        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        auth.addAuthStateListener(new LoginAuthStateListener(this));
    }

    public void entrar(View v)
    {
        String emailText = email.getText().toString();
        String senhaText = senha.getText().toString();

        progressDialog.show();

        try
        {
            validarCampos(emailText, senhaText);
            auth.signInWithEmailAndPassword(emailText, senhaText).addOnCompleteListener(new LoginOnCompleteListener(this, progressDialog));
        }
        catch(NoSuchFieldException e)
        {
            progressDialog.dismiss();

            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle(R.string.warning)
                    .setMessage(e.getMessage())
                    .show();
        }
    }

    public void validarCampos(String email, String senha) throws NoSuchFieldException
    {
        if(email.isEmpty())
            throw new NoSuchFieldException(getString(R.string.preencha_email));

        if(senha.isEmpty())
            throw new NoSuchFieldException("Informe sua senha e tente novamente.");
    }

    public void esqueciSenha(View v)
    {
        AlertDialog.Builder mBuilder= new AlertDialog.Builder(LoginActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_esqueci_senha, null);

        final TextInputLayout TIL_dialog_email_remember = (TextInputLayout) mView.findViewById(R.id.TIL_dialog_email_remember);
        final EditText email     = mView.findViewById(R.id.dialog_email_forgot);
        Button btn_cancel_forgot = mView.findViewById(R.id.btn_cancel_forgot);
        Button btn_send_email    = mView.findViewById(R.id.btn_send_email);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btn_send_email.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // TODO: Validar e-mail
                String emailText = email.getText().toString();

                FirebaseAuth.getInstance().sendPasswordResetEmail(emailText).addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                            Toast.makeText(LoginActivity.this, R.string.success_send_email, Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(LoginActivity.this, R.string.failure_send_email_pass, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btn_cancel_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }
}
