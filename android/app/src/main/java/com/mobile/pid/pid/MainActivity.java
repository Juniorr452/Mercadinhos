package com.mobile.pid.pid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
{
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference testeDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        testeDatabaseReference = firebaseDatabase.getReference().child("Teste");
    }

    public void entrar(View v)
    {
        Toast.makeText(this, "Enviando mensagem para o db...", Toast.LENGTH_SHORT).show();
        testeDatabaseReference.push().setValue("Hello World");
    }
}
