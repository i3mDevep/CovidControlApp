package com.example.ardocontrol;

import android.app.Application;

public class ArdoApplication extends Application {
    private String idCompany;
    private String idSubCompany;

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
