package com.onemena.app.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.jakewharton.rxbinding.view.RxView;
import com.onemena.app.NewsApplication;
import com.onemena.app.config.AppConfig;
import com.onemena.app.config.Config;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.Point;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.base.BaseActicity;
import com.onemena.common.activity.WebActivity;
import com.onemena.data.UserManager;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.listener.JsonArrayListener;
import com.onemena.listener.JsonObjectListener;
import com.onemena.service.PublicService;
import com.onemena.utils.GetPhoneInfoUtil;
import com.onemena.utils.GreenDaoUtils;
import com.onemena.utils.LogManager;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.utils.TimeUtils;
import com.onemena.utils.TongJiUtil;
import com.onemena.video.view.activity.VideoNewsDetailActivity;
import com.onemena.widght.CountdownTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.onemena.app.config.Point.getToday;


public class SplashActivity extends BaseActicity {

    private boolean isPush = true;
    private Subscription subscribe;
    private ImageView adv;
    private CountdownTextView countdown;
    private RelativeLayout rl_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        NewsApplication.getInstance().initSDK();
        String networkType = GetPhoneInfoUtil.INSTANCE.getNetworkType();
        String android_id = GetPhoneInfoUtil.INSTANCE.getAndroidId();
        SpUtil.saveValue(SPKey.USER_IMEI, android_id);
        UserManager.addUserInfo("imei", android_id);
        updateDate();
        UserManager.register(true);
        initConfig();
        String instance_time = SpUtil.getString(SPKey.INSTANCE_TIME);
        if ("".equals(instance_time)) {
            instance_time = String.valueOf(System.currentTimeMillis() / 1000);
            SpUtil.saveValue(SPKey.INSTANCE_TIME, instance_time);
        }
        TongJiUtil.getInstance().putEntries(TJKey.PRE_START, MyEntry.getIns(TJKey.NETWORK, networkType),
                MyEntry.getIns(TJKey.METHOD, "1"), MyEntry.getIns(TJKey.INSTANCE_TIME, instance_time),
                MyEntry.getIns(TJKey.CREATE_TIME, SpUtil.getString(SPKey.CREATE_TIME)));

        List list = GreenDaoUtils.getInstance().searchNews("-1");
        if (list != null && list.size() > 0){
            SpUtil.saveValue(SPKey.IS_DOWNLOAD,true);
        }else {
            SpUtil.saveValue(SPKey.IS_DOWNLOAD,false);
        }
        //获取视频二级标题
        getVideoSecondTitle();
        //获取新闻二级标题
        getNewsSecondTitle();
        //日服功能
//        getDataService();

//        LogManager.i("appLinkData", dataString + "-----------------------");

        if (!SpUtil.getBoolean(SPKey.NOTIC, false)) {
            putUserNotification();
            SpUtil.saveValue(SPKey.NOTIC, true);
        }

        //当首次打开时候埋下第一次的提醒
        putUserNotificationEveryDay();
        putUserNotificationNewEveryDay();

