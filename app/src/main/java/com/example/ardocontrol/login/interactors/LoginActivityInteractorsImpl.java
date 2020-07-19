package com.example.ardocontrol.login.interactors;

import android.app.Activity;
import android.util.Log;

import com.example.ardocontrol.ArdoApplication;
import com.example.ardocontrol.login.presenter.LoginActivityPresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

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
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    firebaseUser.getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                        @Override
                        public void onSuccess(GetTokenResult getTokenResult) {
                            try {
                                boolean isBusines = (boolean) getTokenResult.getClaims().get("business");
                                loginActivityPresenter.LoginError("No puedes iniciar sesion con el user de una empresa!");
                                firebaseAuth.signOut();
                            } catch (Exception e){
                                String companyID = (String) getTokenResult.getClaims().get("companyId");
                                String subCompanyId = (String) getTokenResult.getClaims().get("user_id");
                                String name = (String) getTokenResult.getClaims().get("name");
                                String email = (String) getTokenResult.getClaims().get("email");
                                loginActivityPresenter.LoginSuccess("Welcome!!!",companyID, subCompanyId, name, email);
                            }
                        }
                    });
                } else {
                    loginActivityPresenter.LoginError("Sign in");
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
                    loginActivityPresenter.LoginError(task.getException().getMessage());
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
