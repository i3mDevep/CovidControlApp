package com.example.ardocontrol;

import android.app.Application;

public class ArdoApplication extends Application {
    private String idCompany;
    private String idSubCompany;
    private String displayName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

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
