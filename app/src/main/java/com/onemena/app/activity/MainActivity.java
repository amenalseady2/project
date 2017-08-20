package com.onemena.app.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.asha.nightowllib.NightOwl;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.onemena.app.NewsApplication;
import com.onemena.app.config.Config;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.app.fragment.AttentionFragment;
import com.onemena.app.fragment.HomeFragment;
import com.onemena.base.BaseActicity;
import com.onemena.base.BaseFragment;
import com.onemena.data.UserManager;
import com.onemena.data.eventbus.LoginBean;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.data.eventbus.NightMode;
import com.onemena.data.eventbus.POPWindow;
import com.onemena.data.eventbus.PointShow;
import com.onemena.data.eventbus.RefreshBean;
import com.onemena.data.eventbus.SettingBean;
import com.onemena.data.eventbus.UserLogin;
import com.onemena.http.Api;
import com.onemena.listener.JsonObjectListener;
import com.onemena.listener.OnActFragmentListener;
import com.onemena.lock.LockService;
import com.onemena.me.view.fragment.MeFragmentChange;
import com.onemena.service.MyFirebaseInstanceIDService;
import com.onemena.service.PublicService;
import com.onemena.utils.GetPhoneInfoUtil;
import com.onemena.utils.LogManager;
import com.onemena.utils.MD5;
import com.onemena.utils.MyLocationManager;
import com.onemena.utils.SpUtil;
import com.onemena.utils.TaskUtil;
import com.onemena.utils.ToastUtil;
import com.onemena.utils.TongJiUtil;
import com.onemena.utils.VersionStoreUpUtils;
import com.onemena.utils.shortcut.ShortcutSuperUtils;
import com.onemena.utils.shortcut.ShortcutUtils;
import com.onemena.video.view.fragment.VideoFragment;
import com.onemena.widght.FragmentTabHost;
import com.onemena.widght.HelveRomanTextView;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import io.fabric.sdk.android.Fabric;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.schedulers.Schedulers;

import static com.arabsada.news.R.drawable.btn_tab1_selector;
import static com.onemena.app.config.Point.isHavePoint;
import static com.onemena.data.UserManager.addUserInfo;



/**
 * fragment挂靠页
 */
public class MainActivity extends BaseActicity implements TabHost.OnTabChangeListener, OnActFragmentListener, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAB_1 = "TAB1";
    public static final String TAB_2 = "TAB2";
    public static final String TAB_3 = "TAB3";
    public static final String TAB_4 = "TAB4";
    private static final int RC_SIGN_IN = 9001;

    public Handler handler = new Handler();

    private FragmentTabHost tabHost;
    private long mExitTime;


    private String mCurrentTag = TAB_1;
    private View root_view;
    private View tabhost_view;
    private View main_view_cover;
    private ImageView mTabIcon1;
    private ImageView mTabIcon2;
    private ImageView mTabIcon3;
    private ImageView mTabIcon4;
    private TextView mTabtitle1;
    private TextView mTabtitle2;
    private TextView mTabtitle3;
    private TextView mTabtitle4;
    private TextView txt_point_3;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;
    private TwitterAuthClient twitterAuthClient;
    private TwitterSession twitterSession;
    private boolean isCold = true;
    private boolean isHot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // step1 before super.onCreate
        NightOwl.owlBeforeCreate(this);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Answers());
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        // step2 after setContentView
        NightOwl.owlAfterCreate(this);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        NewsApplication.getInstance().addActivity(this);
        root_view = findViewById(R.id.root_view_mainactivity);
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabhost_view = findViewById(R.id.tabhost_view);

        main_view_cover = findViewById(R.id.main_view_cover);

        initView();
        initThirdConfig();
        if (SpUtil.getBoolean(SPKey.LOCK_SWITCH, true)) {
            startService(new Intent(this, LockService.class));
        }

        MyLocationManager.getInstance().getLocation(this);

        Intent service_1 = new Intent(MainActivity.this, MyFirebaseInstanceIDService.class);
        startService(service_1);


