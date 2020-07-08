package com.example.ardocontrol.dispatch.interactors;

import android.util.Log;

import com.example.ardocontrol.ArdoApplication;
import com.example.ardocontrol.dispatch.presenter.DispatchActivityPresentor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

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
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                   firebaseUser.getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                       @Override
                       public void onSuccess(GetTokenResult getTokenResult) {
                           try {
                               boolean isBusines = (boolean) getTokenResult.getClaims().get("business");
                               firebaseAuth.signOut();
                               dispatchActivityPresentor.goLogin();
                           } catch (Exception e){
                               String companyID = (String) getTokenResult.getClaims().get("companyId");
                               String subCompanyId = (String) getTokenResult.getClaims().get("user_id");
                               dispatchActivityPresentor.goMenu(companyID, subCompanyId);
                           }
                       }
                   });
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
