package com.ardobot.ardocontrol.scanClients.interactors;

import android.util.Log;

import com.ardobot.ardocontrol.scanClients.presenter.ScanClientActivityPresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import androidx.annotation.NonNull;


public class ScanClientActivityInteractorImpl implements ScanClientActivityInteractor {

    private FirebaseFirestore db;

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
    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
    @Override
    public void proccessReadDoc(String scan, String[] ids) {

        String scanContent = scan;
        String idCompany = ids[0];

        if (scanContent.indexOf("PubDSK_") != -1) {

            String[] OneFilter = scanContent.split("PubDSK_");
            scanContent = OneFilter[1];
            String[] SecondFilter = scanContent.split("[a-zA-Z]");
            String[] ThirdFilter = OneFilter[1].replaceAll("^\\s+","").split("[^\\w]+");
            scanContent = SecondFilter[0];
            String identification = scanContent.substring(17, scanContent.length());

            try {
                final int idInt = Integer.parseInt(identification.trim());

                String Name =  "";
                int posInit = 0;
                for(int i= 1; i <  6; i++) {
                    if(ThirdFilter[i].indexOf(String.valueOf(idInt)) != -1){
                        Name = ThirdFilter[i].split(String.valueOf(idInt))[1];
                        posInit = i;
                        break;
                    }
                }
                String Date = "";
                String Gender = "";
                for (int i = 1 + posInit; i <  6 + posInit; i++){
                    String firtCaracter = ThirdFilter[i].substring(0, 1);
                    if(!isNumeric(firtCaracter)){
                        Name = Name + " " + ThirdFilter[i];
                    }else {
                        if(ThirdFilter[i].charAt(1) == 'M'){
                            Gender = "Hombre";
                            Date = ThirdFilter[i].split(String.valueOf(ThirdFilter[i].charAt(1)))[1].substring(0,8);
                            break;
                        }else if(ThirdFilter[i].charAt(1) == 'F'){
                            Gender = "Mujer";
                            Date = ThirdFilter[i].split(String.valueOf(ThirdFilter[i].charAt(1)))[1].substring(0,8);
                            break;
                        }
                    }
                }
                String birth_;
                try {
                    birth_= Date.substring(6, 8) + "/" + Date.substring(4, 6) + "/" + Date.substring(0, 4);
                } catch (Exception err){10/10/2099
                    birth_ = "dd/mm/yyyy";
                }
                db = FirebaseFirestore.getInstance();
                final String ref = "business/" + idCompany + "/clients/" + idInt;
                final String finalName = Name;
                final String finalGender = Gender;
                final String finalBirth_ = birth_;
                db.document(ref).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        String birth = "10/10/2099";
                                        try {
                                            birth = simpleFormat.format(document.getTimestamp("birth").toDate());
                                        } catch (Exception e){
                                            Log.e("ERROR_FORMAT", "Error search",  e);
                                        }
                                        String [] result = new String[] {
                                                document.get("identification").toString(),
                                                document.get("name").toString(),
                                                document.get("gender").toString(),
                                                birth,
                                                document.get("address").toString(),
                                                document.get("cellphone").toString(),
                                        };
                                        scanClientActivityPresenter.successReadDoc(result);
                                    }
                                    else {
                                        String [] result = new String[]{String.valueOf(idInt), finalName, finalGender, finalBirth_, "", ""};
                                        scanClientActivityPresenter.successReadDoc(result);
                                    }
                                } else {
                                    String [] result = new String[]{String.valueOf(idInt), finalName, finalGender, finalBirth_, "", ""};
                                    scanClientActivityPresenter.successReadDoc(result);
                                }
                            }
                        });

            } catch (Exception e){
                scanClientActivityPresenter.errorReadDoc("No fue posible extraer los datos de tu documento");
            }
        }else{
            scanClientActivityPresenter.errorReadDoc("format not allow!");
        }
    }

    @Override
    public void sendDataFirebase(String idCompany, String idSubCompany, String name, String identification, String temperature, String birth, String address, String gender, String readGps, String cellphone, String cause) {

        if(name.equals("") || identification.equals("") || temperature.equals("") || birth.equals("") || address.equals("") || gender.equals("") || readGps.equals("") || cellphone.equals("") || cause.equals("")){
            scanClientActivityPresenter.errorSendData("Completa todos los campos!");
            return;
        }
        Calendar date;
        try {
            String[] mybirth = birth.split("/");
            date = new GregorianCalendar(Integer.parseInt(mybirth[2]), Integer.parseInt(mybirth[1]) - 1, Integer.parseInt(mybirth[0]));
        } catch (Exception e){
            scanClientActivityPresenter.errorSendData("El formato de la fecha no es valido!");
            return;
        }

        final Map<String, Object> content = new HashMap<>();
        content.put("name", name);
        content.put("identification", identification);
        content.put("birth", new Date(date.getTimeInMillis()));
        content.put("address", address);
        content.put("gender", gender);
        content.put("gps", readGps);
        content.put("cellphone", cellphone);
        content.put("time", FieldValue.serverTimestamp());


        final Map<String, Object> contentTracking = new HashMap<>();
        contentTracking.put("temperature", temperature);
        contentTracking.put("gps", readGps);
        contentTracking.put("time", FieldValue.serverTimestamp());
        contentTracking.put("identification", identification);
        contentTracking.put("cause", cause);
        contentTracking.put("idSubcompany", idSubCompany);


        db = FirebaseFirestore.getInstance();

        final String ref = "business/" + idCompany + "/clients/" + identification;
        db.document(ref).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                db.collection(ref+"/tracking").add(contentTracking)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                scanClientActivityPresenter.successSendData();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        scanClientActivityPresenter.errorSendData(e.getMessage());
                                    }
                                });
                            } else {
                                db.document(ref).set(content)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                db.collection(ref+"/tracking").add(contentTracking)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        scanClientActivityPresenter.successSendData();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                scanClientActivityPresenter.errorSendData(e.getMessage());
                                                            }
                                                        });
                                            }
                                        });
                            }
                        } else {
                            scanClientActivityPresenter.errorSendData(task.getException().getMessage());
                        }
                    }
                });

    }

    @Override
    public void searchClient(String idCompany, String identification) {

        if(identification.equals("")){
            scanClientActivityPresenter.errorReadDoc("Ingresa cedula!");
            return;
        }

        db = FirebaseFirestore.getInstance();
        String ref = "business/" + idCompany + "/clients/" + identification;
        db.document(ref).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String birth = "10/10/2099";
                                try {
                                    birth = simpleFormat.format(document.getTimestamp("birth").toDate());
                                } catch (Exception e){
                                    Log.e("ERROR_FORMAT", "Error search",  e);
                                }

                                String [] result = new String[] {
                                        document.get("identification").toString(),
                                        document.get("name").toString(),
                                        document.get("gender").toString(),
                                        birth,
                                        document.get("address").toString(),
                                        document.get("cellphone").toString(),
                                };
                                scanClientActivityPresenter.successReadDoc(result);
                            }
                            else {
                                scanClientActivityPresenter.errorReadDoc("No se encontro esta cedula  en la base de datos");
                            }
                        } else {
                            scanClientActivityPresenter.errorReadDoc("Sucedio un error en la busqueda");
                        }
                    }
                });
    }

}