//        //获取密钥散列（keyhash）值
//        try {
//            PackageInfo info = null;
//            try {
//                info = getPackageManager().getPackageInfo("com.arabsada.news", PackageManager.GET_SIGNATURES);
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String key = Base64.encode_s(md.digest());
//                System.out.println("key========" + key);
//                LogManager.i("key======================================================" + key);
//            }
//        } catch (NoSuchAlgorithmException e) {
//
//        }


        Boolean mode = SpUtil.getBoolean(SPKey.MODE, false);
        TabWidget tabWidget = tabHost.getTabWidget();
        if (mode) {
            tabhost_view.setBackgroundColor(Color.parseColor("#080808"));
        } else {
            tabhost_view.setBackgroundColor(Color.parseColor("#D6D6D6"));
        }

        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            if (mode) {
                tabWidget.getChildTabViewAt(i).setBackgroundColor(getResources().getColor(R.color.nightbg_1b1b1b));
                root_view.setBackgroundColor(getResources().getColor(R.color.nightbg_1b1b1b));
            } else {
                tabWidget.getChildTabViewAt(i).setBackgroundColor(Color.WHITE);
                root_view.setBackgroundColor(Color.WHITE);
            }
        }

        EventBus.getDefault().register(this);

        tabHost.getTabWidget().getChildTabViewAt(3).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mCurrentTag == TAB_1) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        EventBus.getDefault().post(new RefreshBean(0));
                    }
                }
                return false;
            }
        });

        tabHost.getTabWidget().getChildTabViewAt(2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mCurrentTag == TAB_2) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        EventBus.getDefault().post(new RefreshBean(1));
                    }
                }
                return false;
            }
        });
        getVersionInfo();

