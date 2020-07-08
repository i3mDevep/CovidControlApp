package com.example.ardocontrol.scanPeople.presenter;

import com.example.ardocontrol.scanPeople.interactors.ScanActivityInteractors;
import com.example.ardocontrol.scanPeople.interactors.ScanActivityInteractorsImple;
import com.example.ardocontrol.scanPeople.view.ScanPeopleActivityView;

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
    public void processSuccess() {

    }

    @Override
    public void processError(String err) {
        scanPeopleActivityView.errorRead(err);
    }
}
