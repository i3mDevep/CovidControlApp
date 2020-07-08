package com.example.ardocontrol.scanPeople.presenter;

public interface ScanActivityPresentor {
    void processDataRead(String data);
    void processSuccess();
    void processError(String err);
}
