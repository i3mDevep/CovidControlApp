package com.example.ardocontrol.menu.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ardocontrol.ArdoApplication;
import com.example.ardocontrol.R;
import com.example.ardocontrol.login.view.LoginActivity;
import com.example.ardocontrol.menu.presenter.MenuActivityPresenter;
import com.example.ardocontrol.menu.presenter.MenuActivityPresenterImpl;
import com.example.ardocontrol.scanClients.view.ScanClientMainActivity;
import com.example.ardocontrol.scanPeople.view.ScanPeopleActivity;

public class MenuActivity extends AppCompatActivity implements MenuActivityView{

    private MenuDialogConfirmExit confirmExit;
    private MenuActivityPresenter presenter;

    private ArdoApplication ardoApplication;

    private TextView textViewDisplayName, textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        textViewDisplayName = (TextView) findViewById(R.id.displayName);
        textViewEmail = (TextView) findViewById(R.id.email);

        ardoApplication = (ArdoApplication) getApplicationContext();

        textViewDisplayName.setText(ardoApplication.getDisplayName());
        textViewEmail.setText(ardoApplication.getEmail());

        presenter = new MenuActivityPresenterImpl(this);
        confirmExit = new MenuDialogConfirmExit(this, presenter);
    }

    @Override
    public void ScanViewId(boolean typeScan) {
    }

    @Override
    public void GoActivityLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void Logout(View view){
        confirmExit.startLoadingDialog();
    }
   public void scanWorker(View view){
       Intent intent = new Intent(this, ScanPeopleActivity.class);
       startActivity(intent);
   }
   public void scanUser(View view){
       Intent intent = new Intent(this, ScanClientMainActivity.class);
       startActivity(intent);
   }
    @Override
    public void onBackPressed() {
        confirmExit.startLoadingDialog();
    }
}