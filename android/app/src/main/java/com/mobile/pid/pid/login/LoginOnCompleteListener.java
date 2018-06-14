package com.mobile.pid.pid.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.mobile.pid.pid.classes_e_interfaces.Usuario;

/**
 * Created by junio on 27/02/2018.
 */

public class LoginOnCompleteListener implements OnCompleteListener<AuthResult>
{
    private static final String TAG = "LoginOnCompleteListener";

    private Context c;
    private ProgressDialog progressDialog;

    public LoginOnCompleteListener(Context c, ProgressDialog progressDialog) {
        this.c = c;
        this.progressDialog = progressDialog;
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task)
    {
        if (task.isSuccessful())
        {
            Log.d(TAG, "signInWithCredential:success");

            // Verificar se o usuário é novo e cadastrar os dados no banco.
            if (task.getResult().getAdditionalUserInfo().isNewUser())
            {
                Log.d(TAG, "Novo usuário");

                FirebaseUser user = task.getResult().getUser();
                String uid     = user.getUid();
                String nome    = user.getDisplayName();
                String email   = user.getEmail();
                String fotoUrl = user.getPhotoUrl().toString();

                Usuario usuario = new Usuario(uid, nome, email, fotoUrl);
                usuario.cadastrar();
            }
            else
                Log.d(TAG, "Usuário não é novo");
        }
        else
        {
            Log.w(TAG, "signInWithCredential:failure", task.getException());
            progressDialog.dismiss();
            Toast.makeText(c, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
