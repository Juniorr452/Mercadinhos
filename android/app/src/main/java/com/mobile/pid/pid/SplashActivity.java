package com.mobile.pid.pid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // Usuário não está logado
        if (user == null)
            startActivity(new Intent(this, MainActivity.class));
        else
        {
            // TODO: Rediricionar pra activity principal
            Toast.makeText(this, "Saindo da conta " + user.getEmail() +  "...", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();

            startActivity(new Intent(this, MainActivity.class));
        }

        finish();
    }
}
