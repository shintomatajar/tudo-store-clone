package com.tudomart.store.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tudomart.store.R;
import com.tudomart.store.ui.activities.dash.DashboardActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {

    Bitmap mIcon1;
    private static final String TAG = "MyFirebaseIIDService";


    //this method will be called
    //when the token is generated
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage /*message*/) {
        super.onMessageReceived(remoteMessage);

        String icon = remoteMessage.getNotification().getIcon();
        URL url_value = null;
        try {
            url_value = new URL(icon);
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
        try {
            /* Bitmap*/ mIcon1 = BitmapFactory.decodeStream(url_value.openConnection().getInputStream());


            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(new NotificationCompat.BigTextStyle())
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    // .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(mIcon1)
                    .setSmallIcon(R.drawable.ic_delivery_truck)
                    // .setLargeIcon(BitmapFactory. decodeResource (getResources() , R.drawable. ic_launcher_foreground ))
                    .setAutoCancel(true);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());

        } catch (IOException ex) {
            ex.printStackTrace();
        }


        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                // .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(mIcon1)
                .setSmallIcon(R.drawable.ic_delivery_truck).setAutoCancel(true).setContentIntent(pendingIntent);;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
    }


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        //Log.d(TAG, "Refreshed token: " + token);
        // make a own server request here using your http client
    }
}