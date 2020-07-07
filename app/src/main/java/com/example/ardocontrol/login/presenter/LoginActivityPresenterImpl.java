package com.example.ardocontrol.login.presenter;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.example.ardocontrol.login.interactors.LoginActivityInteractors;
import com.example.ardocontrol.login.interactors.LoginActivityInteractorsImpl;
import com.example.ardocontrol.login.view.LoginActivityView;
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
    public void LoginSuccess(String msg) {
        loginActivityView.enableInputs();
        loginActivityView.GoAccessPersmission(msg);
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
