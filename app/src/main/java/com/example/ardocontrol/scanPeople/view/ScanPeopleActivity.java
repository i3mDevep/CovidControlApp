package com.example.ardocontrol.scanPeople.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ardocontrol.ArdoApplication;
import com.example.ardocontrol.R;
import com.example.ardocontrol.menu.view.MenuActivity;
import com.example.ardocontrol.scanPeople.presenter.ScanActivityPresentor;
import com.example.ardocontrol.scanPeople.presenter.ScanActivityPresentorImpl;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.zxing.integration.android.IntentIntegrator;


public class ScanPeopleActivity extends AppCompatActivity implements ScanPeopleActivityView {

    private static final int PERMISSION_FINE_LOCATION = 99;
    private EditText address, name, identification, gender, cellphone, temperature;
    private Button btnsend;
    private SwitchMaterial selectAction;
    private LoadingScan loadingScan;
    private ArdoApplication ardoApplication;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(getApplicationContext(), "Esta app requiere permisos  de localizacion para continuar!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private ScanActivityPresentor presentor;

    private AutoCompleteTextView completeTextView;

    LocationRequest locationRequest;
    LocationCallback locationCallback;
    LocationManager locationManager;


    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
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

        completeTextView = (AutoCompleteTextView) findViewById(R.id.dropTemperature);

        selectAction = (SwitchMaterial) findViewById(R.id.type_action_switch);

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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ScanPeopleActivity.this);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(30000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if(location!= null){
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Toast.makeText(getApplicationContext(),String.valueOf(latitude) + "  real " + String.valueOf(longitude),Toast.LENGTH_SHORT).show();
                }
            }
        };
        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);


    }

    @Override
    protected void onResume() {
        super.onResume();
        isLocationEnabled();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null
                );
    }
    private void isLocationEnabled() {

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Tu localizacion no esta habilitada, por favor habilitala en configuracion");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
        else{
            updateGps();
        }
    }
    private void updateGps(){
        if(ActivityCompat.checkSelfPermission(ScanPeopleActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    startLocationUpdates();
                    if(location!= null){
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Toast.makeText(getApplicationContext(),String.valueOf(latitude) + " update " + String.valueOf(longitude),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
            }
        }
    }

    public void readDoc(View view){
        new IntentIntegrator(this)
                .setOrientationLocked(false)
                .setCaptureActivity(ScannerActivity.class)
                .initiateScan();
    }
    public void sendInfo(View view){

        presentor.sendTrackingWorker(identification.getText().toString(), selectAction.isChecked(), temperature.getText().toString(), null);
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
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
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