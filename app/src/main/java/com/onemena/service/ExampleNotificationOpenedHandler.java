package com.onemena.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.onemena.app.NewsApplication;
import com.onemena.app.activity.BrowerOpenNewsDetailActivity;
import com.onemena.utils.LogManager;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */

public class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.
    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String newsId = "";
        if (data != null) {
            newsId = data.optString("id", "");
            if (StringUtils.isBlank(newsId)) {
                newsId = data.optString("Id", "");
            }
            if (StringUtils.isBlank(newsId)) {
                newsId = data.optString("ID", "");
            }
            if (StringUtils.isBlank(newsId)) {
                newsId = data.optString("id", "");
            }
        }
        LogManager.i("-----" + newsId);

        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

        LogManager.i("Message", "recevier+" + newsId);
        //判断app进程是否存活
        if (getAppSatus(NewsApplication.getInstance(), NewsApplication.getInstance().getPackageName()) != 3) {
            //如果存活的话，就直接启动DetailActivity，但要考虑一种情况，就是app的进程虽然仍然在
            //但Task栈已经空了，比如用户点击Back键退出应用，但进程还没有被系统回收，如果直接启动
            //DetailActivity,再按Back键就不会返回MainActivity了。所以在启动
            //DetailActivity前，要先启动MainActivity。
            LogManager.i("Message", "the app process is alive");

            //将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
            //如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
            //如果Task栈不存在MainActivity实例，则在栈顶创建 58a568039c88c 58a597181dcd8


            FragmentActivity mainActivity = NewsApplication.getInstance().getMainActivity();
            if (mainActivity != null) {
                Intent detailIntent = new Intent(mainActivity, BrowerOpenNewsDetailActivity.class);
                detailIntent.putExtra(BrowerOpenNewsDetailActivity.ID, newsId);
                detailIntent.putExtra(BrowerOpenNewsDetailActivity.NEWS_TYPE, "3");
                mainActivity.startActivity(detailIntent);
            } else {
                Intent detailIntent = new Intent(NewsApplication.getInstance(), BrowerOpenNewsDetailActivity.class);
                detailIntent.putExtra(BrowerOpenNewsDetailActivity.ID, newsId);
                detailIntent.putExtra(BrowerOpenNewsDetailActivity.NEWS_TYPE, "3");
                detailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                NewsApplication.getInstance().startActivity(detailIntent);
            }
        } else {
            //如果app进程已经被杀死，先重新启动app，将DetailActivity的启动参数传入Intent中，参数经过
            //SplashActivity传入MainActivity，此时app的初始化已经完成，在MainActivity中就可以根据传入             //参数跳转到DetailActivity中去了
            LogManager.i("Message", "the app process is dead");
            Intent launchIntent = NewsApplication.getInstance().getPackageManager().
                    getLaunchIntentForPackage(NewsApplication.getInstance().getPackageName());
            // Make sure we have a launcher intent.
            if (launchIntent != null) {
                launchIntent.setFlags(
                        Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                launchIntent.putExtra("id", newsId);
                NewsApplication.getInstance().startActivity(launchIntent);
            }

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
}
