package com.example.ardocontrol.scanPeople.presenter;

import com.example.ardocontrol.scanPeople.interactors.ScanActivityInteractors;
import com.example.ardocontrol.scanPeople.interactors.ScanActivityInteractorsImple;
import com.example.ardocontrol.scanPeople.view.ScanPeopleActivityView;
import com.google.firebase.firestore.GeoPoint;

public class ScanActivityPresentorImpl implements ScanActivityPresentor {

    private ScanPeopleActivityView scanPeopleActivityView;
    private ScanActivityInteractors scanActivityInteractors;

    public ScanActivityPresentorImpl(ScanPeopleActivityView scanPeopleActivityView) {
        this.scanPeopleActivityView = scanPeopleActivityView;
        scanActivityInteractors = new ScanActivityInteractorsImple(this);
    }

    @Override
    public void processDataRead(String data) {
        scanActivityInteractors.processData(data);
    }

    @Override
    public void processSuccess(String id) {
        scanPeopleActivityView.successRead(id);
        scanPeopleActivityView.showProgressBar(true);
        String[] ids = scanPeopleActivityView.getIds();
        scanActivityInteractors.getDataFirebase(ids[0],ids[1],ids[2]);
    }

    @Override
    public void processError(String err) {
        scanPeopleActivityView.disableButtonSend();
        scanPeopleActivityView.errorRead(err);
    }

    @Override
    public void successGetDataFirebase(String[] data) {
        scanPeopleActivityView.setDataFirebase(data);
        scanPeopleActivityView.enableButtonSend();
        scanPeopleActivityView.showProgressBar(false);
    }

    @Override
    public void successSetDataFirestore() {
        scanPeopleActivityView.showProgressBar(false);
        scanPeopleActivityView.successSendDataFirestore("Tus datos han sido correctamente almacenados ;) ");
        scanPeopleActivityView.clearEditText();
    }

    @Override
    public void errorSetDataFirestore(String err) {
        scanPeopleActivityView.showProgressBar(false);
        scanPeopleActivityView.errorRead(err);
    }

    @Override
    public void errorGetDataFirebase(String err) {
        scanPeopleActivityView.clearEditText();
        scanPeopleActivityView.showProgressBar(false);
        scanPeopleActivityView.errorRead(err);
    }

    @Override
    public void sendTrackingWorker(String cc, boolean action, String temperature, GeoPoint loc) {
        String[] ids = scanPeopleActivityView.getIds();
        scanActivityInteractors.sendDataFirebase(cc, action, ids[0], ids[1], temperature, loc);
        scanPeopleActivityView.showProgressBar(true);
    }
}
