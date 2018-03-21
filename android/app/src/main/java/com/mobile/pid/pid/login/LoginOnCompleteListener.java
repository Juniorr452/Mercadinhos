package com.mobile.pid.pid.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobile.pid.pid.UsuarioLogado;

/**
 * Created by junio on 27/02/2018.
 */

public class LoginOnCompleteListener implements OnCompleteListener<AuthResult>
{
    private static final String TAG = "LoginOnCompleteListener";

    private Context c;

    public LoginOnCompleteListener(Context c) {
        this.c = c;
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
                String nome    = user.getDisplayName();
                String email   = user.getEmail();
                String fotoUrl = user.getPhotoUrl().toString();

                Usuario usuario = new Usuario(nome, email, fotoUrl);
                usuario.cadastrar();
            }
            else
                Log.d(TAG, "Usuário não é novo");
        }
        else
        {
            // TODO: Tratar exceptions
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithCredential:failure", task.getException());
            Toast.makeText(c, "Falha na autenticação. Tente logar com outro serviço.", Toast.LENGTH_SHORT).show();
        }

    }
}
