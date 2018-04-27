package com.mobile.pid.pid.login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobile.pid.pid.R;

import java.util.Arrays;

public class RedesSociaisActivity extends AppCompatActivity
{
    private static final String TAG = "RedesSociaisActivity";

    private static final int RC_GOOGLE_SIGN_IN   = 1;

    private GoogleApiClient googleApiClient;
    private CallbackManager facebookCallbackManager;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    // Listeners
    private LoginAuthStateListener  authStateListener;
    private LoginOnCompleteListener loginOnCompleteListener;

    ProgressDialog progressDialog;

    private ImageView btn_email;
    private TextView tv_criarConta;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redes_sociais);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.entrando));
        progressDialog.setMessage(getString(R.string.fazendo_login));

        firebaseAuth           = FirebaseAuth.getInstance();
        firebaseDatabase       = FirebaseDatabase.getInstance();

        loginOnCompleteListener = new LoginOnCompleteListener(this, progressDialog);
        authStateListener       = new LoginAuthStateListener(this);

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
                    Toast.makeText(RedesSociaisActivity.this, "Erro ao conectar com Google API Client", Toast.LENGTH_SHORT).show();
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
    protected void onDestroy() {
        super.onDestroy();
        firebaseAuth.removeAuthStateListener(authStateListener);
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
                Toast.makeText(RedesSociaisActivity.this, "Erro ao logar com sua conta do Google. Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == FacebookSdk.getCallbackRequestCodeOffset())
        {
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    // DIALOG CRIAR CONTA ----------------------------------------
    public void dialogCriarConta(View v)
    {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(RedesSociaisActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_criar_conta, null);

        final EditText dialog_username         = mView.findViewById(R.id.dialog_username);
        final EditText dialog_email            = mView.findViewById(R.id.dialog_email);
        final EditText dialog_password         = mView.findViewById(R.id.dialog_password);
        final EditText dialog_password_confirm = mView.findViewById(R.id.dialog_password_confirm);

        mBuilder.setView(mView);

        mBuilder.setPositiveButton(R.string.create_account, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final String username = dialog_username.getText().toString();
                final String email    = dialog_email.getText().toString();
                String senha    = dialog_password.getText().toString();
                String senhaC   = dialog_password_confirm.getText().toString();

                progressDialog.show();

                if(validarCamposCriarConta(username, email, senha, senhaC))
                {
                    firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(RedesSociaisActivity.this, R.string.success_create_login, Toast.LENGTH_SHORT).show();

                                final FirebaseUser user = firebaseAuth.getCurrentUser();
                                final UserProfileChangeRequest atualizarNome = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();

                                user.updateProfile(atualizarNome).addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        Usuario usuario = new Usuario(user.getUid(), username, email);
                                        usuario.cadastrar();
                                    }
                                });
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Log.e(TAG, task.getException().toString());
                                Toast.makeText(RedesSociaisActivity.this, "Aconteceu um erro", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                    Toast.makeText(RedesSociaisActivity.this, R.string.error_create_login, Toast.LENGTH_SHORT).show();
            }
        });

        mBuilder.setNegativeButton(R.string.cancel, null);

        AlertDialog dialog = mBuilder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.gray_font));

    }

    // TODO: Validação
    private boolean validarCamposCriarConta(String username, String email, String senha, String senhaC)
    {
        /*if (username == null || email == null || senha == null || senhaC == null)
            return false;

        if (username.isEmpty() || email.isEmpty() || senha.isEmpty() || senhaC.isEmpty())
            return false;

        if (!senha.equals(senhaC))
            return false;*/

        return true;
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, loginOnCompleteListener);
    }

    private void firebaseAuthWithFacebook(AccessToken token)
    {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, loginOnCompleteListener);
    }

    public void entrarGoogle(View v)
    {
        progressDialog.show();
        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(googleSignInIntent, RC_GOOGLE_SIGN_IN);
    }

    public void entrarFacebook(View v)
    {
        progressDialog.show();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }

    public void entrarEmail(View v) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
