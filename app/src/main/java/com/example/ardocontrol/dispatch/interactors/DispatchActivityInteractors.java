package com.example.ardocontrol.dispatch.interactors;

import com.google.firebase.auth.FirebaseAuth;

public interface DispatchActivityInteractors {
    void initAuthListener(FirebaseAuth firebaseAuth);
    void removeAuthListener(FirebaseAuth firebaseAuth);
}
