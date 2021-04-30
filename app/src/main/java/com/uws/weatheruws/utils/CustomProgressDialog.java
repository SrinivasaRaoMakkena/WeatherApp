package com.uws.weatheruws.utils;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.uws.weatheruws.R;

public class CustomProgressDialog {

    private static AlertDialog alertDialog;

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

    public static void showProgressBarDialog(final AppCompatActivity activity, String msg) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.item_progress_dialog, null);
        dialogBuilder.setView(dialogView);
        TextView text = dialogView.findViewById(R.id.progressBarNote);
        ProgressBar progressBar = dialogView.findViewById(R.id.progressBarLoader);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
        text.setText(msg);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    public static void hideProgressbarDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}
