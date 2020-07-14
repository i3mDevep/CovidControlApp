package com.example.ardocontrol.scanPeople.interactors;

import android.os.Build;
import android.util.Log;

import com.example.ardocontrol.scanPeople.presenter.ScanActivityPresentor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class ScanActivityInteractorsImple implements ScanActivityInteractors {

    private ScanActivityPresentor scanActivityPresentor;
    private FirebaseFirestore db;


    public ScanActivityInteractorsImple(ScanActivityPresentor scanActivityPresentor) {
        this.scanActivityPresentor = scanActivityPresentor;
    }

    @Override
    public void processData(String data) {
        String scanContent = data;
        if (scanContent.indexOf("PubDSK_") != -1) {
            String[] OneFilter = scanContent.split("PubDSK_");
            scanContent = OneFilter[1];
            String[] SecondFilter = scanContent.split("[a-zA-Z]");
            scanContent = SecondFilter[0];
            String identification = scanContent.substring(17, scanContent.length());
            try {
                int idInt = Integer.parseInt(identification.trim());
                scanActivityPresentor.processSuccess(String.valueOf(idInt));
            }catch (Exception e){
                scanActivityPresentor.processError("this action was not possible");
            }
        } else if(scanContent.indexOf("qrardobot") != -1){
            String[] OneFilter = scanContent.split(",,");
            scanContent = OneFilter[2];
            String[] SecondFilter = scanContent.split(",");
            scanContent = SecondFilter[2];
            scanActivityPresentor.processSuccess(scanContent);
        }else {
            scanActivityPresentor.processError("Format not allow!");
        }
    }

    @Override
    public void sendDataFirebase(String cc, boolean action, final String idCompany, final String idSubCompany, String temperature) {

        if(temperature.equals("")){
            scanActivityPresentor.errorSetDataFirestore("Selecciona un valor de temperatura!");
            return;
        }

        final Map<String, Object> data = new HashMap<>();
        String type = action ? "in":"out";

        data.put("action", type);
        data.put("identification", cc);
        data.put("time", FieldValue.serverTimestamp());
        data.put("temperature", temperature);

        db = FirebaseFirestore.getInstance();
        String ref = "business/" + idCompany + "/subcompanies/" + idSubCompany + "/trakingworker";
        db.collection(ref)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String ref = "business/" + idCompany + "/trakingworker";
                        db.collection(ref)
                                .add(data)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        scanActivityPresentor.successSetDataFirestore();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        scanActivityPresentor.errorSetDataFirestore(e.getMessage());
                    }
                });
    }

    @Override
    public void getDataFirebase(String idCompany, String idSubCompany, final String cc) {
        db = FirebaseFirestore.getInstance();
        String ref = "business/" + idCompany + "/subcompanies/" + idSubCompany + "/worker/" + cc;
        db.document(ref)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.get("name").toString();
                                String lastname = document.get("lastname").toString();
                                String cellphone = document.get("celphone").toString();
                                String gender = document.get("gender").toString();
                                String address = document.get("address").toString();
                                String data[] = new String[]{name + " " + lastname, cellphone, gender, address};
                                scanActivityPresentor.successGetDataFirebase(data);
                            } else {
                                scanActivityPresentor.errorGetDataFirebase("No such document");
                            }
                        } else {
                            scanActivityPresentor.errorGetDataFirebase("get failed with ");
                        }
                    }
                });

    }

}
