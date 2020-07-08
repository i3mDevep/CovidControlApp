package com.example.ardocontrol.scanPeople.interactors;

public interface ScanActivityInteractors {
    void processData(String data);
    void sendDataFirebase(String content[]);
    void getDataFirebase(String idCompany, String idSubCompany, String cc);
}
