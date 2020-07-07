package com.example.ardocontrol.dispatch.presenter;

import com.google.firebase.auth.FirebaseAuth;

public interface DispatchActivityPresentor {
    void goLogin();
    void goMenu();
    void initAuthListener(FirebaseAuth firebaseAuth);
    void removeAuthListener(FirebaseAuth firebaseAuth);
}
