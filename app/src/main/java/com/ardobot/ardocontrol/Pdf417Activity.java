package com.ardobot.ardocontrol;
//Pdf417Activity

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.microblink.MicroblinkSDK;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkbarcode.barcode.BarcodeRecognizer;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.BarcodeUISettings;

public class Pdf417Activity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1337;

    private BarcodeRecognizer mBarcodeRecognizer;

    private RecognizerBundle mRecognizerBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf417);
        setupVersionTextView();

        mBarcodeRecognizer = new BarcodeRecognizer();
        mBarcodeRecognizer.setScanPdf417(true);
        mBarcodeRecognizer.setScanQrCode(true);

        mRecognizerBundle = new RecognizerBundle(mBarcodeRecognizer);
    }

    public void onScanButtonClick(View v) {
        BarcodeUISettings uiSettings = new BarcodeUISettings(mRecognizerBundle);
        uiSettings.setBeepSoundResourceID(R.raw.beep);
        ActivityRunner.startActivityForResult(this, MY_REQUEST_CODE, uiSettings);
    }

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
        Toast.makeText(getApplicationContext(), result.getStringData(),Toast.LENGTH_SHORT).show();
    }

    private void setupVersionTextView() {
        String versionString;

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String appVersion = packageInfo.versionName;
            int appVersionCode = packageInfo.versionCode;

            versionString = "Application version: " +
                    appVersion +
                    ", build " +
                    appVersionCode +
                    "\nLibrary version: " +
                    MicroblinkSDK.getNativeLibraryVersionString();
        } catch (NameNotFoundException e) {
            versionString = "";
        }

        TextView tvVersion = findViewById(R.id.tvVersion);
        tvVersion.setText(versionString);
    }

}
