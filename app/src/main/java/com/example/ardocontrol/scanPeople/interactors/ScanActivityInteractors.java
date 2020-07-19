package com.example.ardocontrol.scanPeople.interactors;

import com.google.firebase.firestore.GeoPoint;

public interface ScanActivityInteractors {
    void processData(String data);
    void sendDataFirebase(String cc, boolean action, String idCompany, String idSubCompany, String temperature, GeoPoint loc, String address);
    void getDataFirebase(String idCompany, String idSubCompany, String cc);
}
