package com.onemena.me.view.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.asha.nightowllib.NightOwl;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onemena.app.activity.MainActivity;
import com.onemena.app.config.Config;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.Point;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.app.fragment.CollectionFragment;
import com.onemena.app.fragment.FeedBackFragment;
import com.onemena.app.fragment.SettingFragment;
import com.onemena.base.BaseFragment;
import com.onemena.cache.DataManager;
import com.onemena.data.UserManager;
import com.onemena.data.eventbus.LoginBean;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.data.eventbus.NightMode;
import com.onemena.data.eventbus.POPWindow;
import com.onemena.data.eventbus.UserLogin;
import com.onemena.listener.JsonObjectListener;
import com.onemena.lock.LockService;
import com.onemena.service.PublicService;
import com.onemena.utils.GreenDaoUtils;
import com.onemena.utils.LogManager;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.utils.TongJiUtil;
import com.onemena.widght.HelveRomanTextView;
import com.onemena.widght.MyDialog2;
import com.onemena.widght.PopwinFontSetting;
import com.onemena.widght.SwitchView;
import com.onemena.widght.waveview.WaveView;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.onemena.app.config.Point.checkPointFlag;
import static com.onemena.app.config.SPKey.LOCK_SWITCH;


/**
 * Created by Administrator on 2016/10/25.
 */
public class MeFragmentChange extends BaseFragment {
    @BindView(R.id.wave_view)
    WaveView waveView;
    @BindView(R.id.txt_login)
    HelveRomanTextView txtLogin;
    @BindView(R.id.img_user)
    SimpleDraweeView imgUser;
    @BindView(R.id.img_login_gl)
    ImageView imgLoginGl;
    @BindView(R.id.img_login_fb)
    ImageView imgLoginFb;
    @BindView(R.id.img_login_tw)
    ImageView imgLoginTw;
    @BindView(R.id.lay_setting)
    LinearLayout laySetting;
    @BindView(R.id.lay_dayornight)
    RelativeLayout layDayornight;
    @BindView(R.id.lay_save)
    LinearLayout laySave;
    @BindView(R.id.home_lay_bg)
    RelativeLayout homeLayBg;
    @BindView(R.id.txt_cachesize)
    TextView txtCachesize;
    @BindView(R.id.txt_count_3)
    HelveRomanTextView txtCount3;
    @BindView(R.id.img_count_3)
    ImageView imgCount3;
    @BindView(R.id.rl_clearcache)
    RelativeLayout rlClearcache;
    @BindView(R.id.txt_pdiv_3)
    TextView txtPdiv3;
    @BindView(R.id.text_font)
    HelveRomanTextView textFont;
    @BindView(R.id.txt_count_4)
    HelveRomanTextView txtCount4;
    @BindView(R.id.img_count_4)
    ImageView imgCount4;
    @BindView(R.id.view_font_style)
    RelativeLayout viewFontStyle;
    @BindView(R.id.txt_pdiv_4)
    TextView txtPdiv4;
    @BindView(R.id.img_count_5_1)
    ImageView imgCount51;
    @BindView(R.id.txt_count_5)
    HelveRomanTextView txtCount5;
    @BindView(R.id.img_count_5)
    ImageView imgCount5;
    @BindView(R.id.view_feekback)
    RelativeLayout viewFeekback;
    @BindView(R.id.txt_pdiv_5)
    TextView txtPdiv5;
    @BindView(R.id.home_lay)
    LinearLayout homeLay;
    @BindView(R.id.home_lay_s)
    ScrollView homeLayS;
    @BindView(R.id.lay_login_method)
    LinearLayout layLoginMethod;
    @BindView(R.id.lay_userbg)
    LinearLayout layUserbg;
    @BindView(R.id.tv_userbar)
    TextView tvUserbar;
    @BindView(R.id.lay_userbg_down)
    RelativeLayout layUserbgDown;
    @BindView(R.id.img_myset)
    ImageView imgMyset;
    @BindView(R.id.tv_myset)
    HelveRomanTextView tvMyset;
    @BindView(R.id.img_mynight)
    ImageView imgMynight;
    @BindView(R.id.tv_mynight)
    HelveRomanTextView tvMynight;
    @BindView(R.id.img_mysave)
    ImageView imgMysave;
    @BindView(R.id.tv_mysave)
    HelveRomanTextView tvMysave;
    @BindView(R.id.txt_s1)
    TextView txtS1;
    @BindView(R.id.txt_s2)
    TextView txtS2;
    @BindView(R.id.img_count_gold)
    ImageView imgCountGold;
    @BindView(R.id.txt_not_complete)
    HelveRomanTextView txtNotComplete;
    @BindView(R.id.txt_count_gold_task)
    HelveRomanTextView txtCountGoldTask;
    @BindView(R.id.txt_gold_task_point)
    TextView txtGoldTaskPoint;
    @BindView(R.id.img_count_gold_task)
    ImageView imgCountGoldTask;
    @BindView(R.id.rl_gold_task)
    RelativeLayout rlGoldTask;
    @BindView(R.id.txt_pdiv_gold)
    TextView txtPdivGold;
    @BindView(R.id.tv_gold_count)
    TextView tvGoldCount;
    @BindView(R.id.ll_task_gold)
    LinearLayout llTaskGold;
    @BindView(R.id.img_count_gold_market_ent)
    ImageView imgCountGoldMarketEnt;
    @BindView(R.id.txt_count_gold_market)
    HelveRomanTextView txtCountGoldMarket;
    @BindView(R.id.txt_gold_market_point)
    TextView txtGoldMarketPoint;
    @BindView(R.id.img_count_gold_market)
    ImageView imgCountGoldMarket;
    @BindView(R.id.rl_gold_market)
    RelativeLayout rlGoldMarket;
    @BindView(R.id.txt_pdiv_gold_market)
    TextView txtPdivGoldMarket;
    @BindView(R.id.switch_lock)
    SwitchView switchLock;
    @BindView(R.id.txt_count_7)
    TextView txtCount7;
    @BindView(R.id.img_count_7)
    ImageView imgCount7;
    @BindView(R.id.view_lock)
    AutoRelativeLayout viewLock;
    @BindView(R.id.txt_pdiv_7)
    TextView txtPdiv7;


