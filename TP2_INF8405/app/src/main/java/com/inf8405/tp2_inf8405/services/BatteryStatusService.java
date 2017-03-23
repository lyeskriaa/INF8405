package com.inf8405.tp2_inf8405.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * Created by LyesKriaa on 17-03-23.
 */

public class BatteryStatusService extends BroadcastReceiver {

    private float lastBatteryLevel;
    private float initialBatteryLevel;
    private Intent batteryStatus;

    public BatteryStatusService(){
        lastBatteryLevel =0f;
        initialBatteryLevel = 0f;
        batteryStatus = new Intent();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        this.batteryStatus = context.registerReceiver(null, ifilter);
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