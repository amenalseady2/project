package com.onemena.http;


import com.onemena.app.config.ConfigUrls;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * HttpService Created by voler on 2017/6/5.
 * 说明：
 */

public class HttpService {

    public static final String BASE_URL = ConfigUrls.HOST;

    public static final int TIME_OUT = 10;

    private static Retrofit retrofit;

    private static void getRetrofit() {
        Timber.plant(new Timber.DebugTree());
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(chain -> new HttpHandler().onRequest(chain))
                .addNetworkInterceptor(new RequestInterceptor(new HttpHandler()))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static <T> T create(Class<T> service) {
        if (retrofit == null) {
            getRetrofit();
        }
        return retrofit.create(service);
    }

}
