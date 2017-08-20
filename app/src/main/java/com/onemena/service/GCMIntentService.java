package com.onemena.service;

import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;


/**
 * Created by WHF on 2016-11-18.
 */
public class GCMIntentService extends GCMBaseIntentService {
    static final String SERVER_URL = "http://192.168.1.10:8080/gcm-demo";
    static final String SENDER_ID = "744689149523";
    @Override
    public void onCreate() {
        super.onCreate();
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            //Log.v(TAG, "Already registered");
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {

    }

    @Override
    protected void onError(Context context, String s) {

    }

    @Override
    protected void onRegistered(Context context, String s) {

    }

    @Override
    protected void onUnregistered(Context context, String s) {

    }
}
