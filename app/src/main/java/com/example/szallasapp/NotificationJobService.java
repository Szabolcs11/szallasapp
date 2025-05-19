package com.example.szallasapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        sendNotification();
        return false; // Ha a feladat azonnal befejeződik
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false; // Ha a feladat leállításra kerül
    }

    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default_channel")
                .setSmallIcon(R.drawable.hotel1)
                .setContentTitle("Emlékeztető")
                .setContentText("Nézd meg legújabb szállásainkat!")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Ha Android 8.0+ van
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "default_channel",
                    "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(1, builder.build());
    }
}
