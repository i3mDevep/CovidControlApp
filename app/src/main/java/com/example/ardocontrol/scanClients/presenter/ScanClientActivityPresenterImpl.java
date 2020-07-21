package com.example.ardocontrol.scanClients.presenter;

import com.example.ardocontrol.scanClients.interactors.ScanClientActivityInteractor;
import com.example.ardocontrol.scanClients.interactors.ScanClientActivityInteractorImpl;
import com.example.ardocontrol.scanClients.view.ScanClientActivityView;

public class ScanClientActivityPresenterImpl implements ScanClientActivityPresenter {

    private ScanClientActivityView clientActivityView;
    private ScanClientActivityInteractor clientActivityInteractor;

    public ScanClientActivityPresenterImpl(ScanClientActivityView clientActivityView) {
        this.clientActivityView = clientActivityView;
        clientActivityInteractor = new ScanClientActivityInteractorImpl(this);
    }

    @Override
    public void processDataRead(String content) {
        clientActivityInteractor.proccessReadDoc(content);
    }

    @Override
    public void successReadDoc(String[] info) {
        clientActivityView.successReadDoc(info);
    }

    @Override
    public void errorReadDoc(String err) {

    }
}
