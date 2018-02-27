package com.mobile.pid.pid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";

    private static final int RC_GOOGLE_SIGN_IN   = 1;

    private GoogleApiClient googleApiClient;
    private CallbackManager facebookCallbackManager;

    private FirebaseAuth                   firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private FirebaseDatabase  firebaseDatabase;
    private DatabaseReference testeDatabaseReference;

    // Listener para quando logar com FirebaseAuth, seja por meio de e-mail, google ou facebook.
    private OnCompleteListener<AuthResult> firebaseSignInListener = new OnCompleteListener<AuthResult>()
    {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task)
        {
            if (task.isSuccessful())
                Log.d(TAG, "signInWithCredential:success");
            else
            {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithCredential:failure", task.getException());
                Toast.makeText(MainActivity.this, "Falha na autenticação. Tente logar com outro serviço.", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth     = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        testeDatabaseReference = firebaseDatabase.getReference().child("Teste");

        // Auth Listener
        authStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
            //  TODO: Mandar pra activity principal
            if (firebaseAuth.getCurrentUser() != null)
            {
                Toast.makeText(MainActivity.this,
                        "Logado como " + firebaseAuth.getCurrentUser().getDisplayName(),
                        Toast.LENGTH_LONG).show();
            }
            }
        };

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

        // Facebook - Facebook Quick Start e https://www.youtube.com/watch?v=mapLbSKNc6I
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        facebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(facebookCallbackManager,
            new FacebookCallback<LoginResult>()
            {
                @Override
                public void onSuccess(LoginResult loginResult)
                {
                    Log.d(TAG, "facebook:onSuccess:" + loginResult);
                    firebaseAuthWithFacebook(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Log.d(TAG, "facebook:onCancel");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d(TAG, "facebook:onError", error);
                }
            });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN)
        {
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
        }
        else if (requestCode == FacebookSdk.getCallbackRequestCodeOffset())
        {
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, firebaseSignInListener);
    }

    private void firebaseAuthWithFacebook(AccessToken token)
    {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, firebaseSignInListener);
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

    public void entrarFacebook(View v)
    {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }
}