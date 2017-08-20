package com.onemena.http;

import android.os.Build;

import com.arabsada.news.BuildConfig;
import com.onemena.data.UserManager;
import com.onemena.utils.GetPhoneInfoUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * HttpHandler Created by voler on 2017/6/5.
 * 说明：
 */

public class HttpHandler {
    Response onResponse(String httpResult, Interceptor.Chain chain, Response response) {
        return response;
    }

    okhttp3.Response onRequest(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .header("Server-Version", "2.0")
                .header("User-Token", StringUtils.defaultString(UserManager.getUserObj().getString("User-Token")))
                .header("App-Version", BuildConfig.VERSION_NAME)
                .header("Access-Token", GetPhoneInfoUtil.INSTANCE.getAndroidId())
                .header("User-Agent", "Android")
                .header("Unit-Type", Build.MODEL)
                .header("Sys-Version", Build.VERSION.RELEASE+"")
                .build();
        return chain.proceed(request);
    }
}
