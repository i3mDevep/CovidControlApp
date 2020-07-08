package com.example.ardocontrol.scanPeople.view;

public interface ScanPeopleActivityView {
    void enableButtonSend();
    void disableButtonSend();
    void errorRead(String err);
    void successRead(String data);
    void clearEditText();
    void setDataFirebase(String[] data);
    void showProgressBar(boolean status);
    void successSendDataFirestore(String msg);
    String[] getIds();
}
