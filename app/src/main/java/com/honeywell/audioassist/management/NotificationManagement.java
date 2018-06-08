package com.honeywell.audioassist.management;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.honeywell.audioassist.R;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationManagement {

    private final static AtomicInteger id = new AtomicInteger(0);
    public static int getId(){
        return id.incrementAndGet();
    }

    public static void registerChannel(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.event_alerts_channel);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(context.getString(R.string.event_alerts_channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
