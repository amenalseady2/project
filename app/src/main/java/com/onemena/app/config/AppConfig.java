package com.onemena.app.config;


import com.arabsada.news.BuildConfig;

/**
 *
 */

public interface AppConfig {
    String VERSION_NAME = BuildConfig.VERSION_NAME;

    String USERNOTIFICATION_EVERYDAY = BuildConfig.APPLICATION_ID + ".USERNOTIFICATION_EVERYDAY";

    String FINE_NEWS_CATEGORY_ID = "34";
    String FINE_NEWS_CATEGORY_NAME = "مقالات مميزة";
}
