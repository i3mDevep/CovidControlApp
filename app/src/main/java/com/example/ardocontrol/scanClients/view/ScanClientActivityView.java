package com.example.ardocontrol.scanClients.view;

public interface ScanClientActivityView {
    void successReadDoc(String [] data);
    void errorReadDoc(String err);
}
