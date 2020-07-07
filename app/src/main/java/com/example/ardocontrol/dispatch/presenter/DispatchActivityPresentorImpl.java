package com.example.ardocontrol.dispatch.presenter;

import android.app.Activity;

import com.example.ardocontrol.dispatch.interactors.DispatchActivityInteractors;
import com.example.ardocontrol.dispatch.interactors.DispatchActivityInteractorsImpl;
import com.example.ardocontrol.dispatch.view.DispatchActivityView;
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
    public void goMenu() {
        dispatchActivityView.goMenu();
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
