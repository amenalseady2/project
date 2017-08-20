package com.onemena.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.onemena.utils.DayServiceUtil;
import com.onemena.utils.SpUtil;

import org.apache.commons.lang3.StringUtils;

import static com.onemena.app.config.SPKey.FB_TOKEN;

/**
 * Created by WHF on 2016-11-21.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("00000", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);

    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server
        if (StringUtils.isNotBlank(token)) {
            Log.i("refreshedToken______",token);
            SpUtil.saveValue(FB_TOKEN,token);
            DayServiceUtil.sendFireBaseToken(token);
        }

    }


}
