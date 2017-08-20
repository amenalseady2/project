package com.onemena.utils;

import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by lvhaiwei on 2016/7/7.
 */
public class MapAppUtil {

    public  void  openMapApp(Context context,String poiname,String lat,String lon) {
        //调起高德地图客户端
        Intent intent;
        try {
            intent = Intent.getIntent("androidamap://viewMap?sourceApplication=妈妈生活圈&poiname=" + poiname + "&lat=" + lat + "&lon=" + lon + "&dev=0");
            //androidamap://navi?sourceApplication=appname&poiname=fangheng&lat=36.547901&lon=104.258354&dev=1&style=2 //开启导航
            //androidamap://viewMap?sourceApplication=appname&poiname=abc&lat=36.2&lon=116.1&dev=0 直接定位到商家
            if (isInstallByread("com.autonavi.minimap")) {
                context.startActivity(intent); //启动调用

            } else {
                ToastUtil.showNormalShortToast("没有安装高德地图客户端");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
     boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
