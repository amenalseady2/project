package com.onemena.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.BuildConfig;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onemena.app.config.AppConfig;
import com.onemena.app.config.ConfigUrls;
import com.onemena.listener.JsonObjectListener;
import com.onemena.utils.GetPhoneInfoUtil;
import com.onesignal.OneSignal;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

/**
 * BackgroundService Created by voler on 2017/6/20.
 * 说明：
 */

public class BackgroundService extends Service {

    private Subscription subscribe;

    @Override
    public void onCreate() {
        super.onCreate();
        subscribe = Observable.interval(20, 60000, TimeUnit.SECONDS)
                .subscribe(aLong -> sendOnesignalToken());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void sendOnesignalToken() {
        OneSignal.idsAvailable((userId, registrationId) -> {
            if (StringUtils.isBlank(registrationId)) {
                registrationId = FirebaseInstanceId.getInstance().getToken();
            }
            if (StringUtils.isNotBlank(registrationId)) {
                HashMap<String, String> map_fb = new HashMap<>();
                map_fb.put("fb_token", registrationId);
                map_fb.put("osn_token", StringUtils.defaultString(userId, ""));
                map_fb.put("plate_form", "Android");
                map_fb.put("app_version", AppConfig.VERSION_NAME);
                PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.NEWS_FB_TOKEN, map_fb, new JsonObjectListener() {
                    @Override
                    public void onJsonObject(JSONObject obj, Boolean isCacheData) {
                        subscribe.unsubscribe();
                    }

                    @Override
                    public void onObjError() {
                    }
                });
                if (BuildConfig.IS_DEBUG) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("system", "android");
                    hashMap.put("type", "2");
                    hashMap.put("playerid", userId);
                    hashMap.put("deviceid", GetPhoneInfoUtil.INSTANCE.getAndroidId());
                    PublicService.getInstance().postJsonObjectRequest(false, "http://app.mysada.com/api/onesignal/take", hashMap, new JsonObjectListener() {
                        @Override
                        public void onJsonObject(JSONObject obj, Boolean isCacheData) throws Exception {

                        }

                        @Override
                        public void onObjError() {

                        }
                    });
                }
            }
            Log.d("debug______", "User:" + userId + "registrationId:" + registrationId);
        });
    }
}
