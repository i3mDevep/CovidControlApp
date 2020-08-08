package com.ardobot.ardocontrol.login.view;

import android.view.View;

public interface LoginActivityView {
    void enableInputs();
    void disableInputs();
    void showProgressBar(boolean status);
    void capturedErrorLogin(String err);
    void goMenu(String msg, String idCompany, String idSubCompany, String name, String email);
}
