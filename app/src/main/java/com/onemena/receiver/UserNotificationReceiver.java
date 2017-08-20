package com.onemena.receiver;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.arabsada.news.R;
import com.onemena.app.activity.SplashActivity;
import com.onemena.app.config.SPKey;
import com.onemena.utils.SpUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2016/12/27.
 */

public class UserNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //判断app进程是否存活
        if (getAppSatus(context, context.getPackageName()) != 3) {
            if (!SpUtil.getBoolean(SPKey.NOTICETIME,false)){
                putUserNextNotification(context);
//                SpUtil.saveValue(SPKey.NOTICETIME,true);
            }


        } else {
            if (SpUtil.getBoolean(SPKey.NOTICETIME,false)) {
                shownotification(context, context.getString(R.string.notification_two));
            }else {
                shownotification(context, context.getString(R.string.notification_first));
                SpUtil.saveValue(SPKey.NOTICETIME,true);
                putUserNextNotification(context);
            }



        }
    }

    public void shownotification(Context context, String msg) {
        Intent detailIntent = new Intent(context, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.
                getActivity(context, 0, detailIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification notification = new Notification();
//        notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
//        notification.icon = R.mipmap.ic_launcher;
//        notification.contentIntent = pendingIntent;
//        notification.flags = Notification.FLAG_AUTO_CANCEL;
//        notification.contentView = new RemoteViews(context.getPackageName(), R.layout.message_notification);
//        notification.contentView.setTextViewText(R.id.name_tv, context.getResources().getString(R.string.app_name));
//        notification.contentView.setTextViewText(R.id.news_context, msg);
        Bitmap rawBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.ic_launcher_noti_n);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_notifi)
                .setLargeIcon(rawBitmap)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(msg)
                .setContentIntent(pendingIntent)
                .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.illuminate))
//                .setDefaults(R.raw.illuminate)
                .setAutoCancel(true)
                .build();

        Log.i("repeat", "showNotification");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (!SpUtil.getBoolean(SPKey.NOTICETIME,false)){
            manager.notify(2, notification);
        }else {
            manager.notify(3, notification);
        }

    }

    /**
     * 返回app运行状态
     * 1:程序在前台运行
     * 2:程序在后台运行
     * 3:程序未启动
     * 注意：需要配置权限<uses-permission android:name="android.permission.GET_TASKS" />
     */
    public int getAppSatus(Context context, String pageName) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(20);

        //判断程序是否在栈顶
        if (list.get(0).topActivity.getPackageName().equals(pageName)) {
            return 1;
        } else {
            //判断程序是否在栈里
            for (ActivityManager.RunningTaskInfo info : list) {
                if (info.topActivity.getPackageName().equals(pageName)) {
                    return 2;
                }
            }
            return 3;//栈里找不到，返回3
        }
    }


    private void putUserNextNotification(Context context){

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.android.USERNOTIFICATION_RECEIVER_ARABSADA");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT );

        AlarmManager manager=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP,nextDataTime(), pendingIntent);

    }


    private long nextDataTime(){

        Date date = new Date();
        //通过日历获取下一天日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, +1);
        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE),20,0,0);
        return cal.getTime().getTime();
    }

}
