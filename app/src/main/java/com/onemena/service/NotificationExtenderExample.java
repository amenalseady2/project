package com.onemena.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.arabsada.news.R;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

/**
 * Created by Administrator on 2017/2/27.
 */

public class NotificationExtenderExample extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {
        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                // Sets the background notification color to Green on Android 5.0+ devices.

                Bitmap rawBitmap = BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher_noti_n);
                builder.setSmallIcon(R.mipmap.ic_launcher_notifi)
                        .setLargeIcon(rawBitmap)
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setAutoCancel(true);

                return builder;
            }
        };

        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);

        return true;
    }
}
