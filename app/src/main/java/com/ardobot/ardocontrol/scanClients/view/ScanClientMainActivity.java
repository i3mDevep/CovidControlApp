package com.ardobot.ardocontrol.scanClients.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ardobot.ardocontrol.ArdoApplication;
import com.ardobot.ardocontrol.CustomeGps;
import com.ardobot.ardocontrol.R;
import com.ardobot.ardocontrol.ScannerActivity;
import com.ardobot.ardocontrol.menu.view.MenuActivity;
import com.ardobot.ardocontrol.scanClients.presenter.ScanClientActivityPresenter;
import com.ardobot.ardocontrol.scanClients.presenter.ScanClientActivityPresenterImpl;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkbarcode.barcode.BarcodeRecognizer;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.BarcodeUISettings;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ScanClientMainActivity extends AppCompatActivity implements ScanClientActivityView {

    private static final int PERMISSION_FINE_LOCATION = 99;
    private static final int MY_REQUEST_CODE = 1337;

    private BarcodeRecognizer mBarcodeRecognizer;

    private RecognizerBundle mRecognizerBundle;

    private ScanClientActivityPresenter activityPresentor;

    private EditText name, identification, address, celphone, temperature, cause;
    private Spinner gender;
    private LoadingClient loadingClient;
    private Button btn_send, birth;

    private ArdoApplication ardoApplication;
    private CustomeGps customeGps;

    private MaterialDatePicker<Long> picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_client_main);

        name = (EditText) findViewById(R.id.text_name);
        identification = (EditText) findViewById(R.id.document);
        birth = (Button) findViewById(R.id.btn_born);
        address = (EditText) findViewById(R.id.address);
        celphone = (EditText) findViewById(R.id.text_cellphone);
        gender = (Spinner) findViewById(R.id.gender_options);
        temperature = (EditText) findViewById(R.id.dropTemperature);
        cause = (EditText) findViewById(R.id.cause);

        btn_send = (Button) findViewById(R.id.btn_send_doc_client);
        btn_send.setEnabled(true);

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText(R.string.select_date);
        picker = builder.build();

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {

                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;

                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date(selection + offsetFromUTC);
                birth.setText(simpleFormat.format(date));
            }
        });

        loadingClient = new LoadingClient(this);

        activityPresentor = new ScanClientActivityPresenterImpl(this);

        ardoApplication = (ArdoApplication) getApplicationContext();

        customeGps = new CustomeGps(this);

        //pdf417//
        mBarcodeRecognizer = new BarcodeRecognizer();
        mBarcodeRecognizer.setScanPdf417(true);
        mBarcodeRecognizer.setScanQrCode(true);

        mRecognizerBundle = new RecognizerBundle(mBarcodeRecognizer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_back:
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
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
/*    @Override
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
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            handleScanResultIntent(data);
        }
    }
    private void handleScanResultIntent(Intent data) {
        mRecognizerBundle.loadFromIntent(data);
        BarcodeRecognizer.Result result = mBarcodeRecognizer.getResult();
        activityPresentor.processDataRead(result.getStringData());

        //Toast.makeText(getApplicationContext(), result.getStringData(),Toast.LENGTH_SHORT).show();
    }
    public void openDatePicker(View view){
        picker.show(getSupportFragmentManager(), picker.toString());
    }
    public void clickReadDoc(View view){
        BarcodeUISettings uiSettings = new BarcodeUISettings(mRecognizerBundle);
        uiSettings.setBeepSoundResourceID(R.raw.beep);
        ActivityRunner.startActivityForResult(this, MY_REQUEST_CODE, uiSettings);
    }
    public void clickSearch(View view){
        activityPresentor.searhClient(identification.getText().toString());
    }
    public void clickSendInfo(View view){
        Location location = customeGps.getLocation();
        if(location != null){
            Geocoder geocoder = new Geocoder(this);
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                activityPresentor.sendDataFirebase(
                        name.getText().toString(),
                        identification.getText().toString(),
                        temperature.getText().toString(),
                        birth.getText().toString(),
                        address.getText().toString(),
                        gender.getSelectedItem().toString(),
                        addresses.get(0).getAddressLine(0),
                        celphone.getText().toString(),
                        cause.getText().toString()
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
        birth.setText(data[3]);
        address.setText(data[4]);
        celphone.setText(data[5]);
        if(data[2].equals("Hombre")){
            gender.setSelection(1);
        } else {
            gender.setSelection(0);
        }
    }

    @Override
    public void clearTexts() {
        identification.setText("");
        name.setText("");
        birth.setText("");
        address.setText(R.string.default_address);
        celphone.setText("");
        temperature.setText("");
        cause.setText(R.string.default_cause);
    }

    @Override
    public void errorReadDoc(String err) {
        Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
        name.setText("");
        birth.setText("");
        address.setText("");
        celphone.setText("");
        temperature.setText("");
        cause.setText("");
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
        showCustomErrorDialog(err);
    }
    private void showCustomErrorDialog(String err) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog_error, viewGroup, false);
        TextView error = (TextView) dialogView.findViewById(R.id.text_error);
        error.setText(err);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void successSendData() {
        showCustomSuccessDialog("Operacion completada con exito");
    }
    private void showCustomSuccessDialog(String msg) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);
        TextView success = (TextView) dialogView.findViewById(R.id.text_success);
        success.setText(msg);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
