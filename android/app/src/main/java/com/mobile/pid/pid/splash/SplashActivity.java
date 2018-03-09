package com.mobile.pid.pid.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobile.pid.pid.home.HomeActivity;
import com.mobile.pid.pid.login.RedesSociaisActivity;

public class SplashActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Usuário não está logado
        if (user == null)
            startActivity(new Intent(this, RedesSociaisActivity.class));
        else
            startActivity(new Intent(this, HomeActivity.class));

        finish();
    }
}
