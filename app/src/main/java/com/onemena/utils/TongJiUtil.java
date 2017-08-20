package com.onemena.utils;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.arabsada.news.BuildConfig;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.onemena.app.NewsApplication;
import com.onemena.app.config.Config;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.TJKey;
import com.onemena.data.UserManager;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.service.AsyHttp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by WHF on 2016-12-02.
 */

public class TongJiUtil {

    public static volatile TongJiUtil mInstance;
    private Context mContext;

    public static TongJiUtil init(Context context) {
        if (mInstance == null) {
            synchronized (TongJiUtil.class) {
                if (mInstance == null) {
                    mInstance = new TongJiUtil();
                    mInstance.mContext = context;
                }
            }
        }
        return mInstance;
    }

    public static TongJiUtil getInstance() {
        return mInstance;
    }

    public void putStatistics(String mapKey, Map<String, String> map) {
        Bundle params = new Bundle();
        params.putString("device_id", UserManager.getUserObj().getString("imei"));
        for (Map.Entry<String, String> entry : map.entrySet()) {
            params.putString(entry.getKey(), entry.getValue());
        }

        FirebaseAnalytics.getInstance(mContext).logEvent(mapKey, params);

    }

    public void putEntries(@TJKey.Name String mapKey, MyEntry... myEntries) {

        String android_id = GetPhoneInfoUtil.INSTANCE.getAndroidId();
        String uid = android_id;
        String userinfo = UserManager.getUserObj().getString("userinfo");
        if (StringUtils.isNotEmpty(userinfo)) {
            JSONObject jsonObject = JSONObject.parseObject(userinfo);
            uid = org.apache.commons.lang3.StringUtils.defaultString(jsonObject.getString("id"), android_id);
        }
        Bundle params = new Bundle();
        params.putString("device_id", android_id);
        params.putString("user_id", uid);
        for (MyEntry entry : myEntries) {
            params.putString(entry.key, entry.value);
        }
        FirebaseAnalytics.getInstance(mContext).logEvent(mapKey, params);
    }

    public void putStatistics(MyEntry... myEntries) {
        if (BuildConfig.IS_DEBUG) {
            return;
        }
        if (Config.imgReport) {
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            Date date = new Date();
            String android_id = GetPhoneInfoUtil.INSTANCE.getAndroidId();
            String uid = "";
            String userinfo = UserManager.getUserObj().getString("userinfo");
            if (StringUtils.isEmpty(userinfo)) {
                uid = android_id;
            } else {
                JSONObject jsonObject = JSONObject.parseObject(userinfo);
                if (jsonObject != null) {
                    uid = org.apache.commons.lang3.StringUtils.defaultString(jsonObject.getString("id"), android_id);
                } else {
                    uid = android_id;
                }
            }
            HashMap<String, String> params = new HashMap<>();
            params.put("app", "2");//arabsada
            params.put("uid", uid);
            params.put("devid", android_id);
            params.put("platform", "android");
            params.put("acTime", dateFormater.format(date));
            for (MyEntry entry : myEntries) {
                params.put(entry.key, entry.value);
            }
            AsyHttp.get(ConfigUrls.HOST_STATISTICS, params, new AsyHttp.ResponseListener() {
                @Override
                public void onGetResponseSuccess(int requestCode, String response) {

                }

                @Override
                public void onGetResponseError(int requestCode, VolleyError error, String cacheResponse) {

                }

                @Override
                public void onResponse(NetworkResponse response) {

                }
            });
        }
    }

    public static EventAction putEventAction(String acType, String docType) {
        return new EventAction(acType, docType);
    }

    public static final class EventAction {
        private Map<String, String> params;
        private String acType;
        private String docType;

        private EventAction(String acType, String docType) {
            this.params = new HashMap<>();
            this.acType = acType;
            this.docType = docType;
        }

        public EventAction putDocId(String docId) {
            params.put(TJKey.DOCID, docId);
            return this;
        }

        public EventAction putCatgId(String catgId) {
            params.put(TJKey.CATGID, catgId);
            return this;
        }

        public EventAction put(@TJKey.Query String key, String value) {
            params.put(key, value);
            return this;
        }

        public void commit() {
            if (BuildConfig.IS_DEBUG || !Config.imgReport) return;
            SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            Date date = new Date();
            String android_id = GetPhoneInfoUtil.INSTANCE.getAndroidId();
            String uid = "";
            String userinfo = UserManager.getUserObj().getString("userinfo");
            if (StringUtils.isEmpty(userinfo)) {
                uid = android_id;
            } else {
                JSONObject jsonObject = JSONObject.parseObject(userinfo);
                if (jsonObject != null) {
                    uid = org.apache.commons.lang3.StringUtils.defaultString(jsonObject.getString("id"), android_id);
                } else {
                    uid = android_id;
                }
            }
            params.put("app", "2");//arabsada
            params.put("uid", uid);
            params.put("devid", android_id);
            params.put("platform", "android");
            params.put("acTime", dateFormater.format(date));
            params.put(TJKey.ACTYPE, acType);
            params.put(TJKey.DOCTYPE, docType);
            AsyHttp.get(ConfigUrls.HOST_STATISTICS, params, new AsyHttp.ResponseListener() {
                @Override
                public void onGetResponseSuccess(int requestCode, String response) {

                }

                @Override
                public void onGetResponseError(int requestCode, VolleyError error, String cacheResponse) {

                }

                @Override
                public void onResponse(NetworkResponse response) {

                }
            });
        }


        public static FirebaseEvent putEventName(@TJKey.Name String eventName) {
            return new FirebaseEvent(eventName);
        }


        public static final class FirebaseEvent {
            Bundle params;
            String eventName;

            private FirebaseEvent(String eventName) {
                this.eventName = eventName;
            }

            public FirebaseEvent put(@TJKey.Query String key, String value) {
                params.putString(key, value);
                return this;
            }

            public FirebaseEvent putLoginType(@TJKey.LoginType String value) {
                params.putString(TJKey.TYPE, value);
                return this;
            }

            public void commit() {
                String android_id = GetPhoneInfoUtil.INSTANCE.getAndroidId();
                String uid = "";
                String userinfo = UserManager.getUserObj().getString("userinfo");
                if (StringUtils.isNotEmpty(userinfo)) {
                    JSONObject jsonObject = JSONObject.parseObject(userinfo);
                    uid = org.apache.commons.lang3.StringUtils.defaultString(jsonObject.getString("id"), android_id);
                }
                params.putString("device_id", android_id);
                params.putString("user_id", uid);
                FirebaseAnalytics.getInstance(NewsApplication.getInstance()).logEvent(eventName, params);
            }
        }
    }

    public static void putEntry(String mapKey, Bundle params) {

        String android_id = GetPhoneInfoUtil.INSTANCE.getAndroidId();
        String uid = android_id;
        String userinfo = UserManager.getUserObj().getString("userinfo");
        if (StringUtils.isNotEmpty(userinfo)) {
            JSONObject jsonObject = JSONObject.parseObject(userinfo);
            uid = org.apache.commons.lang3.StringUtils.defaultString(jsonObject.getString("id"), android_id);
        }
        params.putString("device_id", android_id);
        params.putString("user_id", uid);
        FirebaseAnalytics.getInstance(NewsApplication.getInstance()).logEvent(mapKey, params);
    }


}
