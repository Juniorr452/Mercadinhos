package com.mobile.pid.pid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RedesSociaisActivity extends AppCompatActivity {

    private Button btn_entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redes_sociais);

        btn_entrar = (Button) findViewById(R.id.btn_entrar);
    }
}
