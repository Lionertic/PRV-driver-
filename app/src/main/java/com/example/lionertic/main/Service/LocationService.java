package com.example.lionertic.main.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.lionertic.main.AsyncTask.SaveLoca;
import com.example.lionertic.main.MainActivity;

public class LocationService extends Service {

    Handler mHandler = new Handler();
    Runnable mRunnable;
    boolean c= true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "My Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("lllll")
                    .setContentText("hhfsodldgal").build();

            startForeground(2, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        saveUserLocation();
        return START_NOT_STICKY;
    }
    private void saveUserLocation() {
        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                if(c) {
                    new SaveLoca(MainActivity.context).execute();
                    mHandler.postDelayed(mRunnable, 5000);
                }
            }
        }, 5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        c=false;
        mHandler.removeCallbacks(mRunnable);
    }
}