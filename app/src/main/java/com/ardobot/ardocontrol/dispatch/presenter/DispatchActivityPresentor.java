package com.ardobot.ardocontrol.dispatch.presenter;

import com.ardobot.ardocontrol.ArdoApplication;
import com.google.firebase.auth.FirebaseAuth;

public interface DispatchActivityPresentor {
    void goLogin();
    void goMenu(String idCompany, String idSubCompany, String name, String email);
    void initAuthListener(FirebaseAuth firebaseAuth);
    void removeAuthListener(FirebaseAuth firebaseAuth);
}
