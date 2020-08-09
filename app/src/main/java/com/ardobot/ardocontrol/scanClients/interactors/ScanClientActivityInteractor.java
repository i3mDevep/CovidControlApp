package com.ardobot.ardocontrol.scanClients.interactors;

public interface ScanClientActivityInteractor {
    void proccessReadDoc(String scan, String[] ids);
    void sendDataFirebase(String idCompany, String idSubCompany, String name, String identification, String temperature, String birth, String address, String gender, String readGps, String cellphone, String cause);
    void searchClient(String idCompany, String identification);
}
