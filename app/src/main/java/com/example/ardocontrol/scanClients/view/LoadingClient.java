package com.example.ardocontrol.scanClients.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.ardocontrol.R;

public class LoadingClient {
    private Activity activity;
    private AlertDialog dialog;

    public LoadingClient(Activity activity) {
        this.activity = activity;
    }
    void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custome_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }
    void dismissDialog() {
        dialog.dismiss();
    }
}
