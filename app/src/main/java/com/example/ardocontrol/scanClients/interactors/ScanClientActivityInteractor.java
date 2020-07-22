package com.example.ardocontrol.scanClients.interactors;

public interface ScanClientActivityInteractor {
    void proccessReadDoc(String scan, String[] ids);
    void sendDataFirebase(String idCompany, String idSubCompany, String name, String identification, String temperature, String age, String address, String gender, String readGps, String cellphone);
    void searchClient(String idCompany, String identification);
}
