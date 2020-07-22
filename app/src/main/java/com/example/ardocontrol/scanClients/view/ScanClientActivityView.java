package com.example.ardocontrol.scanClients.view;

public interface ScanClientActivityView {
    String[] getIds();
    void successReadDoc(String [] data);
    void clearTexts();
    void errorReadDoc(String err);
    void enableDialog();
    void disableDialog();
    void enableBtnSend();
    void disableBtnSend();
    void errorSendData(String err);
    void successSendData();
}
