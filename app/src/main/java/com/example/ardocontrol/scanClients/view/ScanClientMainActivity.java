package com.example.ardocontrol.scanClients.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ardocontrol.ArdoApplication;
import com.example.ardocontrol.CustomeGps;
import com.example.ardocontrol.R;
import com.example.ardocontrol.ScannerActivity;
import com.example.ardocontrol.scanClients.presenter.ScanClientActivityPresenter;
import com.example.ardocontrol.scanClients.presenter.ScanClientActivityPresenterImpl;
import com.example.ardocontrol.scanPeople.view.ScanPeopleActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.List;

public class ScanClientMainActivity extends AppCompatActivity implements ScanClientActivityView {

    private static final int PERMISSION_FINE_LOCATION = 99;

    private ScanClientActivityPresenter activityPresentor;

    private EditText name, identification, age, address, celphone, temperature;
    private Spinner gender;
    private LoadingClient loadingClient;
    private Button btn_send;

    private ArdoApplication ardoApplication;
    private CustomeGps customeGps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_client_main);

        name = (EditText) findViewById(R.id.text_name);
        identification = (EditText) findViewById(R.id.document);
        age = (EditText) findViewById(R.id.age);
        address = (EditText) findViewById(R.id.address);
        celphone = (EditText) findViewById(R.id.text_cellphone);
        gender = (Spinner) findViewById(R.id.gender_options);
        temperature = (EditText) findViewById(R.id.dropTemperature);
        btn_send = (Button) findViewById(R.id.btn_send_doc_client);
        btn_send.setEnabled(true);

        loadingClient = new LoadingClient(this);

        activityPresentor = new ScanClientActivityPresenterImpl(this);

        ardoApplication = (ArdoApplication) getApplicationContext();

        customeGps = new CustomeGps(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        customeGps.isLocationEnabled();
    }
    @Override
    protected void onPause() {
        super.onPause();
        customeGps.stopLocationUpdates();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    customeGps.startLocationUpdates();
                } else {
                    Toast.makeText(getApplicationContext(), "Esta app requiere permisos  de localizacion para continuar!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() != null){
                activityPresentor.processDataRead(result.getContents());
            }else{
                Toast.makeText(getApplicationContext(), "Scan canceled!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Error this message is null", Toast.LENGTH_LONG).show();
        }
    }
    public void clickReadDoc(View view){
        new IntentIntegrator(this)
                .setOrientationLocked(false)
                .setCaptureActivity(ScannerActivity.class)
                .initiateScan();
    }
    public  void clickSendInfo(View view){
        Location location = customeGps.getLocation();
        if(location != null){
            Geocoder geocoder = new Geocoder(this);
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                activityPresentor.sendDataFirebase(
                        name.getText().toString(),
                        identification.getText().toString(),
                        temperature.getText().toString(),
                        age.getText().toString(),
                        address.getText().toString(),
                        gender.getSelectedItem().toString(),
                        addresses.get(0).getAddressLine(0),
                        celphone.getText().toString()
                        );
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "No fue posible extraer una direccion para esas coordenadas", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(), "No tenemos permisos para acceder a la posicion actual del gps", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void successReadDoc(String[] data) {
        identification.setText(data[0]);
        name.setText(data[1]);
        gender.setSelection(data[2] == "Hombre"? 1: 0);
        age.setText(data[3]);
        address.setText(data[4]);
        celphone.setText(data[5]);
    }

    @Override
    public void clearTexts() {
        identification.setText("");
        name.setText("");
        age.setText("");
        address.setText("");
        celphone.setText("");
        temperature.setText("");
    }

    @Override
    public void errorReadDoc(String err) {
        Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
    }

    @Override
    public String[] getIds() {
        String idCompany = ardoApplication.getIdCompany();
        String idSubCompany = ardoApplication.getIdSubCompany();
        String cc = identification.getText().toString();
        String[] ids = new String [] {idCompany, idSubCompany, cc};
        return ids;
    }
    @Override
    public void enableDialog() {
        loadingClient.startLoadingDialog();
    }

    @Override
    public void disableDialog() {
        loadingClient.dismissDialog();
    }

    @Override
    public void enableBtnSend() {
        btn_send.setEnabled(true);
    }

    @Override
    public void disableBtnSend() {
        btn_send.setEnabled(false);
    }

    @Override
    public void errorSendData(String err) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(err)
                .show();
    }

    @Override
    public void successSendData() {
        new SweetAlertDialog(this)
                .setTitleText("Registro exitoso!")
                .show();
    }
}
