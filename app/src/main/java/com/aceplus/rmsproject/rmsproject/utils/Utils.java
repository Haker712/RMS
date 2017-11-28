package com.aceplus.rmsproject.rmsproject.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.aceplus.rmsproject.rmsproject.HomePageActivity;
import com.aceplus.rmsproject.rmsproject.MainActivity;
import com.aceplus.rmsproject.rmsproject.R;

/**
 * Created by DELL on 11/2/2017.
 */

public class Utils {

    public static ProgressDialog progressDialog;

    public static void callProgressDialog(final String message, final Activity activity) {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity, ProgressDialog.THEME_HOLO_LIGHT);
        }

        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (activity.isFinishing()) {
                    return;
                } else {
                    progressDialog = new ProgressDialog(activity, ProgressDialog.THEME_HOLO_LIGHT);
                    progressDialog.setIndeterminate(false);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage(message);
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            }
        });
    }

    public static void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    public static void callInfoDialog(String message, Context context) {
        final android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.InvitationDialog)
                .setPositiveButton(R.string.invitation_ok, null)
                .create();
        builder.setTitle(R.string.alert);
        builder.setMessage(message);
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button btnAccept = builder.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
            }
        });
        builder.show();
    }

}