//        ShortcutUtils.removeShortcut(this, getShortCutIntent(), getString(R.string.app_name));
//        addShortcut();

        updateSplashAdv();
    }

    public void updateSplashAdv() {
        Api.getComApi().getSplashAdv()
                .subscribeOn(Schedulers.io())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .filter(advBean -> advBean.isSuccess())
                .map(advBean -> advBean.getContent())
                .filter(dataBean -> {
                    String url = dataBean.getAndroidPic();
                    if (StringUtils.isNotEmpty(url)) {
                        String dirPath = getCacheDir().getAbsolutePath() + File.separator + "adv_image";
                        File file = new File(dirPath, MD5.GetMD5Code(url));
                        return !file.exists();
                    } else {
                        SpUtil.saveValue(SPKey.SPLASH_ADV, "");
                        return false;
                    }
                })
                .flatMap(dataBean -> {
                    SpUtil.saveValue(SPKey.ADV_END_TIME, dataBean.getEndTime());
                    SpUtil.saveValue(SPKey.ADV_TYPE, dataBean.getType());
                    SpUtil.saveValue(SPKey.ADV_REDIRECT, dataBean.getRedirectTo());
                    String androidPic = dataBean.getAndroidPic();
                    Observable<String> stringObservable = Observable.just(androidPic);
                    Observable<ResponseBody> bodyObservable = Api.getComApi().downloadFile(androidPic);
                    return Observable.zip(stringObservable, bodyObservable, (url, responseBody) -> {
                        String dirPath = getCacheDir().getAbsolutePath() + File.separator + "adv_image";
                        File dir = new File(dirPath);
                        if (!dir.exists()) dir.mkdirs();
                        for (File file : dir.listFiles()) {
                            file.delete(); // 删除所有文件
                        }
                        File file = new File(dir, MD5.GetMD5Code(url));
                        SpUtil.saveValue(SPKey.SPLASH_ADV, file.toString());

                        FileOutputStream fileOuputStream = null;
                        try {
                            fileOuputStream = new FileOutputStream(file);
                            fileOuputStream.write(responseBody.bytes());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fileOuputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return url;
                    });
                })
                .observeOn(Schedulers.io())
                .subscribe(s -> {
                }, throwable ->
                throwable.printStackTrace());
    }

    private void addShortcut() {
        Observable.timer(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .compose(this.bindToLifecycle())
                .filter(aLong -> !SpUtil.getBoolean(SPKey.IS_SHORTCUT,false))
                .filter(aLong -> !ShortcutSuperUtils.isShortCutExist(this, getString(R.string.app_name), getShortCutIntent()))
                .observeOn(Schedulers.io())
                .subscribe(aLong -> {
                    ShortcutUtils.addShortcut(this, getShortCutIntent(), getString(R.string.app_name)
                            , false, R.mipmap.ic_launcher);
                    SpUtil.saveValue(SPKey.IS_SHORTCUT,true);
                },Throwable::printStackTrace);
    }

    private Intent getShortCutIntent() {
        // 使用MAIN，可以避免部分手机(比如华为、HTC部分机型)删除应用时无法删除快捷方式的问题
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setClass(this, SplashActivity.class);
        return intent;
    }


    public void getVersionInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("t", "" + System.currentTimeMillis());
        PublicService.getInstance().getJsonObjectRequest(ConfigUrls.UPDATE_URL, params, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
                try {
                    final Integer serverVersion = obj.getInteger("serverVersion_android");
                    final Integer forceVersion = obj.getInteger("forceVersion_android");
                    Integer imgReport = obj.getInteger("imgReport");
                    Integer speedDebug = obj.getInteger("speedDebug");
                    final String download_android = obj.getString("download_android");
                    if (speedDebug != null) {
                        Config.speedDebug = BooleanUtils.toBoolean(speedDebug);
                        SpUtil.saveValue(SPKey.EXAMINATION, BooleanUtils.toBoolean(speedDebug));
                    }

                    if (imgReport != null) {
                        Config.imgReport = BooleanUtils.toBoolean(imgReport);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                VersionStoreUpUtils.INSTANCE.launchAppDetail(root_view, MainActivity.this, serverVersion, forceVersion,download_android);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 200);

                } catch (Exception e) {
                }
            }

            @Override
            public void onObjError() {
            }
        });
    }


    /*
    初始化第三方登录配置
     */
    private void initThirdConfig() {
        ButterKnife.bind(this);
        twitterAuthClient = new TwitterAuthClient();
        callbackManager = CallbackManager.Factory.create();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .requestId()//B6:28:78:E1:FD:98:E8:F5:81:89:46:AB:D1:0A:0A:96:10:D1:86:9D  com.mysada.news
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(AppIndex.API).build();
    }

    private TabHost.TabSpec buildTagSpec(String tagName, int tabId, int drawableId, int textId) {

        View tabIndicator = LayoutInflater.from(this).inflate(
                tabId, null, false);

        ImageView tabIcon = (ImageView) tabIndicator.findViewById(R.id.tabicon);
        TextView txt_point = (TextView) tabIndicator.findViewById(R.id.txt_point);

        HelveRomanTextView tabtitle = (HelveRomanTextView) tabIndicator.findViewById(R.id.tabtitle);
        if (TAB_1.equals(tagName)) {
            mTabIcon1 = tabIcon;
            mTabtitle1 = tabtitle;

        } else if (TAB_2.equals(tagName)) {
            mTabIcon2 = tabIcon;
            mTabtitle2 = tabtitle;
        } else if (TAB_3.equals(tagName)) {
            mTabIcon3 = tabIcon;
            mTabtitle3 = tabtitle;
            txt_point_3 = txt_point;
        } else if (TAB_4.equals(tagName)) {
            mTabIcon4 = tabIcon;
            mTabtitle4 = tabtitle;
        }

        txt_point.setVisibility(View.GONE);
        tabIcon.setImageResource(drawableId);
        tabtitle.setText(textId);

        return tabHost.newTabSpec(tagName).setIndicator(tabIndicator);
    }

    private void initView() {

        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        String user = UserManager.getUserObj().getString("userinfo");
        if (StringUtils.isNotEmpty(user)) {
            String login_froms = JSONObject.parseObject(user).getString("login_from");
            if ("localhost".equals(login_froms)) {
                tabHost.addTab(this.buildTagSpec(TAB_3,
                        R.layout.item_view_tab_bottom, R.drawable.btn_tab3_notlogin_selector, R.string.tab_mine_notlogin), MeFragmentChange.class, null);
            } else {
                tabHost.addTab(this.buildTagSpec(TAB_3,
                        R.layout.item_view_tab_bottom, R.drawable.btn_tab3_selector, R.string.tab_mine), MeFragmentChange.class, null);
            }
        } else {
            EventBus.getDefault().post(new UserLogin(true));
            tabHost.addTab(this.buildTagSpec(TAB_3,
                    R.layout.item_view_tab_bottom, R.drawable.btn_tab3_notlogin_selector, R.string.tab_mine_notlogin), MeFragmentChange.class, null);
        }


        tabHost.addTab(this.buildTagSpec(TAB_4,
                R.layout.item_view_tab_bottom, R.drawable.btn_tab4_selector, R.string.tab_attention), AttentionFragment.class, null);
        tabHost.addTab(this.buildTagSpec(TAB_2,
                R.layout.item_view_tab_bottom, R.drawable.btn_tab2_selector, R.string.tab_video), VideoFragment.class, null);

        tabHost.addTab(this.buildTagSpec(TAB_1,
                R.layout.item_view_tab_bottom, R.drawable.btn_tab1_selector, R.string.tab_home), HomeFragment.class, null);

        tabHost.setCurrentTabByTag(TAB_1);
        tabHost.setOnTabChangedListener(this);
        checkMode();
    }


    @Override
    public void onTabChanged(String tag) {
        tabHost.setCurrentTabByTag(tag);
        if (tag != TAB_2 && mCurrentTag.equals(TAB_2)) {
//            ToastUtil.showNormalShortToast("从video切换到不是video");
            VideoFragment fragmentVideo = (VideoFragment) getSupportFragmentManager().findFragmentByTag(TAB_2);
            fragmentVideo.hidden();
        }
        if (tag == TAB_4) {
            AttentionFragment fragmentAttention = (AttentionFragment) getSupportFragmentManager().findFragmentByTag(TAB_4);
            if (fragmentAttention != null) {
                fragmentAttention.getMyAttentionList();
            }
        }
        if (tag == TAB_3) {
            MeFragmentChange fragmentAttention = (MeFragmentChange) getSupportFragmentManager().findFragmentByTag(TAB_3);
            if (fragmentAttention != null) {
                fragmentAttention.fragmentShow();
            }
        }
        mCurrentTag = tag;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());
        Branch branch = Branch.getInstance();
        branch.initSession(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                    // params will be empty if no data found
                    // ... insert custom logic here ...
                } else {
//                    Log.i("MyApp", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }


    private Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    public BaseFragment getActiveFragment() {
        BaseFragment mActiveFragment = null;
        FragmentManager manager = getSupportFragmentManager();
        int nCount = manager.getBackStackEntryCount();

        if (nCount > 0) {
            String tag = manager.getBackStackEntryAt(nCount - 1).getName();
            mActiveFragment = (BaseFragment) manager
                    .findFragmentByTag(tag);
        }

        return mActiveFragment;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // do nothing
            return true;
        }
        if (VersionStoreUpUtils.INSTANCE.popDismiss()) {
            return true;
        }
        BaseFragment activeFragment = getActiveFragment();
        if (activeFragment == null) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    ToastUtil.showNormalShortToast(getResources().getString(R.string.click_two_exit_app));
                    mExitTime = System.currentTimeMillis();

                } else {
                    //退出应用
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(startMain);
                    finish();
                    //Process.killProcess(Process.myPid());
                    System.exit(0);
                }
                return true;

            }
        } else {
            if (activeFragment != null && activeFragment instanceof BaseFragment) {
                boolean result = activeFragment.onKeyDown(keyCode, event);
                if (result) {
                    return true;
                }
            }
        }

        return super.onKeyDown(keyCode, event);
    }


    private void checkMode() {
        if (SpUtil.getBoolean(SPKey.MODE, false)) {
            nightMode();
        } else {
            dayMode();
        }
        checkPoint();
    }

    private void nightMode() {
        mTabtitle1.setTextColor(getResources().getColorStateList(R.color.text_color_selector_night));
        mTabtitle2.setTextColor(getResources().getColorStateList(R.color.text_color_selector_night));
        mTabtitle3.setTextColor(getResources().getColorStateList(R.color.text_color_selector_night));

        mTabtitle4.setTextColor(getResources().getColorStateList(R.color.text_color_selector_night));
        mTabIcon1.setImageResource(R.drawable.btn_tab1_selector_night);
        mTabIcon2.setImageResource(R.drawable.btn_tab2_selector_night);

        mTabIcon4.setImageResource(R.drawable.btn_tab4_selector_night);

        String user = UserManager.getUserObj().getString("userinfo");
        if (StringUtils.isNotEmpty(user)) {
            String login_froms = JSONObject.parseObject(user).getString("login_from");
            if ("localhost".equals(login_froms)) {
                mTabIcon3.setImageResource(R.drawable.btn_tab3_notlogin_selector_night);//tab_mine_notlogin
                mTabtitle3.setText(getResources().getString(R.string.tab_mine_notlogin));

            } else {
                mTabIcon3.setImageResource(R.drawable.btn_tab3_selector_night);
                mTabtitle3.setText(getResources().getString(R.string.tab_mine));
            }
        } else {
            EventBus.getDefault().post(new UserLogin(true));
            mTabIcon3.setImageResource(R.drawable.btn_tab3_notlogin_selector_night);
            mTabtitle3.setText(getResources().getString(R.string.tab_mine_notlogin));
        }


    }

    private void checkPoint() {
        if (isHavePoint()){//SpUtils.getBoolean("first_open",true)
            txt_point_3.setVisibility(View.VISIBLE);
        } else {
            txt_point_3.setVisibility(View.GONE);
        }
    }

    private void dayMode() {
        mTabtitle1.setTextColor(getResources().getColorStateList(R.color.text_color_selector));
        mTabtitle2.setTextColor(getResources().getColorStateList(R.color.text_color_selector));
        mTabtitle3.setTextColor(getResources().getColorStateList(R.color.text_color_selector));

        mTabtitle4.setTextColor(getResources().getColorStateList(R.color.text_color_selector));
        mTabIcon1.setImageResource(btn_tab1_selector);
        mTabIcon2.setImageResource(R.drawable.btn_tab2_selector);
        mTabIcon4.setImageResource(R.drawable.btn_tab4_selector);

        String user = UserManager.getUserObj().getString("userinfo");
        if (StringUtils.isNotEmpty(user)) {
            String login_froms = JSONObject.parseObject(user).getString("login_from");
            if ("localhost".equals(login_froms)) {
                mTabIcon3.setImageResource(R.drawable.btn_tab3_notlogin_selector);
                mTabtitle3.setText(getResources().getString(R.string.tab_mine_notlogin));
            } else {
                mTabIcon3.setImageResource(R.drawable.btn_tab3_selector);
                mTabtitle3.setTextColor(getResources().getColorStateList(R.color.text_color_selector));
                mTabtitle3.setText(getResources().getString(R.string.tab_mine));
            }
        } else {
            EventBus.getDefault().post(new UserLogin(true));
            mTabIcon3.setImageResource(R.drawable.btn_tab3_notlogin_selector);
            mTabtitle3.setText(getResources().getString(R.string.tab_mine_notlogin));
        }


    }

    public void onEventMainThread(NightMode mode) {
        boolean mode1 = mode.getMode();
        TabWidget tabWidget = tabHost.getTabWidget();
        if (mode1) {
            nightMode();
            tabhost_view.setBackgroundColor(Color.parseColor("#080808"));
        } else {
            dayMode();
            tabhost_view.setBackgroundColor(Color.parseColor("#D6D6D6"));
        }

        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            if (mode1) {
                tabWidget.getChildTabViewAt(i).setBackgroundColor(getResources().getColor(R.color.nightbg_1b1b1b));
                root_view.setBackgroundColor(getResources().getColor(R.color.nightbg_1b1b1b));
            } else {
                tabWidget.getChildTabViewAt(i).setBackgroundColor(Color.WHITE);
                root_view.setBackgroundColor(Color.WHITE);
            }
        }


    }

    public void onEventMainThread(LoginBean content) {
        if (SpUtil.getBoolean(SPKey.MODE, false)) {
            nightMode();
        } else {
            dayMode();
        }
    }


    //popwin显示的时候，背景出来
    public void onEventMainThread(POPWindow popWindow) {
        if (popWindow.isShowPOP) {
            main_view_cover.setVisibility(View.VISIBLE);
        } else {
            main_view_cover.setVisibility(View.GONE);
        }


    }

    //重新登录的代码
    public void onEventMainThread(UserLogin userLogin) {
        if (userLogin.reLogin) {
            UserManager.register(false);
        }

    }

    public void onEventMainThread(PointShow pointShow) {
//        txt_point_3.setVisibility(View.GONE);
        checkPoint();

    }

    public void onEventMainThread(SettingBean content) {
            changeTab(0);
    }

    public void changeTab(int num) {
        tabHost.getTabWidget().getChildTabViewAt(num).performClick();
    }

    private <T> Fragment findFragmentByTag(Class<T> clazz) {

        return getSupportFragmentManager().findFragmentByTag(clazz.getName());
    }


    public Fragment getFragment(String fragmentTag) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(fragmentTag);
        if (fragment == null) {
            fragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        }
        return fragment;
    }


    @Override
    public void onRefearch() {

    }

    @Override
    public void removeMsg() {

    }

    /**
     * 54      * 获得系统亮度
     * 55      *
     * 56      * @return
     * 57
     */
    public int getSystemBrightness() {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }

    /**
     * 69      * 改变App当前Window亮度
     * 70      *
     * 71      * @param brightness
     * 72
     */
    public void changeAppBrightness(int brightness) {
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent("sendbrocaster_tomytextview");
        intent.putExtra("activity_destroy", "activity_destroy");
        sendBroadcast(intent);

        //移除location位置监听
        MyLocationManager.getInstance().removeLocationListener();
        NewsApplication.getInstance().removeActivity(this);
        EventBus.getDefault().unregister(this);
        VersionStoreUpUtils.INSTANCE.popDestroy();

        AppEventsLogger.deactivateApp(this);//facebook，应用程序激活后调用应用事件记录器
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        EventBus.getDefault().post("logout_thirdfail");
    }


    public interface MyTouchListener {
        public void onTouchEvent(MotionEvent event);
    }

    // 保存MyTouchListener接口的列表
