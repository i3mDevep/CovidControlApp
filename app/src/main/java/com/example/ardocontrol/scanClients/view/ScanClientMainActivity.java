package com.example.ardocontrol.scanClients.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ardocontrol.R;
import com.example.ardocontrol.ScannerActivity;
import com.example.ardocontrol.scanClients.presenter.ScanClientActivityPresenter;
import com.example.ardocontrol.scanClients.presenter.ScanClientActivityPresenterImpl;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Calendar;

public class ScanClientMainActivity extends AppCompatActivity implements ScanClientActivityView{

    private ScanClientActivityPresenter activityPresentor;

    private EditText name, identification, age;
    private Spinner gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_client_main);

        name = (EditText) findViewById(R.id.text_name);
        identification = (EditText) findViewById(R.id.document);
        age = (EditText) findViewById(R.id.age);
        gender = (Spinner) findViewById(R.id.gender_options);

        activityPresentor = new ScanClientActivityPresenterImpl(this);
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

    @Override
    public void successReadDoc(String[] data) {
        identification.setText(data[0]);
        name.setText(data[1]);
        gender.setSelection(data[2] == "Hombre"? 1: 0);

        try {
            int Year = Integer.parseInt(data[3].substring(0, 4));
            int Month = Integer.parseInt(data[3].substring(4, 6));
            int Day = Integer.parseInt(data[3].substring(6, 8));
            String age_ = getAge(Year,Month,Day);
            age.setText(age_);
        }catch (Exception err){
            Toast.makeText(getApplicationContext(), "Ingrese edad manual!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void errorReadDoc(String err) {

    }
    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}
