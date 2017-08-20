package com.onemena.app.config;

import com.onemena.utils.SpUtil;

import java.util.Calendar;

/**
 * Point Created by voler on 2017/5/4.
 * 说明：int32位，留出后12位存日期，前8位存红点相关标识，中12位存红点显示标识
 */

public class Point {

    public static final int GOLD_MARKET_FLAG = 0x4000;
    public static final int NOTI_FLAG = 0x8000;
    public static final int GOLD_TASK_FLAG = 0x1000;
    public static final int VERSION_FLAG = 0x2000;
    public static final int seven = 0x80000;


    public static final int NO_POINT_FLAG = 0x0;
    public static final int ALL_POINT_FLAG = 0x00fff000;
    public static final int CLEAR_DAY_FLAG = 0xfffff000;


    public static final int UPDATE_MARKET_FLAG = 0x01000000;


    public static void removePointFlag(int flag) {
        int haveTaskPoint = SpUtil.getInt(SPKey.HAVE_POINT);
        haveTaskPoint = haveTaskPoint & ~flag;
        SpUtil.saveValue(SPKey.HAVE_POINT, haveTaskPoint);
    }

    public static void addPointFlag(int flag) {
        int have_point = SpUtil.getInt(SPKey.HAVE_POINT);
        have_point = have_point | flag;
        SpUtil.saveValue(SPKey.HAVE_POINT, have_point);
    }

    public static boolean checkPointFlag(int flag) {
        int have_point = SpUtil.getInt(SPKey.HAVE_POINT);
        return (have_point & flag) != Point.NO_POINT_FLAG;
    }

    public static int getToday() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isHavePoint() {
        int have_point = SpUtil.getInt(SPKey.HAVE_POINT);
        if ((have_point & ALL_POINT_FLAG) == 0) {//无红点
            return false;
        } else {//有红点
            return true;
        }
    }
}
