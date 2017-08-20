package com.onemena.lock;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

/**
 * LockService Created by voler on 2017/6/9.
 * 说明：
 */

public class LockService extends Service {
    private String TAG = this.getClass().getSimpleName();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("-----","start");
        IntentFilter mScreenOnFilter = new IntentFilter();
        mScreenOnFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mScreenOnFilter.addAction(Intent.ACTION_SCREEN_ON);
        LockService.this.registerReceiver(mScreenActionReceiver, mScreenOnFilter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mScreenActionReceiver);
        // 在此重新启动,使服务常驻内存
//        startService(new Intent(this, LockService.class));
    }


    private BroadcastReceiver mScreenActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("-----","get");
            if (action.equals(Intent.ACTION_SCREEN_ON)) {
            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                Log.e(TAG, "screen off");
                Intent LockIntent = new Intent(context, MyLockScreenActivity.class);
                LockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(LockIntent);
            }
        }
    };
}
