package com.example.biztositas;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String channelId = "insurance_notification_channel";
    private final int notificationId = 0;

    private NotificationManager mManager;
    private Context mContext;


    public NotificationHandler(Context context){
        this.mContext = context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        }else{
            NotificationChannel channel = new NotificationChannel(channelId,"Insurance Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.GREEN);
            channel.setDescription("Nézz rá a biztosításaidra.");
            this.mManager.createNotificationChannel(channel);
        }
    }

    public void send(String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext,channelId)
                .setContentTitle("Biztosítások")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_directions_car_24);
        this.mManager.notify(notificationId,builder.build());
    }

}
