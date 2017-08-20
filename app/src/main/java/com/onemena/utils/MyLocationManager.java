package com.onemena.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

/**
 * 创建者 Cruise WANG on 2016/7/20.
 */
public class MyLocationManager {
    public static Double LATITUDE = 0.0; //纬度
    public static Double LONGITUDE = 0.0; //获取经度

    private static MyLocationManager myLocationManager;
    private LocationManager locationManager;
    private String locationProvider;

    private MyLocationManager() {

    }

    public static MyLocationManager getInstance() {

        if (myLocationManager == null) {
            myLocationManager = new MyLocationManager();
        }
        return myLocationManager;
    }

    public void getLocation(Context context) {
        //获取地理位置管理器
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        getProvider();

        if (locationProvider == null) {
            //无法定位：1、提示用户打开定位服务；2、跳转到设置界面
            Intent i = new Intent();
            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(i);
        } else {
            locationManager.requestLocationUpdates(locationProvider, 5000, 1, locationListener);
            //获取Location
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location != null) {
                //不为空,显示地理位置经纬度
                setLocation(location);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locationListener);
            }
            //监视地理位置变化
            locationManager.requestLocationUpdates(locationProvider, 5000, 1, locationListener);
        }
    }

    private void getProvider() {
        // 构建位置查询条件
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);  //高精度
        criteria.setAltitudeRequired(false);  //不查询海拔
        criteria.setBearingRequired(false);  //不查询方位
        criteria.setCostAllowed(true);  //不允许付费
        criteria.setPowerRequirement(Criteria.POWER_LOW);  //低耗
        // 返回最合适的符合条件的 provider ，第 2 个参数为 true 说明 , 如果只有一个 provider 是有效的 , 则返回当前  provider
        locationProvider = locationManager.getBestProvider(criteria, true);
    }

    public void removeLocationListener() {
        if (locationManager != null) {
            //移除监听器
            locationManager.removeUpdates(locationListener);
        }
    }

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
            LogManager.i("onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            LogManager.i("onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            LogManager.i("onProviderDisabled");
        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
            setLocation(location);
//            double lat = location.getLatitude();
//            double lng = location.getLongitude();
//            String latLongString = "纬度:" + lat + "\n经度:" + lng;
//           ToastUtil.showNormalShortToast(latLongString);

        }
    };

    private static void setLocation(Location location) {
        if (location != null) {
            LATITUDE = location.getLatitude();
            LONGITUDE = location.getLongitude();
            LogManager.i(LATITUDE + "~~~~" + LONGITUDE);
        }
    }
}
