package com.mobile.pid.pid.login;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.pid.pid.R;

public class RedesSociaisActivity extends AppCompatActivity {

    private ImageView btn_email;
    private TextView tv_criarConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redes_sociais);

        //OBJETOS DA VIEW
        btn_email = (ImageView) findViewById(R.id.btn_email);
        tv_criarConta = (TextView) findViewById(R.id.tv_criarConta);

        // LOGAR POR EMAIL
        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RedesSociaisActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // CRIAR UMA NOVA CONTA
        tv_criarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(RedesSociaisActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_criar_conta, null);

                final TextInputLayout TIL_dialog_username = (TextInputLayout) mView.findViewById(R.id.TIL_dialog_username);
                final TextInputLayout TIL_dialog_email = (TextInputLayout) mView.findViewById(R.id.TIL_dialog_email);
                final TextInputLayout TIL_dialog_password = (TextInputLayout) mView.findViewById(R.id.TIL_dialog_password);
                final TextInputLayout TIL_dialog_password_confirm = (TextInputLayout) mView.findViewById(R.id.TIL_dialog_password_confirm);
                Button btn_create_account = (Button) mView.findViewById(R.id.btn_create_account);
                Button btn_cancel_account = (Button) mView.findViewById(R.id.btn_cancel_account);

                btn_create_account.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(!TIL_dialog_email.getEditText().getText().toString().isEmpty()) {
                            Toast.makeText(RedesSociaisActivity.this, R.string.success_create_login, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RedesSociaisActivity.this, R.string.error_create_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                btn_cancel_account.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });
    }
}
