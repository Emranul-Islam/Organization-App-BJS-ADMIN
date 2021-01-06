package com.muhammad_sohag.admin_bjs.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.muhammad_sohag.admin_bjs.MainActivity;
import com.muhammad_sohag.admin_bjs.R;
import com.muhammad_sohag.admin_bjs.UpdateData;


public class NotificationService extends FirebaseMessagingService {

    private final String CHANNEL = "notification1";
    private int notificationId = 100;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    private void showNotification(String title, String body) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationChannel(notificationManager);
        }


        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent resultIntent = new Intent(this, UpdateData.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(notificationSound)
                .setLights(Color.GREEN, 0, 2000)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(notificationId, notificationBuilder.build());
        if (notificationId > 10000) {
            notificationId = 0;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notificationChannel(NotificationManager notificationManager) {
        NotificationChannel notificationChannel = new NotificationChannel("myChannel", CHANNEL, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription("myChannel");
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.GREEN);
        notificationManager.createNotificationChannel(notificationChannel);


    }
}

