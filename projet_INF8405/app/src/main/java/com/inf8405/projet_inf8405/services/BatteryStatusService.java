package com.inf8405.projet_inf8405.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.inf8405.projet_inf8405.activities.MapsActivity;

/**
 * Created by LyesKriaa on 17-04-24.
 */

public class BatteryStatusService extends BroadcastReceiver {

    private float lastBatteryLevel;
    private float initialBatteryLevel;
    private Intent batteryStatus;
    private boolean notify = false;

    public BatteryStatusService(){
        lastBatteryLevel =0f;
        initialBatteryLevel = 0f;
        batteryStatus = new Intent();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        this.batteryStatus = context.registerReceiver(null, ifilter);
        if(GetCurrentBatteryLevel() <= 16.0 && !notify) {
            //Log.e("BAAAAATTERRYY SLOW", String.valueOf(GetCurrentBatteryLevel()));
            if(MapsActivity.getMapsActivity() != null) {
                notify = true;
                MapsActivity.getMapsActivity().notifySaveEnergy(); // afin de mettre un delai de 30 minutes
            }
        }
        if(GetCurrentBatteryLevel() > 15.0) {
            //Log.e("BAAAAATTERRYY OK", String.valueOf(GetCurrentBatteryLevel()));
            notify = false;
        }
    }

    public void SetInitialBatteryLevel(float level)
    {
        initialBatteryLevel = level;
    }

    public float GetCurrentBatteryLevel()
    {
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = (float)level / (float)scale * 100.0f;
        return batteryPct;
    }

    public float GetTotalBatteryUsage()
    {
        return  GetCurrentBatteryLevel() - initialBatteryLevel;
    }
    public void SaveCurrentBatteryUsage()
    {
        this.lastBatteryLevel = this.GetCurrentBatteryLevel();
    }
    public float GetLastBatteryUsage() {
        float latestBatteryLevel = GetCurrentBatteryLevel();
        return latestBatteryLevel - lastBatteryLevel;
    }
}
