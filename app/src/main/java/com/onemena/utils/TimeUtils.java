package com.onemena.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 张玉水 on 2016/7/12.
 */
public class TimeUtils {

    public static String ms2TimeString(long ms){

        long currentTimeMillis = System.currentTimeMillis();

        long offms = currentTimeMillis - ms;
        if (offms<0){
            return "刚刚";
        }else {
            if (offms<3600*1000){
                if (offms/1000/60==0){
                    return "刚刚";
                }
                return offms/1000/60+"分钟前";
            }else if(offms<3600L*1000*24){
                return offms/1000L/60/60+"小时前";
            }else if (offms>=3600L*1000*24 && offms<3600L*1000*24*2){
                return "昨天";
            }else if (offms>=3600L*1000*24*2 && offms< 3600L*1000*24*365 ){
                return offms/1000L/60/60/24+"天前";
            }else if (offms>=3600L*1000*24*365){
                return offms/1000L/60/60/24/365+"年前";
            }

        }
        return "Time illegal";
    }

    //将一个时间的字符串 2015-6-7 计算到现在的 X岁X个月
    public static String getAge(String birthdayTime){
        Date date1 = String2Date(birthdayTime);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = String2Date(sdf.format(new Date()));

        return getResult(date1,date2);

    }

    public static String getResult(Date birthDay,Date now){

        if (birthDay.after(now)){
            return "birthday illegal";
        }

        Calendar c = Calendar.getInstance();

        c.setTime(birthDay);
        int year1 = c.get(Calendar.YEAR);
        int month1 = c.get(Calendar.MONTH);

        c.setTime(now);
        int year2 = c.get(Calendar.YEAR);
        int month2 = c.get(Calendar.MONTH);

        int yearResult;
        int monthResult;
        String result="";
        if (month1>month2 && year2>year1){
            monthResult=12-(month1-month2);
            yearResult=year2-year1-1;
        }else {
            monthResult=month2-month1;
            yearResult=year2-year1;
        }
        if (yearResult<0){
            yearResult=0;
        }
        if (yearResult!=0&& monthResult!=0){
            result=yearResult+"岁"+monthResult+"个月";
        }else if (yearResult!=0&& monthResult==0){
            result=yearResult+"岁";
        }else if (yearResult==0&& monthResult!=0){
            result=monthResult+"个月";
        }else{
            result=monthResult+"个月";
        }
        return result;


    }

    public static String getBabyTuijianAge(String age) {
        if (TextUtils.isEmpty(age)){
            return "";
        }
        if (age.contains("岁")){

            String[] years = age.split("岁");
//            if (years.length==1){
//                //针对“2岁”的情况
//                return years[0];
//            }else {
//                //针对“2岁3个月”的情况
//                return Integer.parseInt(years[0])+1+"";
//            }
            return years[0];
        }else {
            //针对“几天”、“几个月”的情况
            return 1+"";
        }

    }


    // 比较两个日期相差多少天
    public static int daysOfTwo(Date fDate, Date oDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;
    }

    private String getDateHourMin(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = format.format(date);

        return time;
    }

    /**
     * 字符串转日期
     *
     * @param str
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private Date StrToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @author 张玉水
     * @date 2016-5-6 下午5:43:46 TODO 将字符串转成date
     * @param date
     * @return
     */
    public static Date String2Date(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("SimpleDateFormat")
    private Date StrToHour(String str) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = format.parse(str.trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }




    public static String showTime(String ctime) {

        String r = "";
        if(ctime==null)return r;

        long nowtimelong = System.currentTimeMillis();

        long ctimelong = Long.parseLong(ctime);
        long result = Math.abs(nowtimelong - ctimelong);

        if(result < 60000){// 一分钟内
            long seconds = result / 1000;
            if(seconds == 0){
                r = "刚刚";
            }else{
                r = seconds + "秒前";
            }
        }else if (result >= 60000 && result < 3600000){// 一小时内
            long seconds = result / 60000;
            r = seconds + "分钟前";
        }else if (result >= 3600000 && result < 86400000){// 一天内
            long seconds = result / 3600000;
            r = seconds + "小时前";
        }else if (result >= 86400000 && result < 1702967296){// 三十天内
            long seconds = result / 86400000;
            r = seconds + "天前";
        }else{
             String format="MM-dd HH:mm";
            SimpleDateFormat df = new SimpleDateFormat(format);
            r = df.format(ctime).toString();
        }
        return r;
    }

    /*
    * 将时间转换为时间戳
    */
    public static String dateToStamp(String s) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    public static long nextDataTime(int someday,int hour,int min){

        Date date = new Date();
        //通过日历获取下一天日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, +someday);
        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE),hour,min,0);
        return cal.getTime().getTime();
    }

    public static boolean nextDataOpen(){

        Date date = new Date();
        //通过日历获取下一天日期
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, +1);
        cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE),11,30,0);
        return true;
    }

    public static long getDataTime(){

        //通过日历获取下一天日期
        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis();
    }
}