        doNext();
        if (getIntent().getBooleanExtra("Notification",false)) {
            int random_id = getIntent().getIntExtra("random_id", 10101);
            TongJiUtil.getInstance().putEntries(TJKey.RECALL,MyEntry.getIns(TJKey.ID, String.valueOf(random_id)));
        }

    }



    public void doNext() {
        Intent intent = getIntent();
        Uri uri = getIntent().getData();
        String id = intent.getStringExtra("id");
        String type = "0";
        String follow = "";
        String newsType = "3";
        if (uri != null) {
            newsType = "4";
            type = uri.getQueryParameter("type");
            id = uri.getQueryParameter("id");
            follow = uri.getQueryParameter("follow");
        }
        if ("0".equals(type) && StringUtils.isNotEmpty(id)) {
            goNewsDetailActivity(id,follow,newsType);
        } else if ("1".equals(type)) {
            goVideoDetailActivity(id,newsType);
        } else {
            initAdv();//继续下一步操作
        }
    }

    private void goNewsDetailActivity(String id, String follow, String newsType) {
        Intent detailIntent = new Intent(this, BrowerOpenNewsDetailActivity.class);
        detailIntent.putExtra("id", id);
        detailIntent.putExtra("follow", follow);
        detailIntent.putExtra("news_type", newsType);
        startActivity(detailIntent);
    }

    private void goVideoDetailActivity(String id, String newsType) {
        Intent videoIntent = new Intent(this, VideoNewsDetailActivity.class);
        videoIntent.putExtra("id", id);
        videoIntent.putExtra("news_type", newsType);
        startActivity(videoIntent);
    }

    private void initAdv() {
        //启动页广告
        countdown = (CountdownTextView) findViewById(R.id.tv_countdown);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        adv = (ImageView) findViewById(R.id.iv_adv);
        RxView.clicks(countdown)
                .throttleFirst(1, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    TongJiUtil.getInstance().putEntries(TJKey.AD_JUMP,MyEntry.getIns(TJKey.TYPE,"2"));
                    goMainActivity();
                });

        subscribe = Observable.timer(3300, TimeUnit.MILLISECONDS)
                .subscribe(l -> {
                    TongJiUtil.getInstance().putEntries(TJKey.AD_JUMP,MyEntry.getIns(TJKey.TYPE,"2"));
                    goMainActivity();
                });

        String path = SpUtil.getString(SPKey.SPLASH_ADV, "");
        Observable.just(path)
                .subscribeOn(Schedulers.io())
                .delay(400, TimeUnit.MILLISECONDS)
                .takeWhile(s -> {
                    if (StringUtils.isNotEmpty(s)) {
                        int endTime = SpUtil.getInt(SPKey.ADV_END_TIME);
                        if (endTime > System.currentTimeMillis() / 1000) {
                            return true;
                        } else {
                            goMainActivity();
                            return false;
                        }
                    } else {
                        goMainActivity();
                        return false;
                    }
                })
                .map(s -> BitmapFactory.decodeFile(s))
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())//使用RXlifecycle,使subscription和activity一起销毁
                .subscribe(bitmap -> {
                    adv.setImageBitmap(bitmap);
                    setClick();
                    countdown.setVisibility(View.VISIBLE);
                    countdown.start();
                });

    }

    private void setClick() {
        RxView.clicks(adv)
                .throttleFirst(1, TimeUnit.SECONDS)
                .map(aVoid -> SpUtil.getString(SPKey.ADV_REDIRECT))
                .filter(s -> StringUtils.isNotEmpty(s))
                .subscribe(s -> {
                    rl_root.setVisibility(View.GONE);

                    switch (SpUtil.getString(SPKey.ADV_TYPE)) {
                        case "article":
                            subscribe.unsubscribe();
                            TongJiUtil.getInstance().putEntries(TJKey.AD_JUMP,MyEntry.getIns(TJKey.TYPE,"0"));
                            goNewsDetailActivity(s, "", "7");
                            break;
                        case "video":
                            subscribe.unsubscribe();
                            TongJiUtil.getInstance().putEntries(TJKey.AD_JUMP,MyEntry.getIns(TJKey.TYPE,"0"));
                            goVideoDetailActivity(s, "7");
                            break;
                        case "ad":
                            subscribe.unsubscribe();
                            TongJiUtil.getInstance().putEntries(TJKey.AD_JUMP,MyEntry.getIns(TJKey.TYPE,"1"));
                            Intent intent = new Intent(this, WebActivity.class);
                            intent.putExtra("url", s);
                            startActivity(intent);
                            break;
                        case "ad_shop":
                            try {
                                String marketPkg = "com.android.vending";
                                Uri uri = null;
                                if (s != null)
                                    uri = Uri.parse(s);
                                Intent next = new Intent(Intent.ACTION_VIEW, uri);
                                next.setPackage(marketPkg);
                                next.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(next);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                });
    }

    private void goMainActivity() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
//      Observable.just(null)
//              .observeOn(AndroidSchedulers.mainThread())
//              .subscribe(o -> {
//                  LogManager.i("-----","999");
        MainActivity.startActivity(SplashActivity.this);
        finish();
//                  new MyLocationManager().getLocation(this);
//
//              });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPush = !isPush) {
            MainActivity.startActivity(this);
            finish();
        }
    }



    public void getVideoSecondTitle() {

        final long start_time = System.currentTimeMillis();

        PublicService.getInstance().postJsonArrayRequest(true, 0, ConfigUrls.VIDEO_TITLE_DU, null, new JsonArrayListener() {
            @Override
            public void onJsonArray(int skip, JSONArray array, Boolean isCacheData) {
                long end_time = System.currentTimeMillis();
                TongJiUtil.getInstance().putEntries(TJKey.FIRET_CHECK_NETWORK,
                        MyEntry.getIns(TJKey.TIME2, Long.toString(end_time - start_time)));
                for (int i = 0; i < array.size(); i++) {
                    SpUtil.saveValue(SPKey.VIDEO_TITLE, array.toJSONString());

                }
            }

            @Override
            public void onJsonArrayError() {

            }
        });
    }

    public void getNewsSecondTitle() {

        PublicService.getInstance().postJsonObjectRequest(true, ConfigUrls.NEWS_TITLE_DU, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) throws Exception {
                JSONArray content = obj.getJSONArray("content");
                for (int i = 0; i < content.size(); i++) {
                    SpUtil.saveValue(SPKey.TITLEARRAY, content.toJSONString());
                }
            }

            @Override
            public void onObjError() {
            }
        });
    }




    //com.android.USERNOTIFICATION_RECEIVER
    private void putUserNotification() {

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.android.USERNOTIFICATION_RECEIVER_ARABSADA");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long first = 1000 * 60 * 60 * 2;
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + first, pendingIntent);

    }
    //com.android.USERNOTIFICATION_RECEIVER
    private void putUserNotificationEveryDay() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(AppConfig.USERNOTIFICATION_EVERYDAY);
        broadcastIntent.putExtra("type", 0);//0每天提醒
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.RTC_WAKEUP, TimeUtils.nextDataTime(1,11,30), pendingIntent);
    }

    private void putUserNotificationNewEveryDay() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(AppConfig.USERNOTIFICATION_EVERYDAY);
        broadcastIntent.putExtra("type", 1);//0每天提醒
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.RTC_WAKEUP, TimeUtils.getDataTime() + 24 * 3600 * 1000, pendingIntent);
    }

    @SuppressWarnings("deprecation")
    private void initConfig() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        Config.displayWidth = display.getWidth();
        Config.displayHeight = display.getHeight();
        Config.density = dm.density;
        Config.densityDpi = dm.densityDpi;
        Config.heightPixels = dm.heightPixels;
        Config.widthPixels = dm.widthPixels;
        // 日志
        LogManager.i(" -->>Config.displayWidth =" + Config.displayWidth);
        LogManager.i(" -->>Config.displayHeight =" + Config.displayHeight);
        LogManager.i(" -->>Config.density =" + Config.density);

        LogManager.i(" -->>Config.densityDpi =" + Config.densityDpi);
        LogManager.i(" -->>Config.heightPixels =" + Config.heightPixels);
        LogManager.i(" -->>Config.widthPixels =" + Config.widthPixels);

    }

    private void updateDate() {
        int have_point = SpUtil.getInt(SPKey.HAVE_POINT);
        int today = getToday();
        if ((have_point & today) != today) {
            have_point = have_point & Point.CLEAR_DAY_FLAG | getToday();//更新日期

            //当天第一次打开，重置相关状态
            have_point= have_point|Point.GOLD_TASK_FLAG;
            have_point= have_point|Point.UPDATE_MARKET_FLAG;
        }
        SpUtil.saveValue(SPKey.HAVE_POINT, have_point);
    }

}