//    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<MainActivity.MyTouchListener>();
    private MyTouchListener myTouchListeners;

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void registerMyTouchListener(MyTouchListener listener) {
//        myTouchListeners.add(listener);
        myTouchListeners = listener;
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
//        myTouchListeners.remove(listener);
        myTouchListeners = null;
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        for (MyTouchListener listener : myTouchListeners) {
//            listener.onTouchEvent(ev);
//        }
        if (myTouchListeners != null) {
            myTouchListeners.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }


    //google 登录
    public void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    //Facebook 第三方登录
    public void signInFacebook() {

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(org.json.JSONObject object, GraphResponse response) {
                                Log.i("loginResult", JSONObject.toJSON(object).toString());
                                TongJiUtil.getInstance().putEntries(TJKey.UC_LOGIN_GG_RESULT, MyEntry.getIns(TJKey.TYPE, "1"));
                                String photourl = "";
                                try {
                                    if (object.optJSONObject("picture") != null) {
                                        photourl = object.optJSONObject("picture").optJSONObject("data").optString("url");
                                    }
                                } catch (Exception e) {
                                }
                                LoginThird(object.optString("email"), object.optString("first_name"), "facebook", photourl, object.optString("id"));
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,gender,birthday,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                EventBus.getDefault().post("login_error");
            }

            @Override
            public void onError(FacebookException error) {
                EventBus.getDefault().post("logout_thirdfail");
            }
        });
    }


    //推特第三方登录
    public void signInTwitter() {
        twitterAuthClient.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterSession = result.data;
                Twitter.getApiClient(twitterSession).getAccountService().verifyCredentials(true, false).enqueue(new Callback<User>() {
                    @Override
                    public void success(Result<User> result) {
                        User currentUser = result.data;
                        String name = currentUser.name;
                        String userName = currentUser.screenName;
                        String profilePicture = currentUser.profileImageUrl;
                        String email = currentUser.email;
                        TwitterSession twiiterSession = Twitter.getInstance().core.getSessionManager().getActiveSession();
                        String userId = String.valueOf(twiiterSession.getUserId());
                        TongJiUtil.getInstance().putEntries(TJKey.UC_LOGIN_GG_RESULT,
                                MyEntry.getIns(TJKey.TYPE, "0"));
                        LoginThird(email, userName, "twitter", profilePicture, userId);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        EventBus.getDefault().post("login_error");
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                EventBus.getDefault().post("login_error");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }


    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            TongJiUtil.getInstance().putEntries(TJKey.UC_LOGIN_GG_RESULT, MyEntry.getIns(TJKey.TYPE, "2"));
            LoginThird(acct.getEmail(), acct.getDisplayName(), "google", acct.getPhotoUrl() + "", acct.getId());
        } else {
            EventBus.getDefault().post("login_error");
        }
    }

    /*
    服务器三方登录
     */
    public void LoginThird(String email, String first_name, String lonin_from, String profile, String user_name) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", StringUtils.defaultString(email, ""));
        map.put("first_name", StringUtils.defaultString(first_name, ""));
        map.put("login_from", StringUtils.defaultString(lonin_from, ""));
        map.put("profile_photo", StringUtils.defaultString(profile, ""));
        map.put("user_name", StringUtils.defaultString(user_name, ""));
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.LOGIN_THIRD, map, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) throws Exception {
                String jsonString = obj.toJSONString();
                LoginBean loginBean = JSONObject.parseObject(jsonString, LoginBean.class);
                int code = obj.getInteger("code");
                if (code == 1) {
                    JSONObject content = obj.getJSONObject("content");
                    addUserInfo("userinfo", content.toJSONString());//存储用户信息
                    addUserInfo("User-Token", content.getString("user_token"));
                    SpUtil.saveValue(SPKey.LOGIN_USER_ID, content.getString("id"));
                    JSONObject extra = obj.getJSONObject("extra");
                    final String alert_message = extra.getString("alert_message");
                    if (StringUtils.isNotEmpty(alert_message)) {
                        TongJiUtil.getInstance().putEntries(TJKey.TASK_COM, MyEntry.getIns(TJKey.TASK_ID, extra.getString("task_id")));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                TaskUtil.showTaskFinish(MainActivity.this,alert_message);
                            }
                        }, 2000);
                    }
                    EventBus.getDefault().post(loginBean);
                } else {
                    EventBus.getDefault().post("login_error");
                }
            }

            @Override
            public void onObjError() {
                EventBus.getDefault().post("login_error");
            }
        });
    }


    public boolean isCold() {
        return isCold;
    }

    public void setCold(boolean cold) {
        isCold = cold;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }


    // 判断当前的应用程序是不是在运行
    //需要申请GETTask权限
    private boolean isApplicationBroughtToBackground() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public boolean wasBackground = false;    //声明一个布尔变量,记录当前的活动背景

    @Override
    public void onPause() {
        super.onPause();
        if (isApplicationBroughtToBackground())
            wasBackground = true;
    }

    public void onResume() {
        super.onResume();
        // step3 onResume
        NightOwl.owlResume(this);
        isHot = true;
        if (wasBackground) {//
            LogManager.i("aa", "从后台回到前台");
            String networkType = GetPhoneInfoUtil.INSTANCE.getNetworkType();
            String instance_time = SpUtil.getString(SPKey.INSTANCE_TIME);
            TongJiUtil.getInstance().putEntries(TJKey.PRE_START, MyEntry.getIns(TJKey.NETWORK, networkType),
                    MyEntry.getIns(TJKey.METHOD, "2"), MyEntry.getIns(TJKey.INSTANCE_TIME, instance_time),
                    MyEntry.getIns(TJKey.CREATE_TIME, SpUtil.getString(SPKey.CREATE_TIME)));
        }
        wasBackground = false;
    }
}
