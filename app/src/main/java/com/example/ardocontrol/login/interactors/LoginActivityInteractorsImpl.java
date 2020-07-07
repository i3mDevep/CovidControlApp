package com.example.ardocontrol.login.interactors;

import android.app.Activity;
import android.util.Log;

import com.example.ardocontrol.login.presenter.LoginActivityPresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;

public class LoginActivityInteractorsImpl implements LoginActivityInteractors {

    private LoginActivityPresenter loginActivityPresenter;
    private FirebaseAuth.AuthStateListener authStateListener;

    public LoginActivityInteractorsImpl(LoginActivityPresenter loginActivityPresenter) {
        this.loginActivityPresenter = loginActivityPresenter;
        AuthStateListenerOnChanged();
    }
    private  void AuthStateListenerOnChanged(){
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    loginActivityPresenter.LoginSuccess("Bienvenido!!!");
                }else {
                    loginActivityPresenter.LoginError("Debes iniciar sesion!!!");
                }
            }
        };
    }
    @Override
    public void signIn(String email, String password, Activity activity, final FirebaseAuth firebaseAuth) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    loginActivityPresenter.LoginError("No fue posible iniciar sesion");
                }
            }
        });
    }

    @Override
    public void initAuthListener(FirebaseAuth firebaseAuth) {
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void removeAuthListener(FirebaseAuth firebaseAuth) {
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
