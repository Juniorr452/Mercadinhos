package com.mobile.pid.pid.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

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
            Log.d(TAG, "signInWithCredential:success");
        else
        {
            // TODO: Tratar exceptions
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithCredential:failure", task.getException());
            Toast.makeText(c, "Falha na autenticação. Tente logar com outro serviço.", Toast.LENGTH_SHORT).show();
        }

    }
}
