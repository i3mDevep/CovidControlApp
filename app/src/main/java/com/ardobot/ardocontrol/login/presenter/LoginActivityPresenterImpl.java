package com.ardobot.ardocontrol.login.presenter;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.ardobot.ardocontrol.ArdoApplication;
import com.ardobot.ardocontrol.login.interactors.LoginActivityInteractors;
import com.ardobot.ardocontrol.login.interactors.LoginActivityInteractorsImpl;
import com.ardobot.ardocontrol.login.view.LoginActivityView;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivityPresenterImpl implements LoginActivityPresenter {

    private LoginActivityView loginActivityView;
    private LoginActivityInteractors loginActivityInteractors;

    public LoginActivityPresenterImpl(LoginActivityView loginActivityView) {
        this.loginActivityView = loginActivityView;
        loginActivityInteractors = new LoginActivityInteractorsImpl(this);
    }

    @Override
    public void signIn(String email, String password, Activity activity, FirebaseAuth firebaseAuth) {
        loginActivityView.disableInputs();
        loginActivityView.showProgressBar(true);
        loginActivityInteractors.signIn(email, password, activity, firebaseAuth);
    }

    @Override
    public void LoginSuccess(String msg, String idCompany, String idSubCompany, String name, String email) {
        loginActivityView.enableInputs();
        loginActivityView.goMenu(msg, idCompany, idSubCompany, name, email);
        loginActivityView.showProgressBar(false);
    }

    @Override
    public void LoginError(String err) {
        loginActivityView.enableInputs();
        loginActivityView.capturedErrorLogin(err);
        loginActivityView.showProgressBar(false);
    }

    @Override
    public void initAuthListener(FirebaseAuth firebaseAuth) {
        loginActivityView.showProgressBar(true);
        loginActivityInteractors.initAuthListener(firebaseAuth);
    }

    @Override
    public void removeAuthListener(FirebaseAuth firebaseAuth) {
        loginActivityView.showProgressBar(false);
        loginActivityInteractors.removeAuthListener(firebaseAuth);
    }

}
