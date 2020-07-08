package com.example.ardocontrol.scanPeople.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ardocontrol.R;
import com.example.ardocontrol.menu.view.MenuActivity;
import com.example.ardocontrol.scanPeople.presenter.ScanActivityPresentor;
import com.example.ardocontrol.scanPeople.presenter.ScanActivityPresentorImpl;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanPeopleActivity extends AppCompatActivity implements ScanPeopleActivityView {

    private EditText address, name, identification, gender, cellphone, temperature;
    private Button btnsend;
    private LoadingScan loadingScan;

    private ScanActivityPresentor presentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_people);

        address = (EditText) findViewById(R.id.text_address);
        name = (EditText) findViewById(R.id.text_name);
        identification = (EditText) findViewById(R.id.text_identification);
        gender = (EditText) findViewById(R.id.text_gender);
        cellphone = (EditText) findViewById(R.id.text_cellphone);
        temperature = (EditText) findViewById(R.id.dropTemperature);

        btnsend = (Button) findViewById(R.id.btn_send_doc);

        loadingScan = new LoadingScan(this);

        presentor = new ScanActivityPresentorImpl(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            presentor.processDataRead(result.getContents().toString());
        }else{
            Toast.makeText(getApplicationContext(), "Error this message is null", Toast.LENGTH_LONG).show();
        }
    }
    public void readDoc(View view){
        IntentIntegrator Intent = new IntentIntegrator(ScanPeopleActivity.this);
        Intent.setPrompt("Active Scan...");
        Intent.setOrientationLocked(false);
        Intent.setBeepEnabled(true);
        Intent.setCameraId(0);
        Intent.setBarcodeImageEnabled(false);
        Intent.initiateScan();
    }
    public void sendInfo(View view){

    }
    public  void cancel(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void enableButtonSend() {
        btnsend.setEnabled(true);
    }

    @Override
    public void disableButtonSend() {
        btnsend.setEnabled(false);
    }

    @Override
    public void errorRead(String err) {
        Toast.makeText(getApplicationContext(),err,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void successRead(String[] info) {

    }

    @Override
    public void clearEditText() {
        address.setText("");
        name.setText("");
        identification.setText("");
        temperature.setText("");
        cellphone.setText("");
        gender.setText("");
    }
    @Override
    public void showProgressBar(boolean status) {
        if(status){
            loadingScan.startLoadingDialog();
        }else {
            loadingScan.dismissDialog();
        }
    }
}