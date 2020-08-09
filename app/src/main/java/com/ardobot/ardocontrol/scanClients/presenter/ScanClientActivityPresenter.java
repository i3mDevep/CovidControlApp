package com.ardobot.ardocontrol.scanClients.presenter;

public interface ScanClientActivityPresenter {
    void processDataRead(String content);
    void successReadDoc(String [] info);
    void errorReadDoc(String err);
    void errorSendData(String err);
    void successSendData();
    void searhClient(String identification);
    void sendDataFirebase(String name, String identification, String temperature, String birth, String address, String gender, String readGps, String cellphone, String cause);
}
