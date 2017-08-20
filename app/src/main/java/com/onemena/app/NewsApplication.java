package com.onemena.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.arabsada.news.BuildConfig;
import com.arabsada.news.CrashHandler;
import com.asha.nightowllib.NightOwl;
import com.crashlytics.android.answers.Answers;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.google.ads.conversiontracking.AdWordsConversionReporter;
import com.google.firebase.FirebaseApp;
import com.onemena.app.activity.MainActivity;
import com.onemena.app.config.Config;
import com.onemena.app.config.SPKey;
import com.onemena.service.ExampleNotificationOpenedHandler;
import com.onemena.service.ExampleNotificationReceivedHandler;
import com.onemena.utils.DayServiceUtil;
import com.onemena.utils.GetPhoneInfoUtil;
import com.onemena.utils.SpUtil;
import com.onemena.utils.TongJiUtil;
import com.onesignal.OneSignal;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;
import shanggame.news.greendao.DaoMaster;
import shanggame.news.greendao.DaoSession;

import static com.onemena.app.config.SPKey.FB_TOKEN;


/**
 * Created by 张玉水 on 16/10/30.
 */
public class NewsApplication extends Application {


    public static NewsApplication instance;
    private Handler handler;
    public SQLiteDatabase db;
    public DaoMaster daoMaster;
    public DaoSession daoSession;
    private boolean isInit = false;

    public static NewsApplication getInstance() {
        return instance;
    }


    private RequestQueue mRequestQueue;


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }

        return mRequestQueue;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("-----", String.valueOf(System.currentTimeMillis()));
        instance = this;
        if (!BuildConfig.IS_DEBUG)
            CrashHandler.create(this);
        handler = new Handler();
        OneSignal.startInit(this).inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .autoPromptLocation(true)
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .init();

        NightOwl.builder().defaultMode(SpUtil.getInt(SPKey.NIGHT_MODE)).create();

        if (StringUtils.isNotBlank(SpUtil.getString(FB_TOKEN))) {
            DayServiceUtil.sendFireBaseToken(SpUtil.getString(FB_TOKEN));
        }

        new Handler().postDelayed(() -> DayServiceUtil.sendOneSignalToken(), 10000);

        initSDK();
        Log.e("-----", String.valueOf(System.currentTimeMillis()));
    }


    public void initSDK() {
        if (isInit) return;
        FirebaseApp.initializeApp(this);
//        Fresco.initialize(this);
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this).setMaxCacheSize(30 * ByteConstants.MB).build();
        ImagePipelineConfig pipelineConfig = ImagePipelineConfig.newBuilder(this).setMainDiskCacheConfig(diskCacheConfig).build();
        Fresco.initialize(this, pipelineConfig);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(Config.TWITTER_KEY, Config.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new TweetComposer());
        TongJiUtil.init(this);
        //facebook 应用分析初始化
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
//        Branch.enableLogging();
        Branch.getAutoInstance(this);
        setupDatabase();
        // MySadaFirstOpen
        // Google Android first open conversion tracking snippet
        // Add this code to the onCreate() method of your application activity
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        LogManager.i("00000", "Refreshed token___: " + refreshedToken);

        AdWordsConversionReporter.reportWithConversionId(this.getApplicationContext(), "865029686", "AwNCCPLV2WwQtpy9nAM", "0.00", false);
        isInit = true;
    }

    private void setupDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = helper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public Handler getHandler() {
        return handler;
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 低内存情况下调用Java垃圾回收器
        System.gc();
    }

    public List<Activity> activities = new ArrayList<Activity>();

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public MainActivity getMainActivity() {
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i) instanceof MainActivity) {
                return (MainActivity) activities.get(i);
            }
        }
        return null;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();

        for (Activity activity : activities) {
            activity.finish();
        }

        System.exit(0);
    }

}
