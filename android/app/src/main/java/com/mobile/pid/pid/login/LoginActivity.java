package com.mobile.pid.pid.login;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.pid.pid.R;

public class LoginActivity extends AppCompatActivity {

    private ImageView btn_voltar;
    private TextView tv_remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_voltar = (ImageView) findViewById(R.id.btn_voltar);
        tv_remember = (TextView) findViewById(R.id.tv_remember);

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
        });

        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
        });
    }
}
