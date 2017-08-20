package com.onemena.app.manager;

import android.app.Application;

import com.arabsada.news.BuildConfig;
import com.onemena.app.NewsApplication;


/**
 * UtilsManager Created by voler on 2017/5/2.
 * 说明：统一提供工具类所需的基础依赖，如：context
 */

public class UtilsManager {
    public static final boolean DEBUG_MESSSAGE= BuildConfig.IS_DEBUG;
    public static Application getApplication() {
        return NewsApplication.getInstance();
    }

}
