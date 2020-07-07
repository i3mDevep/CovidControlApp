package com.example.ardocontrol.login.view;

import android.view.View;

public interface LoginActivityView {
    void enableInputs();
    void disableInputs();
    void showProgressBar(boolean status);
    void capturedErrorLogin(String err);
    void GoAccessPersmission(String msg);
}
