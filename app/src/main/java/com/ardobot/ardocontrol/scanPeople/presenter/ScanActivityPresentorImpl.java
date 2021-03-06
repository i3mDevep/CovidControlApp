package com.ardobot.ardocontrol.scanPeople.presenter;

import com.ardobot.ardocontrol.scanPeople.interactors.ScanActivityInteractors;
import com.ardobot.ardocontrol.scanPeople.interactors.ScanActivityInteractorsImple;
import com.ardobot.ardocontrol.scanPeople.view.ScanPeopleActivityView;
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
        scanPeopleActivityView.successSendDataFirestore("Datos almacenados con exito!");
        scanPeopleActivityView.disableButtonSend(); // new
        scanPeopleActivityView.clearEditText();
    }

    @Override
    public void errorSetDataFirestore(String err) {
        scanPeopleActivityView.enableButtonSend();
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
    public void sendTrackingWorker(String cc, boolean action, String temperature, GeoPoint loc, String address) {
        scanPeopleActivityView.disableButtonSend();
        String[] ids = scanPeopleActivityView.getIds();
        scanPeopleActivityView.showProgressBar(true);
        scanActivityInteractors.sendDataFirebase(cc, action, ids[0], ids[1], temperature, loc, address);
    }
}
