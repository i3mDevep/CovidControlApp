package com.example.ardocontrol.scanPeople.view;

public interface ScanPeopleActivityView {
    void enableButtonSend();
    void disableButtonSend();
    void errorRead(String err);
    void successRead(String info[]);
    void clearEditText();
    void showProgressBar(boolean status);
}
