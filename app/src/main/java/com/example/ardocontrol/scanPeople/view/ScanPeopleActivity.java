package com.example.ardocontrol.scanPeople.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.zxing.integration.android.IntentIntegrator;


public class ScanPeopleActivity extends AppCompatActivity implements ScanPeopleActivityView {

    private EditText address, name, identification, gender, cellphone, temperature;
    private Button btnsend;
    private SwitchMaterial selectAction;
    private LoadingScan loadingScan;
    private ArdoApplication ardoApplication;

    private ScanActivityPresentor presentor;

    private AutoCompleteTextView completeTextView;

    LocationManager locationManager;
    Context mContext;


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

        mContext = this;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                10, locationListenerGPS);
        isLocationEnabled();

    }
    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            String msg="New Latitude: "+latitude + "New Longitude: "+longitude;
            Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private void isLocationEnabled() {

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
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
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(mContext);
            alertDialog.setTitle("Confirm Location");
            alertDialog.setMessage("Your Location is enabled, please enjoy");
            alertDialog.setNegativeButton("Back to interface",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLocationEnabled();
    }

    public void readDoc(View view){
        new IntentIntegrator(this)
                .setOrientationLocked(false)
                .setCaptureActivity(ScannerActivity.class)
                .initiateScan();
    }
    public void sendInfo(View view){
       // GeoPoint geo = new GeoPoint( loc.getLatitude(), loc.getLongitude());

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