package com.example.ardocontrol.scanClients.presenter;

public interface ScanClientActivityPresenter {
    void processDataRead(String content);
    void successReadDoc(String [] info);
    void errorReadDoc(String err);
    void errorSendData(String err);
    void successSendData();
    void sendDataFirebase(String name, String identification, String temperature, String age, String address, String gender, String readGps, String cellphone);
}
