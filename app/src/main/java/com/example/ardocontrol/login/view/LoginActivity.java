package com.example.ardocontrol.login.view;

import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ardocontrol.ArdoApplication;
import com.example.ardocontrol.R;
import com.example.ardocontrol.login.presenter.LoginActivityPresenter;
import com.example.ardocontrol.login.presenter.LoginActivityPresenterImpl;
import com.example.ardocontrol.menu.view.MenuActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity  implements LoginActivityView {

    private LoginActivityPresenter loginPresenter;
    private EditText edtEmail;
    private  EditText edtPassword;
    private Button btnLogin;
    private LoginDialog loginDialog;
    private FirebaseAuth firebaseAuth;
    private ArdoApplication ardoApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        edtEmail = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.submitlogin);

        loginPresenter = new LoginActivityPresenterImpl(this);

        loginDialog = new LoginDialog(LoginActivity.this);

        ardoApplication = (ArdoApplication) getApplicationContext();

        firebaseAuth = firebaseAuth.getInstance();

    }
    public void requestLogin(View view){
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        if(!email.trim().equals("") && !password.trim().equals("")) {
            loginPresenter.signIn(email, password, this, firebaseAuth);
        } else {
            SweetAlertDialog sweetdialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
            sweetdialog.setTitleText("Oops...");
            sweetdialog.setContentText("Complete los campos !");
            sweetdialog.show();
            sweetdialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
    }

    @Override
    public void enableInputs() {
        edtEmail.setEnabled(true);
        edtPassword.setEnabled(true);
        btnLogin.setEnabled(true);
    }

    @Override
    public void disableInputs() {
        edtEmail.setEnabled(false);
        edtPassword.setEnabled(false);
        btnLogin.setEnabled(false);
    }

    @Override
    public void showProgressBar(boolean status) {
        if(status){
            loginDialog.startLoadingDialog();
        }else {
            loginDialog.dismissDialog();
        }
    }

    @Override
    public void capturedErrorLogin(String err) {
        if(!err.equals("Sign in")){
            Toast.makeText(getApplicationContext(),err, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void goMenu(String msg, String idCompany, String idSubCompany, String name, String email) {
        ardoApplication.setIdCompany(idCompany);
        ardoApplication.setIdSubCompany(idSubCompany);
        ardoApplication.setDisplayName(name);
        ardoApplication.setEmail(email);
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginPresenter.initAuthListener(firebaseAuth);
    }

    @Override
    protected void onStop() {
        super.onStop();
        loginPresenter.removeAuthListener(firebaseAuth);
    }

    @Override
    public void onBackPressed() {

    }
}