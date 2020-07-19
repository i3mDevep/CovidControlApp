package com.example.ardocontrol.login.presenter;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;

public interface LoginActivityPresenter {
    void signIn(String email, String password, Activity activity, FirebaseAuth firebaseAuth);
    void LoginSuccess(String msg, String idCompany, String idSubCompany, String name, String email);
    void LoginError(String err);
    void initAuthListener(FirebaseAuth firebaseAuth);
    void removeAuthListener(FirebaseAuth firebaseAuth);
}
