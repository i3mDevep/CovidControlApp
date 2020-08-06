package com.ardobot.ardocontrol.dispatch.presenter;

import android.app.Activity;

import com.ardobot.ardocontrol.ArdoApplication;
import com.ardobot.ardocontrol.dispatch.interactors.DispatchActivityInteractors;
import com.ardobot.ardocontrol.dispatch.interactors.DispatchActivityInteractorsImpl;
import com.ardobot.ardocontrol.dispatch.view.DispatchActivityView;
import com.google.firebase.auth.FirebaseAuth;

public class DispatchActivityPresentorImpl implements DispatchActivityPresentor  {
    private DispatchActivityView dispatchActivityView;
    private DispatchActivityInteractors dispatchActivityInteractors;

    public DispatchActivityPresentorImpl(DispatchActivityView dispatchActivityView) {
        this.dispatchActivityView = dispatchActivityView;
        dispatchActivityInteractors = new DispatchActivityInteractorsImpl(this);
    }

    @Override
    public void goLogin() {
        dispatchActivityView.goLogin();
    }

    @Override
    public void goMenu(String idCompany, String idSubCompany, String name, String email) {
        dispatchActivityView.goMenu(idCompany, idSubCompany, name, email);
    }

    @Override
    public void initAuthListener(FirebaseAuth firebaseAuth) {
        dispatchActivityInteractors.initAuthListener(firebaseAuth);
    }

    @Override
    public void removeAuthListener(FirebaseAuth firebaseAuth) {
        dispatchActivityInteractors.removeAuthListener(firebaseAuth);
    }
}
