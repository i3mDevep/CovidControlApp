package com.ardobot.ardocontrol.dispatch.interactors;

import android.util.Log;

import com.ardobot.ardocontrol.dispatch.presenter.DispatchActivityPresentor;
import com.google.android.gms.tasks.OnSuccessListener;
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
                               Log.d("CLAIMS", String.valueOf(getTokenResult.getClaims()));
                               String companyID = (String) getTokenResult.getClaims().get("companyId");
                               String name = (String) getTokenResult.getClaims().get("name");
                               String email = (String) getTokenResult.getClaims().get("email");
                               String subCompanyId = (String) getTokenResult.getClaims().get("user_id");
                               dispatchActivityPresentor.goMenu(companyID, subCompanyId, name, email);
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
