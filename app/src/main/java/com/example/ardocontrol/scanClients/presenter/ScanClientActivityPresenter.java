package com.example.ardocontrol.scanClients.presenter;

public interface ScanClientActivityPresenter {
    void processDataRead(String content);
    void successReadDoc(String [] info);
    void errorReadDoc(String err);
}
