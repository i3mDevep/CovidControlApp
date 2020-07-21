package com.example.ardocontrol.scanPeople.view;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ardocontrol.ArdoApplication;
import com.example.ardocontrol.R;
import com.example.ardocontrol.ScannerActivity;
import com.example.ardocontrol.menu.view.MenuActivity;
import com.example.ardocontrol.scanPeople.presenter.ScanActivityPresentor;
import com.example.ardocontrol.scanPeople.presenter.ScanActivityPresentorImpl;
import com.example.ardocontrol.scanPeople.utils.CustomeGps;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.GeoPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.List;


public class ScanPeopleActivity extends AppCompatActivity implements ScanPeopleActivityView {

    private static final int PERMISSION_FINE_LOCATION = 99;

    private TextView textType;
    private EditText address, name, identification, gender, cellphone, temperature;
    private Button btnsend;
    private SwitchMaterial selectAction;
    private LoadingScan loadingScan;
    private ArdoApplication ardoApplication;

    private CustomeGps customeGps;

    private ScanActivityPresentor presentor;

    private AutoCompleteTextView completeTextView;

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
        textType = (TextView) findViewById(R.id.textViewTypeAction);

        btnsend = (Button) findViewById(R.id.btn_send_doc);

        completeTextView = (AutoCompleteTextView) findViewById(R.id.dropTemperature);

        selectAction = (SwitchMaterial) findViewById(R.id.type_action_switch);
        selectAction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    textType.setText("Entrada");
                }else{
                    textType.setText("Salida");
                }
            }
        });

        loadingScan = new LoadingScan(this);

        ardoApplication = (ArdoApplication) getApplicationContext();

        double[] items = new double[70];
        String[] cast_items = new String[69];
        items[0] = 28.0;
        for (int i = 1; i < 70; i++) {
            items[i] = items[i - 1] + 0.2;
            cast_items[i - 1] = String.format("%.1f", items[i - 1]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, cast_items);
        completeTextView.setAdapter(adapter);

        presentor = new ScanActivityPresentorImpl(this);

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
                presentor.processDataRead(result.getContents().toString());
            }else{
                Toast.makeText(getApplicationContext(), "Scan canceled!", Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(getApplicationContext(), "Error this message is null", Toast.LENGTH_LONG).show();
        }
    }
    public void readDoc(View view){
        new IntentIntegrator(this)
                .setOrientationLocked(false)
                .setCaptureActivity(ScannerActivity.class)
                .initiateScan();
    }
    public void sendInfo(View view){
        Location location = customeGps.getLocation();
        if(location != null){
            GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
            Geocoder geocoder = new Geocoder(this);
            try {
                List<Address>  addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                presentor.sendTrackingWorker(identification.getText().toString(), selectAction.isChecked(), temperature.getText().toString(), geoPoint, addresses.get(0).getAddressLine(0));

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "No fue posible extraer una direccion para esas coordenadas", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(), "No tenemos permisos para acceder a la posicion actual del gps", Toast.LENGTH_SHORT).show();
        }
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
        new SweetAlertDialog(ScanPeopleActivity.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(err)
                .show();
    }

    @Override
    public void successRead(String data) {
        identification.setText(data);
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
    public void setDataFirebase(String[] data) {
        name.setText(data[0]);
        cellphone.setText(data[1]);
        gender.setText(data[2]);
        address.setText(data[3]);
        temperature.setText("");
    }

    @Override
    public void showProgressBar(boolean status) {
        if(status){
            loadingScan.startLoadingDialog();
        }else {
            loadingScan.dismissDialog();
        }
    }

    @Override
    public void successSendDataFirestore(String msg) {
        new SweetAlertDialog(ScanPeopleActivity.this)
                .setTitleText(msg)
                .show();
    }

    @Override
    public String[] getIds() {
        String idCompany = ardoApplication.getIdCompany();
        String idSubCompany = ardoApplication.getIdSubCompany();
        String cc = identification.getText().toString();
        String[] ids = new String [] {idCompany, idSubCompany, cc};
        return ids;
    }
}