    private JSONObject userinfo;
    private String profile_photo;
    private String user_name;
    private String login_from;
    private Handler handler = new Handler();


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_me_change, null);//الوضع الصباحي
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        init();
        checkMode();
        caculateCacheSize();
    }

    private void init() {
        String user = UserManager.getUserObj().getString("userinfo");
        if (StringUtils.isNotEmpty(user)) {
            userinfo = JSONObject.parseObject(user);
            profile_photo = userinfo.getString("profile_photo");
            user_name = userinfo.getString("user_name");
            login_from = userinfo.getString("login_from");
            if ("localhost".equals(login_from)) {
                user_name = "زائر" + user_name;
                layLoginMethod.setVisibility(View.VISIBLE);
                tvGoldCount.setText("5");
                txtNotComplete.setText("");
            } else {
                layLoginMethod.setVisibility(View.GONE);
                imgUser.setImageURI(Uri.parse(ConfigUrls.HOST_DU_IMG + profile_photo));
            }
        } else {
            EventBus.getDefault().post(new UserLogin(true));
            user_name = "زائر";
        }
        txtLogin.setText(user_name);
        textFont.setText(SpUtil.getString(SPKey.WEBVIEW_SIZE, "صغير"));

//        switchNotify.toggleSwitch(SpUtil.getBoolean(NOTIFY_SWITCH, true));
//        switchNotify.setOnStateChangedListener(this);
        switchLock.toggleSwitch(SpUtil.getBoolean(LOCK_SWITCH, true));
        switchLock.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                view.toggleSwitch(true);
                TongJiUtil.getInstance().putEntries(TJKey.LOCKREAD_SWITCH, MyEntry.getIns(TJKey.TYPE, "1"));
                SpUtil.saveValue(LOCK_SWITCH, true);
                mContext.startService(new Intent(mContext, LockService.class));
            }

            @Override
            public void toggleToOff(SwitchView view) {
                view.toggleSwitch(false);
                TongJiUtil.getInstance().putEntries(TJKey.LOCKREAD_SWITCH, MyEntry.getIns(TJKey.TYPE, "0"));
                SpUtil.saveValue(LOCK_SWITCH, false);
                mContext.stopService(new Intent(mContext, LockService.class));
            }
        });

        redPointShow();
        getUserInfo();
    }

    private void redPointShow() {
        if (checkPointFlag(Point.GOLD_TASK_FLAG)) {
            txtGoldTaskPoint.setVisibility(View.VISIBLE);
        } else {
            txtGoldTaskPoint.setVisibility(View.GONE);
        }

        if (checkPointFlag(Point.GOLD_MARKET_FLAG)) {
            txtGoldMarketPoint.setVisibility(View.VISIBLE);
        } else {
            txtGoldMarketPoint.setVisibility(View.GONE);
        }

        if (checkPointFlag(Point.NOTI_FLAG)) {
        } else {
        }
    }

    private void getUserInfo() {

        String user = UserManager.getUserObj().getString("userinfo");
        if (StringUtils.isEmpty(user)) return;
        userinfo = JSONObject.parseObject(user);
        login_from = userinfo.getString("login_from");
        if ("localhost".equals(login_from) || StringUtils.isEmpty(login_from)) return;
        PublicService.getInstance().getJsonObjectRequest(ConfigUrls.USER_INFO + SpUtil.getString(SPKey.LOGIN_USER_ID, ""), null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) throws Exception {
                JSONObject content = obj.getJSONObject("content");
                tvGoldCount.setText(content.getString("coin_count"));
                String un_coin_complete = content.getString("un_coin_complete");
                if (Integer.parseInt(un_coin_complete) > 0) {
                    txtNotComplete.setText(getString(R.string.gold_task_today_notcomplete) + " " + un_coin_complete);
                } else {
                    txtNotComplete.setText(getString(R.string.gold_task_today_complete));
                }

            }

            @Override
            public void onObjError() {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onBackStackChanged() {
        super.onBackStackChanged();
        textFont.setText(SpUtil.getString(SPKey.WEBVIEW_SIZE, "صغير"));
        init();
        checkMode();
    }


    @OnClick({R.id.img_login_gl, R.id.img_user, R.id.img_login_fb, R.id.img_login_tw,
            R.id.lay_setting, R.id.lay_dayornight, R.id.lay_save,
            R.id.rl_clearcache, R.id.view_font_style, R.id.view_feekback, R.id.rl_gold_task, R.id.ll_task_gold, R.id.rl_gold_market})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_login_gl:
                TongJiUtil.getInstance().putEntries(TJKey.UC_LOGIN,
                        MyEntry.getIns(TJKey.TYPE, "2"));
                ((MainActivity) mContext).signInGoogle();
                break;
            case R.id.img_login_fb:
                TongJiUtil.getInstance().putEntries(TJKey.UC_LOGIN,
                        MyEntry.getIns(TJKey.TYPE, "1"));
                ((MainActivity) mContext).signInFacebook();
                break;
            case R.id.img_login_tw:
                TongJiUtil.getInstance().putEntries(TJKey.UC_LOGIN,
                        MyEntry.getIns(TJKey.TYPE, "0"));
                ((MainActivity) mContext).signInTwitter();
                break;
            case R.id.lay_setting:
                addToBackStack(new SettingFragment());
                break;
            case R.id.lay_dayornight:
                setDayOrNight();
                NightOwl.owlNewDress(mContext);
                //要存一下
                SpUtil.saveValue(SPKey.NIGHT_MODE, NightOwl.owlCurrentMode());
                break;
            case R.id.lay_save:
//                testTime("http://www.baidu.com","百度");
//                testTime("http://now.net.mysada.com","mysada");
                addToBackStack(CollectionFragment.getInstance());
                break;
            case R.id.rl_clearcache:
                if (DataManager.getCacheSize(mContext) == 0)
                    return;

                showClearCachePop(view);
                TongJiUtil.getInstance().putEntries(TJKey.UC_CLEAR);
                break;
            case R.id.view_font_style:
                showFontSettingPop(view);
                break;
            case R.id.view_feekback:
                TongJiUtil.getInstance().putEntries(TJKey.UC_FEEDBACK);
                addToBackStack(new FeedBackFragment());
                break;
            case R.id.img_user:

                break;
            case R.id.ll_task_gold:
                TongJiUtil.getInstance().putEntries(TJKey.TASK_ENT, MyEntry.getIns(TJKey.TYPE, "1"));
                addGoldFragment();
                break;
            case R.id.rl_gold_task:

                TongJiUtil.getInstance().putEntries(TJKey.TASK_ENT, MyEntry.getIns(TJKey.TYPE, "2"));
                addGoldFragment();
                break;
            case R.id.rl_gold_market:

                TongJiUtil.getInstance().putEntries(TJKey.SHOP_ENT, MyEntry.getIns(TJKey.TYPE, "1"));
                addGoldMarketFragment();
                break;
        }
    }


    public void addGoldFragment() {
        GoldTaskFragment goldTaskFragment = GoldTaskFragment.newInstance(getString(R.string.task_list_title), ConfigUrls.GOLD_TASK);
        addToBackStack(goldTaskFragment);
    }

    private void addGoldMarketFragment() {
        NewGoldMarketFragment goldmarketfragment = NewGoldMarketFragment.newInstance(getString(R.string.金币商城), ConfigUrls.GOLD_MARKET);
        addToBackStack(goldmarketfragment);
    }

    private void testTime(final String url, final String tag) {
        final long starttime = System.currentTimeMillis();
        PublicService.getInstance().getJsonObjectRequest(url, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) throws Exception {
                long endtime = System.currentTimeMillis();
                LogManager.i(tag + "============", (endtime - starttime) + "");
            }

            @Override
            public void onObjError() {
                long endtime = System.currentTimeMillis();
                LogManager.i(tag + "============", (endtime - starttime) + "");
            }
        });
    }


    private void setDayOrNight() {
        Boolean mode = !SpUtil.getBoolean(SPKey.MODE, false);
        SpUtil.saveValue(SPKey.MODE, mode);
        EventBus.getDefault().post(new NightMode(mode));

    }


    public void onEventMainThread(LoginBean loginBean) {
        LoginBean.ContentBean content = loginBean.getContent();
        String login_from = content.getLogin_from();
        if (StringUtils.isNotEmpty(login_from)) {
            if ("localhost".equals(login_from)) {
                txtLogin.setText("زائر" + content.getUser_name());
                layLoginMethod.setVisibility(View.VISIBLE);
                tvGoldCount.setText("5");
                txtNotComplete.setText("");
                imgUser.setImageURI(Uri.parse("res://" + mContext.getPackageName() + "/" + R.mipmap.pc_profile));
            } else {
                txtLogin.setText(content.getUser_name());
                layLoginMethod.setVisibility(View.GONE);
                showLoginDialog(getResources().getString(R.string.login_success), R.mipmap.personal_center_cleared);
                imgUser.setImageURI(Uri.parse(ConfigUrls.HOST_DU_IMG + content.getProfile_photo()));
                getUserInfo();
            }
        }
        redPointShow();
    }

    public void onEventMainThread(String content) {
        if ("login_error".equals(content)) {
            showLoginDialog(getResources().getString(R.string.login_fail), R.mipmap.public_toast_fail);
        } else if ("logout_success".equals(content)) {
            showLoginDialog(getResources().getString(R.string.login_exitsuccess), R.mipmap.personal_center_cleared);
        } else if ("logout_thirdfail".equals(content)) {
            showLoginDialog(getResources().getString(R.string.logout_thirdfail), R.mipmap.public_toast_fail);
        }

    }

    /**
     * 计算cache大小
     */
    private void caculateCacheSize() {
        long cacheSize = DataManager.getCacheSize(mContext);
        if (cacheSize > 0) {
            String formatSize = DataManager.getFormatSize(mContext, cacheSize);
            txtCachesize.setText(formatSize);
        }
    }


    /**
     * 清除app缓存
     */
    public void myclearaAppCache() {

        Observable.just(null)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(r ->  showClearCacheSuccessDialog(false))
                .compose(this.bindToLifecycle())
                .observeOn(Schedulers.io())
                .subscribe(o -> {
                    GreenDaoUtils.getInstance().deleteAll("-1");
                    SpUtil.saveValue(SPKey.IS_DOWNLOAD, false);
                    DataManager.cleanCache(mContext);
                });
//        showClearCacheSuccessDialog(false);
    }

    //删除缓存的dialog
    private void showClearCachePop(View v) {
        View popview;
        if (SpUtil.getBoolean(SPKey.MODE, false)) {
            popview = View.inflate(getActivity().getApplicationContext(),
                    R.layout.clear_cache_pop_night, null);
        } else {
            popview = View.inflate(getActivity().getApplicationContext(),
                    R.layout.clear_cache_pop, null);
        }


        final PopupWindow pop = new PopupWindow(popview,
                Config.displayWidth - 80, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置pop背景，如果没有设置背景(这里设置的背景为空)，当点击其他位置的时候，pop不会自动消失
        pop.setBackgroundDrawable(new ColorDrawable(0));
        //获取popwindow焦点
        pop.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        pop.setOutsideTouchable(true);
        pop.update();
        HelveRomanTextView tv_cancel = (HelveRomanTextView) popview.findViewById(R.id.tv_cancel);
        HelveRomanTextView tv_commit = (HelveRomanTextView) popview.findViewById(R.id.tv_commit);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myclearaAppCache();
                pop.dismiss();
            }
        });
        //设置popwindow显示位置
        pop.showAtLocation(v, Gravity.CENTER, 0, 0);


        EventBus.getDefault().post(new POPWindow(true));
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                EventBus.getDefault().post(new POPWindow(false));
            }
        });
    }

    private void showClearCacheSuccessDialog(boolean onlyShowSuccessd) {


        final View view = View.inflate(getActivity(), R.layout.clear_cache_result_dialog, null);
        final MyDialog2 dialog = new MyDialog2(getActivity(), view, R.style.dialog);

        final HelveRomanTextView tv_msg = (HelveRomanTextView) view.findViewById(R.id.tv_msg);
        ImageView pb_loding = (ImageView) view.findViewById(R.id.pb_loding);
        if (onlyShowSuccessd) {
            tv_msg.setText("تم التنظيف بنجاح");
            pb_loding.setImageResource(R.mipmap.personal_center_cleared);
            Observable.timer(1500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .compose(this.bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        txtCachesize.setText("0KB");
                        dialog.dismiss();
                    });
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    txtCachesize.setText("0KB");
//                    caculateCacheSize();//刷新缓存的大小
//                    dialog.dismiss();
//                }
//            }, 1500);

        } else {
            tv_msg.setText("الأخبار المحفوظة");
            pb_loding.setImageResource(R.mipmap.personal_center_cleaning);
            RotateAnimation ra = new RotateAnimation(0, 360 * 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            ra.setDuration(1000);
            // 设定重复次数，实际次数是重复次数加一。Animation.INFINITE为-1，表示无限次循环
            ra.setRepeatCount(0);
            // 设定重复模式.Animation.REVERS=2表示倒序重复 ，，，Animation.RESTART=1，表示从头开始
            ra.setRepeatMode(Animation.RESTART);
            // 开启动画
            pb_loding.startAnimation(ra);
            ra.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    dialog.dismiss();
                    showClearCacheSuccessDialog(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        //设置显示动画
        if (onlyShowSuccessd) {
            dialog.getWindow().setWindowAnimations(R.style.clearcache_dismiss_animation);
        }

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    private void showLoginDialog(String notictxt, int imgreouse) {


        final View view = View.inflate(getActivity(), R.layout.clear_cache_result_dialog, null);
        final MyDialog2 dialog = new MyDialog2(getActivity(), view, R.style.dialog);

        final HelveRomanTextView tv_msg = (HelveRomanTextView) view.findViewById(R.id.tv_msg);
        ImageView pb_loding = (ImageView) view.findViewById(R.id.pb_loding);
        tv_msg.setText(notictxt);
        pb_loding.setImageResource(imgreouse);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1500);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private class myAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            DataManager.cleanInternalCache(getContext());
            DataManager.cleanExternalCache(getContext());
            return null;
        }
    }


    /**
     * 字体大小弹框
     */
    private void showFontSettingPop(View v) {
        PopwinFontSetting popwinFontSetting = new PopwinFontSetting(getActivity());
        popwinFontSetting.setFontSizeChangListener(new PopwinFontSetting.OnFontSizeChangListener() {
            @Override
            public void setChangListener(String style) {
                textFont.setText(style);
            }
        });
        popwinFontSetting.show(v);
    }

    public void fragmentShow() {
        getUserInfo();
        checkMode();
    }


    public void checkMode() {
        changeModeTextColor(R.color.textcolor_222328, R.color.textcolor_707070, txtCount3,
                txtCount4, txtCount5,txtCount7, tvMyset, tvMynight, tvMysave, txtCountGoldTask, txtCountGoldMarket);
        changeModeTextColor(R.color.textcolor_a1a6bb, R.color.textcolor_707070, txtCachesize, textFont, txtNotComplete);
        changeModeBackgroud(R.color.color_rice5_white, R.color.txt_464646, txtPdiv3, txtPdiv4, txtPdiv5,txtPdiv7, txtS1, txtS2, txtPdivGold, txtPdivGoldMarket);
        changeModeBackgroud(R.color.white, R.color.nightbg_252525, homeLay, tvUserbar, homeLayS, layUserbg, rlGoldMarket);
        changeModeBackgroud(R.color.color_f8f8f8, R.color.nightbg_1b1b1b, layUserbgDown);
        changeModeBackgroud(R.color.main_bule, R.color.txt_234A7D, homeLayBg, waveView);

        changeModeText(R.string.mycenter_night, R.string.mycenter_day, tvMynight);
        changeModeTextColor(R.color.dialog_white_bg, R.color.textcolor_707070, txtLogin);


        String user = UserManager.getUserObj().getString("userinfo");
        if (StringUtils.isNotEmpty(user)) {
            String login_froms = JSONObject.parseObject(user).getString("login_from");
            if ("localhost".equals(login_froms)) {
                changeModeImage(R.mipmap.pc_profile, R.mipmap.pc_profile_night, imgUser);
            }
        } else {
            changeModeImage(R.mipmap.pc_profile, R.mipmap.pc_profile_night, imgUser);
        }


    }

    public void onEventMainThread(NightMode mode) {
        checkMode();
    }

}
