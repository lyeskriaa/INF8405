package com.inf8405.tp2_inf8405.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.inf8405.tp2_inf8405.R;

/**
 * Created by LyesKriaa on 17-03-16.
 * @source : https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html#DetermineType
 */

public class NetworkStatusService extends BroadcastReceiver {
    public static boolean LOCATION_SERVICE_STARTED = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            Intent locationService = new Intent(context, LocationService.class);
            context.stopService(locationService);
            Toast.makeText(context, R.string.network_unactive,Toast.LENGTH_LONG).show();
        }
        else {
            if(!LOCATION_SERVICE_STARTED) {
                Intent locationService = new Intent(context, LocationService.class);
                context.startService(locationService);
            }
        }
    }
}
