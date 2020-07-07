package com.example.ardocontrol.login.interactors;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;

public interface LoginActivityInteractors {
    void signIn(String email, String password, Activity activity, FirebaseAuth firebaseAuth);
    void initAuthListener(FirebaseAuth firebaseAuth);
    void removeAuthListener(FirebaseAuth firebaseAuth);
}
