package com.example.ardocontrol.scanPeople.interactors;

public interface ScanActivityInteractors {
    void processData(String data);
    void sendDataFirebase(String cc, boolean action, String idCompany, String idSubCompany, String temperature);
    void getDataFirebase(String idCompany, String idSubCompany, String cc);
}
