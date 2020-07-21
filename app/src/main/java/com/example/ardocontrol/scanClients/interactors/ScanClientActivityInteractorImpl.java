package com.example.ardocontrol.scanClients.interactors;

import android.util.Log;

import com.example.ardocontrol.scanClients.presenter.ScanClientActivityPresenter;

import java.util.Arrays;

public class ScanClientActivityInteractorImpl implements ScanClientActivityInteractor {

    private ScanClientActivityPresenter scanClientActivityPresenter;

    public ScanClientActivityInteractorImpl(ScanClientActivityPresenter scanClientActivityPresenter) {
        this.scanClientActivityPresenter = scanClientActivityPresenter;
    }
    private static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }
    @Override
    public void proccessReadDoc(String scan) {
        String scanContent = scan;
        if (scanContent.indexOf("PubDSK_") != -1) {

            String[] OneFilter = scanContent.split("PubDSK_");
            scanContent = OneFilter[1];
            String[] SecondFilter = scanContent.split("[a-zA-Z]");
            String[] ThirdFilter = OneFilter[1].replaceAll("^\\s+","").split("[^\\w]+");
            scanContent = SecondFilter[0];
            String identification = scanContent.substring(17, scanContent.length());
            try {
                int idInt = Integer.parseInt(identification.trim());
                String Name =  ThirdFilter[1].split(String.valueOf(idInt))[1];
                String Date = "";
                String Gender = "";
                for (int i = 2; i <  6; i++){
                    String firtCaracter = ThirdFilter[i].substring(0, 1);
                    if(!isNumeric(firtCaracter)){
                        Name = Name + " " + ThirdFilter[i];
                    }else {
                        if(ThirdFilter[i].charAt(1) == 'M'){
                            Gender = "Hombre";
                        }else if(ThirdFilter[i].charAt(1) == 'F'){
                            Gender = "Mujer";
                        }
                        Date = ThirdFilter[i].split(String.valueOf(ThirdFilter[i].charAt(1)))[1].substring(0,8);
                    }
                }
                String [] result = new String[]{String.valueOf(idInt), Name, Gender, Date };
                scanClientActivityPresenter.successReadDoc(result);
            }catch (Exception e){
                scanClientActivityPresenter.errorReadDoc("this action was not possible");
            }
        }
    }

}
