package com.ardobot.ardocontrol;

import android.app.Application;

import com.microblink.MicroblinkSDK;
import com.microblink.intent.IntentDataTransferMode;

public class ArdoApplication extends Application {

    private String idCompany;
    private String idSubCompany;
    private String displayName;
    private String email;

    @Override
    public void onCreate() {
        super.onCreate();
        MicroblinkSDK.setLicenseKey("sRwAAAAXY29tLmFyZG9ib3QuYXJkb2NvbnRyb2zPRUkxXzSgn81amnHWfrJiZqvltR285QRve1JVm0Av46JQjlh0N4tQr98UhDFlnvdpg/ic4RuUZeYoVVeF0tvUJ6QBKZes6Hmf6K6AbVFzwpblzf6pRQ8FA6MuLbqxN/ByUHaa", this);
        //MicroblinkSDK.setLicenseKey("sRwAAAAXY29tLmFyZG9ib3QuYXJkb2NvbnRyb2zPRUkxXzSgn81amnHWnrBiCkAZZmKF+yK4Xn+v40Qd0fBEvkLgwk6iCOXl1+KDFVH6KPZrWo4+0WkEjn+CZV2AI4w7+ntYbjfxrJCCS5nYYJCUqZA9bZ+aiSMXxRrOnuK54Zcy", this);
        MicroblinkSDK.setIntentDataTransferMode(IntentDataTransferMode.PERSISTED_OPTIMISED);

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(String idCompany) {
        this.idCompany = idCompany;
    }

    public String getIdSubCompany() {
        return idSubCompany;
    }

    public void setIdSubCompany(String idSubCompany) {
        this.idSubCompany = idSubCompany;
    }
}
