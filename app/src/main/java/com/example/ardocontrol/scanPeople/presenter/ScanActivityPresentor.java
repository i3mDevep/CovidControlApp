package com.example.ardocontrol.scanPeople.presenter;

public interface ScanActivityPresentor {
    void processDataRead(String data);
    void processSuccess(String id);
    void processError(String err);
    void successGetDataFirebase(String[] data);
    void successSetDataFirestore();
    void errorSetDataFirestore(String err);
    void errorGetDataFirebase(String err);
    void sendTrackingWorker(String cc, boolean action, String temperature);
}