package com.example.ardocontrol.scanPeople.interactors;

import android.os.Build;
import android.util.Log;

import com.example.ardocontrol.scanPeople.presenter.ScanActivityPresentor;

public class ScanActivityInteractorsImple implements ScanActivityInteractors {

    private ScanActivityPresentor scanActivityPresentor;

    public ScanActivityInteractorsImple(ScanActivityPresentor scanActivityPresentor) {
        this.scanActivityPresentor = scanActivityPresentor;
    }

    @Override
    public void processData(String data) {
        String scanContent = data;
        if (scanContent.indexOf("PubDSK_") != -1) {
            int contador = 0;
            for (int i = 0; i < scanContent.indexOf("PubDSK_"); i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (Character.isDigit(scanContent.charAt(i)) || Character.isAlphabetic(scanContent.charAt(i))) {
                        if (contador == 0 && (scanContent.charAt(i) == 'I' || scanContent.charAt(i) == 'i')){
                            i = scanContent.indexOf("PubDSK_");
                            contador = 0;
                        }else {
                            contador = contador + 1;
                        }
                    }
                }
            }
            String[] OneFilter = scanContent.split("PubDSK_");
            scanContent = OneFilter[1];
            String[] SecondFilter = scanContent.split("[a-zA-Z]");
            scanContent = SecondFilter[0];
            String identification = scanContent.substring(scanContent.length()-contador);
            scanActivityPresentor.processError(identification);
        }
    }

    @Override
    public void sendData(String[] content) {

    }
}
