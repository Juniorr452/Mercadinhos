package com.mobile.pid.pid.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by junio on 27/02/2018.
 */

public class LoginAuthStateListener implements FirebaseAuth.AuthStateListener
{
    private static final String TAG = "LoginAuthStateListener";

    private Context c;

    public LoginAuthStateListener(Context c) {
        this.c = c;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
    {
        //  TODO: Mandar pra activity principal
        if (firebaseAuth.getCurrentUser() != null)
        {
            Toast.makeText(c,
                    "Logado como " + firebaseAuth.getCurrentUser().getEmail(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
