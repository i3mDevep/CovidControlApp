package com.ardobot.ardocontrol.scanClients.presenter;

import com.ardobot.ardocontrol.scanClients.interactors.ScanClientActivityInteractor;
import com.ardobot.ardocontrol.scanClients.interactors.ScanClientActivityInteractorImpl;
import com.ardobot.ardocontrol.scanClients.view.ScanClientActivityView;

public class ScanClientActivityPresenterImpl implements ScanClientActivityPresenter {

    private ScanClientActivityView clientActivityView;
    private ScanClientActivityInteractor clientActivityInteractor;

    public ScanClientActivityPresenterImpl(ScanClientActivityView clientActivityView) {
        this.clientActivityView = clientActivityView;
        clientActivityInteractor = new ScanClientActivityInteractorImpl(this);
    }

    @Override
    public void processDataRead(String content) {
        clientActivityView.enableDialog();
        String[] ids = clientActivityView.getIds();
        clientActivityInteractor.proccessReadDoc(content, ids);
    }

    @Override
    public void successReadDoc(String[] info) {
        clientActivityView.disableDialog();
        clientActivityView.successReadDoc(info);
    }

    @Override
    public void errorReadDoc(String err) {
        clientActivityView.disableDialog();
        clientActivityView.errorReadDoc(err);
    }

    @Override
    public void errorSendData(String err) {
        clientActivityView.disableDialog();
        clientActivityView.enableBtnSend();
        clientActivityView.errorSendData(err);
    }

    @Override
    public void successSendData() {
        clientActivityView.disableDialog();
        clientActivityView.enableBtnSend();
        clientActivityView.clearTexts();
        clientActivityView.successSendData();
    }

    @Override
    public void searhClient(String identification) {
        String idCompany = clientActivityView.getIds()[0];
        clientActivityView.enableDialog();
        clientActivityInteractor.searchClient(idCompany, identification);
    }

    @Override
    public void sendDataFirebase(String name, String identification, String temperature, String birth, String address, String gender, String readGps,  String cellphone,  String cause) {
        clientActivityView.enableDialog();
        clientActivityView.disableBtnSend();
        String[] ids = clientActivityView.getIds();
        clientActivityInteractor.sendDataFirebase(ids[0], ids[1], name, identification, temperature, birth, address, gender, readGps, cellphone, cause);
    }
}
