package com.example.ardocontrol.dispatch.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.ardocontrol.ArdoApplication;
import com.example.ardocontrol.R;
import com.example.ardocontrol.dispatch.presenter.DispatchActivityPresentor;
import com.example.ardocontrol.dispatch.presenter.DispatchActivityPresentorImpl;
import com.example.ardocontrol.login.view.LoginActivity;
import com.example.ardocontrol.menu.view.MenuActivity;
import com.google.firebase.auth.FirebaseAuth;

public class DispatchActivity extends AppCompatActivity implements DispatchActivityView {

    private DispatchActivityPresentor dispatchActivityPresentor;
    private FirebaseAuth firebaseAuth;
    private  ArdoApplication ardoApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);

        firebaseAuth = firebaseAuth.getInstance();

        ardoApplication = (ArdoApplication) getApplicationContext();
        dispatchActivityPresentor = new DispatchActivityPresentorImpl(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dispatchActivityPresentor.initAuthListener(firebaseAuth);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dispatchActivityPresentor.removeAuthListener(firebaseAuth);
    }

    @Override
    public void goLogin() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }, 1500);
    }

    @Override
    public void goMenu(String idCompany, String idSubCompany) {
        ardoApplication.setIdCompany(idCompany);
        ardoApplication.setIdSubCompany(idSubCompany);
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}