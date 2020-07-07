package com.example.ardocontrol.menu.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import com.example.ardocontrol.menu.presenter.MenuActivityPresenter;


public class MenuDialogConfirmExit {
    private Activity activity;
    private MenuActivityPresenter menuActivityPresenter;
    private Dialog dialog;

    public MenuDialogConfirmExit(Activity activity, MenuActivityPresenter activityPresenter) {
        this.activity = activity;
        this.menuActivityPresenter = activityPresenter;
    }
    void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Estas seguro que desea cerrar sesion ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                menuActivityPresenter.Loggout();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        dialog = builder.create();
        dialog.show();
    }
}
