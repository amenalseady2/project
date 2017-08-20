package com.onemena.http;

/**
 * Api Created by voler on 2017/6/5.
 * 说明：
 */

public class Api {
    private static CommonService commonService;

    public static CommonService getComApi() {
        if (commonService == null) {
            commonService = HttpService.create(CommonService.class);
        }
        return commonService;
    }
}
