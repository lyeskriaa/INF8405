package com.inf8405.projet_inf8405.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.inf8405.projet_inf8405.fireBaseHelper.UserDBHelper;

/**
 * Created by LyesKriaa on 17-04-22.
 * service de update de la localisation en background (meme si lapplication est ferme)
 */

public class LocationService extends Service {
    private static final String TAG = "BOOOOOOOOOOMTESTGPS";
    private static LocationManager mLocationManager = null;
    private static int LOCATION_INTERVAL = 6000;
    private static float LOCATION_DISTANCE = 0.5f;


    private static class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);

            if(UserDBHelper.getInstance().getUsersRef() != null) {
                Log.e(TAG, "onLocationChanged: userRef not null " + location);
                if(UserDBHelper.getInstance().getCurrentUser() != null) {
                       Log.e(TAG, "update LOCATION : " + location);
                    UserDBHelper.getInstance().updateUserLocation(location, UserDBHelper.getInstance().getCurrentUser().getId());
                }
            }

        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    public static final LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    public static int getLocationInterval() {
        return LOCATION_INTERVAL;
    }

    public static void setLocationInterval(int locationInterval) {
        LOCATION_INTERVAL = (locationInterval > 0) ? locationInterval * 60000 : 100 ;
        initConditionUpdateLocation();
    }

    public static float getLocationDistance() {
        return LOCATION_DISTANCE;
    }

    public static void setLocationDistance(float locationDistance) {
        LOCATION_DISTANCE = locationDistance;
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        com.inf8405.projet_inf8405.services.NetworkStatusService.LOCATION_SERVICE_STARTED = true;
        initializeLocationManager();
        initConditionUpdateLocation();
    }

    private static void initConditionUpdateLocation() {
        try {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        com.inf8405.projet_inf8405.services.NetworkStatusService.LOCATION_SERVICE_STARTED = false;
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        //Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


}
