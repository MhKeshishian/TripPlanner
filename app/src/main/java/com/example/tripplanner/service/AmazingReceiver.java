/**
 * FILE          : AmazingReceiver.java
 * AUTHOR        : Mher Keshishian
 * FIRST VERSION : 2024-04-03
 * PURPOSE       : BroadcastReceiver to listen for amazing events and perform actions accordingly.
 */

package com.example.tripplanner.service;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import com.example.tripplanner.MainActivity;

public class AmazingReceiver extends BroadcastReceiver {

    private static final String AMAZING_TASK_DONE_ACTION = "com.example.tripplanner.action.AMAZING_TASK_DONE";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action == null) return;

        Log.d("AmazingReceiver", "Received action: " + action);

        switch (action) {
            case ConnectivityManager.CONNECTIVITY_ACTION:
                handleConnectivityChange(context);
                break;
            case Intent.ACTION_TIMEZONE_CHANGED:
                handleTimeZoneChange(context);
                break;
            case AMAZING_TASK_DONE_ACTION:
                handleAmazingTaskDone();
                break;
        }
    }

    private void handleConnectivityChange(Context context) {
        boolean isConnected = isDeviceConnectedToInternet(context);

        if (!isConnected) {
            showNoInternetDialog(context);
        }
    }

    private void handleTimeZoneChange(Context context) {
        showTimeZoneChangeDialog(context);
    }

    private void handleAmazingTaskDone() {
        Log.d("AmazingReceiver", "Amazing task done event received");
        // Perform actions when amazing task is done
    }

    private boolean isDeviceConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }
        return false; // Device is not connected to the internet
    }

    private void showNoInternetDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Internet Connection")
                .setMessage("This app requires an internet connection. Please connect to the internet.")
                .setPositiveButton("Close App", (dialog, which) -> {
                    // Quit the app
                    ((MainActivity) context).finishAffinity();
                })
                .setCancelable(false)
                .show();
    }

    private void showTimeZoneChangeDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Time Zone Change")
                .setMessage("The time zone has changed. The dates of your trip may have shifted. It is recommended to review the date range of your trip.")
                .setPositiveButton("Close", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }
}

