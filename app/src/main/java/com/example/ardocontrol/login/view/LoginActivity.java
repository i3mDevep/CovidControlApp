package com.example.ardocontrol.login.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        edtEmail = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.submitlogin);

        loginPresenter = new LoginActivityPresenterImpl(this);

        loginDialog = new LoginDialog(LoginActivity.this);

        firebaseAuth = firebaseAuth.getInstance();

    }
    public void requestLogin(View view){
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        loginPresenter.signIn(email, password, this, firebaseAuth);
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
        Toast.makeText(getApplicationContext(),err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void GoAccessPersmission(String msg) {
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