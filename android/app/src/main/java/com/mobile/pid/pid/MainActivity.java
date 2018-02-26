package com.mobile.pid.pid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";

    private static final int RC_GOOGLE_SIGN_IN = 1;

    private GoogleApiClient googleApiClient;

    private FirebaseAuth     firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference testeDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth     = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        testeDatabaseReference = firebaseDatabase.getReference().child("Teste");

        // Google Sign In - https://www.youtube.com/watch?v=-ywVw2O1pP8
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener()
                {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this, "Erro ao conectar com Google API Client", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    public void entrar(View v)
    {
        Toast.makeText(this, "Enviando mensagem para o db...", Toast.LENGTH_SHORT).show();
        testeDatabaseReference.push().setValue("Hello World");
    }

    public void entrarGoogle(View v)
    {
        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(googleSignInIntent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case RC_GOOGLE_SIGN_IN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

                if (result.isSuccess())
                {
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                }
                else
                {
                    Log.e(TAG, "GOOGLE_SIGN_IN ERROR: " + result.getStatus());
                    Toast.makeText(MainActivity.this, "Erro ao logar com sua conta do Google. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                }


                break;
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // TODO: Mandar pra activity principal
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, user.getEmail(), Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Falha na autenticação", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
