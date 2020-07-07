package com.example.ardocontrol.dispatch.interactors;

import com.example.ardocontrol.dispatch.presenter.DispatchActivityPresentor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;

public class DispatchActivityInteractorsImpl implements DispatchActivityInteractors {

    private DispatchActivityPresentor dispatchActivityPresentor;
    private FirebaseAuth.AuthStateListener authStateListener;

    public DispatchActivityInteractorsImpl(DispatchActivityPresentor dispatchActivityPresentor) {
        this.dispatchActivityPresentor = dispatchActivityPresentor;
        AuthStateListenerOnChanged();
    }

    private  void AuthStateListenerOnChanged(){
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    dispatchActivityPresentor.goMenu();
                }else {
                    dispatchActivityPresentor.goLogin();
                }
            }
        };
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
