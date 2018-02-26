package com.mobile.pid.pid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // TODO: Verificar se o usuário está logado e rediricionar já pra activity de turmas
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
