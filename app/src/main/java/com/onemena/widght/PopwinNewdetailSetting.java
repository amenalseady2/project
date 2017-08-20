package com.onemena.widght;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.onemena.app.NewsApplication;
import com.arabsada.news.R;
import com.onemena.app.config.Config;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.data.eventbus.NightMode;
import com.onemena.data.eventbus.POPWindow;
import com.onemena.base.BaseFragment;
import com.onemena.app.fragment.HomeNewsDetailFragment;
import com.onemena.app.fragment.ReportFragment;
import com.onemena.listener.JsonObjectListener;
import com.onemena.service.PublicService;
import com.asha.nightowllib.NightOwl;
import com.nineoldandroids.animation.ObjectAnimator;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.utils.TongJiUtil;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


/**
 * Created by Administrator on 2016/11/24.
 */

public class PopwinNewdetailSetting extends PopupWindow implements View.OnClickListener, SwitchView.OnStateChangedListener {


    private Activity mContext;
    private String mId;
    private BaseFragment mFragment;
    private int likeState;
    private int likeNum;
    private int disLikeNum;
    private final View popView;
    private LinearLayout ll_like;
    private LinearLayout ll_dislike;
    private LinearLayout ll_save;
    private LinearLayout ll_report;
    private HelveRomanTextView tv_pop_biger;
    private HelveRomanTextView tv_pop_big;
    private HelveRomanTextView tv_pop_middle;
    private HelveRomanTextView tv_pop_small;
    private HelveRomanTextView tv_pop_style;
    private HelveRomanTextView tv_pop_switch;
    private View view_diver_2;
    private View view_diver_1;
    private View view_d_1;
    private View view_d_2;
    private View view_d_3;
    private HelveRomanTextView tv_dismiss;
    private ImageView iv_like;
    private ImageView iv_dislike;
    private ImageView iv_save;
    private ImageView iv_report;
    private ArrayList<TextView> TVLists;
    private WebView webView;
    private SwitchView switch_view;
    private TextView tv_dislike;
    private TextView tv_like;
    private HelveRomanTextView tv_save;
    private HelveRomanTextView tv_report;
    private Typeface tf_bond;
    private Typeface tf_ligit;
    private boolean isSaved;
    private ImageView dislike_image_jian;
    private ImageView like_image_add;
    private String category_id;
    private LinearLayout ll_textsize;
    private View view_diver_3;
    private ImageView like_image_jian;
    private ImageView dislike_image_add;
    private TextView helveBoldTextView;

    public PopwinNewdetailSetting(Activity context, String id, BaseFragment fragment, int likeState, int likeNum, int disLikeNum, boolean isSaved) {

        mContext = context;
        mId = id;
        mFragment = fragment;
        this.likeState = likeState;
        this.likeNum = likeNum;
        this.disLikeNum = disLikeNum;
        this.isSaved = isSaved;
        popView = LayoutInflater.from(mContext).inflate(R.layout.newsdetail_setting_pop, null);
        initPopWindow();
        setContentView(popView);
    }

    public PopwinNewdetailSetting(Activity context, String id, String category_id, BaseFragment fragment, int likeState, int likeNum, int disLikeNum, boolean isSaved) {

        mContext = context;
        mId = id;
        this.category_id = category_id;
        mFragment = fragment;
        this.likeState = likeState;
        this.likeNum = likeNum;
        this.disLikeNum = disLikeNum;
        this.isSaved = isSaved;
        popView = LayoutInflater.from(mContext).inflate(R.layout.newsdetail_setting_pop, null);

        initPopWindow();
        setContentView(popView);
    }



    /**
     * 初始化弹出的pop
     */
    private void initPopWindow() {
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        setBackgroundDrawable(new ColorDrawable(0));
        //设置popwindow出现和消失动画
        setAnimationStyle(R.style.PopMenuAnimation);


        //获取popwindow焦点
        setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        setOutsideTouchable(true);
        update();

        AssetManager mgr = mContext.getAssets();//得到AssetManager
        //根据路径得到Typeface
        tf_ligit = Typeface.createFromAsset(mgr, "fonts/HelveticaNeueLTArabic-Roman.ttf");
        tf_bond = Typeface.createFromAsset(mgr, "fonts/HelveticaNeueLTArabic-Bold.ttf");


        ll_like = (LinearLayout) popView.findViewById(R.id.ll_like);
        ll_dislike = (LinearLayout) popView.findViewById(R.id.ll_dislike);
        ll_save = (LinearLayout) popView.findViewById(R.id.ll_save);
        ll_report = (LinearLayout) popView.findViewById(R.id.ll_report);

        tv_pop_biger = (HelveRomanTextView) popView.findViewById(R.id.tv_pop_biger);
        tv_pop_big = (HelveRomanTextView) popView.findViewById(R.id.tv_pop_big);
        tv_pop_middle = (HelveRomanTextView) popView.findViewById(R.id.tv_pop_middle);
        tv_pop_small = (HelveRomanTextView) popView.findViewById(R.id.tv_pop_small);
        tv_pop_style = (HelveRomanTextView) popView.findViewById(R.id.tv_pop_style);
        view_diver_2 = popView.findViewById(R.id.view_diver_2);
        view_diver_1 = popView.findViewById(R.id.view_diver_1);
        view_diver_3 = popView.findViewById(R.id.view_diver_3);
        view_d_1 = popView.findViewById(R.id.view_d_1);
        view_d_2 = popView.findViewById(R.id.view_d_2);
        view_d_3 = popView.findViewById(R.id.view_d_3);
        tv_pop_switch = (HelveRomanTextView) popView.findViewById(R.id.tv_pop_switch);
        TVLists = new ArrayList<TextView>();//easy to manager textviews
        TVLists.add(tv_pop_biger);
        TVLists.add(tv_pop_big);
        TVLists.add(tv_pop_middle);
        TVLists.add(tv_pop_small);

        tv_dislike = (TextView) popView.findViewById(R.id.tv_dislike);
        tv_like = (TextView) popView.findViewById(R.id.tv_like);
        tv_save = (HelveRomanTextView) popView.findViewById(R.id.tv_save);
        tv_report = (HelveRomanTextView) popView.findViewById(R.id.tv_report);

        tv_dismiss = (HelveRomanTextView) popView.findViewById(R.id.tv_dismiss);
        switch_view = (SwitchView) popView.findViewById(R.id.switch_view);
        switch_view.toggleSwitch(true);
        switch_view.setOnStateChangedListener(this);//设置switch_view监听

        iv_like = (ImageView) popView.findViewById(R.id.iv_like);
        like_image_add = (ImageView) popView.findViewById(R.id.like_image_add);
        like_image_jian = (ImageView) popView.findViewById(R.id.like_image_jian);

        dislike_image_jian = (ImageView) popView.findViewById(R.id.dislike_image_jian);
        dislike_image_add = (ImageView) popView.findViewById(R.id.dislike_image_add);
        ll_textsize = (LinearLayout) popView.findViewById(R.id.ll_textsize);


        iv_dislike = (ImageView) popView.findViewById(R.id.iv_dislike);
        iv_save = (ImageView) popView.findViewById(R.id.iv_save);
        iv_report = (ImageView) popView.findViewById(R.id.iv_report);
        iv_like.setOnClickListener(this);
        iv_dislike.setOnClickListener(this);
        iv_save.setOnClickListener(this);
        iv_report.setOnClickListener(this);

        tv_dismiss.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tv_dismiss.setTypeface(tf_bond);//设置字体

        changeLikeView(likeState);

        chekMode();

    }

    private void chekMode() {

        checkSizeMode();
        saveChanged();
        changeLikeView(likeState);
        if (SpUtil.getBoolean(SPKey.MODE, false)) {
            //夜间模式
            popView.setBackgroundColor(Color.parseColor("#252525"));
            tv_dismiss.setBackgroundResource(R.drawable.home_item_click_night_selector);
            tv_dismiss.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
            ll_textsize.setBackgroundResource(R.drawable.newsdetail_setting_textsize_night_bg);
            view_diver_2.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
            view_diver_1.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
            view_diver_3.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
            view_d_1.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
            view_d_2.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
            view_d_3.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
            tv_pop_style.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
            tv_pop_switch.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
            iv_report.setBackgroundResource(R.mipmap.details_setting_report_night);
            tv_like.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
            tv_dislike.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
            tv_save.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
            tv_report.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
            switch_view.toggleSwitch(true);
            dislike_image_add.setImageResource(R.mipmap.details_reduce_);
            dislike_image_jian.setImageResource(R.mipmap.details_cacel);
            like_image_add.setImageResource(R.mipmap.details_add_night);
            like_image_jian.setImageResource(R.mipmap.details_cacel);

        } else {
            //白天模式
            popView.setBackgroundColor(mContext.getResources().getColor(R.color.color_f8f8f8));
            tv_dismiss.setBackgroundResource(R.drawable.home_item_click_selector);
            tv_dismiss.setTextColor(mContext.getResources().getColor(R.color.textcolor_64697b));
            ll_textsize.setBackgroundResource(R.drawable.newsdetail_setting_textsize_bg);
            view_diver_2.setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));
            view_diver_1.setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));
            view_diver_3.setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));
            view_d_1.setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));
            view_d_2.setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));
            view_d_3.setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));
            tv_pop_style.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            tv_pop_switch.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            iv_report.setBackgroundResource(R.mipmap.details_setting_report);
            tv_save.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            tv_report.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            switch_view.toggleSwitch(false);

            like_image_jian.setImageResource(R.mipmap.details_cace_night);
            like_image_add.setImageResource(R.mipmap.details_add);
            dislike_image_add.setImageResource(R.mipmap.details_reduce_add);
            dislike_image_jian.setImageResource(R.mipmap.details_cace_night);
        }
    }

    private void checkSizeMode() {
        int title_size = SpUtil.getInt(SPKey.TITLE_SIZE);
        switch (title_size){
            case 22:
                changeTVColor(tv_pop_small);
                break;
            case 24:
                changeTVColor(tv_pop_middle);
                break;
            case 27:
                changeTVColor(tv_pop_big);
                break;
            case 29:
                changeTVColor(tv_pop_biger);
                break;
        }
    }

    public void show(View parent, WebView webView, TextView helveBoldTextView) {
        this.webView = webView;
        this.helveBoldTextView = helveBoldTextView;
        //设置popwindow显示位置
        showAtLocation(parent, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        ll_dislike.setAlpha(0.1f);
        ll_like.setAlpha(0.1f);
        ll_report.setAlpha(0.1f);
        ll_save.setAlpha(0.1f);
        showViewAnim(400, ll_like);
        showViewAnim(460, ll_dislike);
        showViewAnim(520, ll_save);
        showViewAnim(580, ll_report);

        String webview_size = SpUtil.getString(SPKey.WEBVIEW_SIZE);
        switch (webview_size) {
            case "صغير":

                changeTVColor(tv_pop_small);
                break;
            case "طبيعي":

                changeTVColor(tv_pop_middle);
                break;
            case "كبير":

                changeTVColor(tv_pop_big);
                break;
            case "الأكبر":

                changeTVColor(tv_pop_biger);
                break;
            default:
                changeTVColor(tv_pop_middle);
                break;
        }
        tv_dismiss.setOnClickListener(this);

        tv_pop_small.setOnClickListener(this);
        tv_pop_middle.setOnClickListener(this);
        tv_pop_big.setOnClickListener(this);
        tv_pop_biger.setOnClickListener(this);

//        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
//        lp.alpha = 0.5f; //0.0-1.0
//        lp.gravity = Gravity.CENTER_HORIZONTAL;
//        mContext.getWindow().setAttributes(lp);
//        setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
//                lp.alpha = 1.0f;
//                mContext.getWindow().setAttributes(lp);
//            }
//        });

        EventBus.getDefault().post(new POPWindow(true));
        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                EventBus.getDefault().post(new POPWindow(false));
            }
        });


        changeLikeView(likeState);
    }

    //图标显示的动画
    public void showViewAnim(int timedelay, final View view) {
        NewsApplication.getInstance().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator.ofFloat(view, "TranslationY", 50, -30, 20, 0).setDuration(600).start();
                ObjectAnimator.ofFloat(view, "Alpha", 0.1f, 1f).setDuration(200).start();
            }
        }, timedelay);
    }

    private void changeTVColor(TextView tv_pop) {
        for (TextView tv : TVLists) {
            if (SpUtil.getBoolean(SPKey.MODE, false)) {
                if (tv.equals(tv_pop)) {
                    tv_pop.setTextColor(Color.parseColor("#234A7D"));
                    tv_pop.setTypeface(tf_bond);//设置字体
                } else {
                    tv.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
//                tv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    tv.setTypeface(tf_ligit);//设置字体
                }
            } else {
                if (tv.equals(tv_pop)) {
                    tv_pop.setTextColor(mContext.getResources().getColor(R.color.main_bule));
                    tv_pop.setTypeface(tf_bond);//设置字体
                } else {
                    tv.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
//                tv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    tv.setTypeface(tf_ligit);//设置字体
                }
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_like:

                ObjectAnimator.ofFloat(iv_like, "scaleX", 1.0f, 1.1f, 1.0f).setDuration(200).start();
                ObjectAnimator.ofFloat(iv_like, "scaleY", 1.0f, 1.1f, 1.0f).setDuration(200).start();
                if (likeState == 1) {
                    likeNum += 1;
                    sendToNet(2);
                    changeLikeView(2);
                    showMaopaoAnim(like_image_add);
                } else if (likeState == 2) {
                    //已经喜欢了，取消喜欢
                    likeNum -= 1;
                    sendToNet(1);
                    changeLikeView(1);
                    showMaopaoAnim(like_image_jian);
                } else if (likeState == 3) {
                    //已经不喜欢了，变为：喜欢加一，不喜欢减一
                    likeNum += 1;
                    disLikeNum-=1;
                    sendToNet(2);
                    changeLikeView(2);
                    showMaopaoAnim(like_image_add);
                }
                TongJiUtil.getInstance().putEntries(TJKey.CLICK_LICK,
                        MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE),
                        MyEntry.getIns(TJKey.RESOURCE_ID, mId));

                break;
            case R.id.iv_dislike:

                ObjectAnimator.ofFloat(iv_dislike, "scaleX", 1.0f, 1.1f, 1.0f).setDuration(200).start();
                ObjectAnimator.ofFloat(iv_dislike, "scaleY", 1.0f, 1.1f, 1.0f).setDuration(200).start();
                if (likeState == 1) {//从不知道-->不喜欢
                    disLikeNum += 1;
                    sendToNet(3);
                    changeLikeView(3);
                    showMaopaoAnim(dislike_image_add);
                    likeState = 3;
                } else if (likeState == 2) {
                    //已经喜欢了，取消喜欢，变为不喜欢。喜欢减一，不喜欢加一
                    likeNum -= 1;
                    disLikeNum+=1;
                    sendToNet(3);
                    changeLikeView(3);
                    likeState = 3;
                    showMaopaoAnim(dislike_image_add);
                } else if (likeState == 3) {
                    //已经不喜欢了，变为不知道
                    disLikeNum -= 1;
                    sendToNet(1);
                    changeLikeView(1);
                    likeState = 1;
                    showMaopaoAnim(dislike_image_jian);
                }
                TongJiUtil.getInstance().putEntries(TJKey.CLICK_DISLIKE,
                        MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE),
                        MyEntry.getIns(TJKey.RESOURCE_ID, mId));
                break;
            case R.id.iv_save:
                TongJiUtil.getInstance().putEntries(TJKey.SAVE,
                        MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE),
                        MyEntry.getIns(TJKey.RESOURCE_ID, mId));
                ObjectAnimator.ofFloat(iv_save, "scaleX", 1.0f, 1.1f, 1.0f).setDuration(200).start();
                ObjectAnimator.ofFloat(iv_save, "scaleY", 1.0f, 1.1f, 1.0f).setDuration(200).start();
                isSaved = !isSaved;
                ((HomeNewsDetailFragment)mFragment).saveOrNotSaveNews();
                saveChanged();
                likeState = 2;
                break;
            case R.id.iv_report:
//                TongJiUtil.getInstance().putEntries("news_report_setting",
//                        MyEntry.getIns("category_id", category_id),
//                        MyEntry.getIns("news_id", mId));
                ObjectAnimator.ofFloat(iv_report, "scaleX", 1.0f, 1.1f, 1.0f).setDuration(200).start();
                ObjectAnimator.ofFloat(iv_report, "scaleY", 1.0f, 1.1f, 1.0f).setDuration(200).start();
                ReportFragment fragment = ReportFragment.getInstance();
                Bundle bundle_s = new Bundle();
                bundle_s.putString(ReportFragment.ID, mId);
                bundle_s.putString(ReportFragment.CATEGORY_ID, category_id);
                bundle_s.putString(ReportFragment.TYPE, "2");
                fragment.setArguments(bundle_s);
                mFragment.addToBackStack(fragment);
                dismiss();
                break;
            case R.id.tv_dismiss:
                dismiss();
                break;

            case R.id.tv_pop_small:
//                TongJiUtil.getInstance().putEntries("news_font",
//                        MyEntry.getIns("category_id", category_id),
//                        MyEntry.getIns("news_id", mId),
//                        MyEntry.getIns("fontsize", "小1"));
                changeTVColor(tv_pop_small);
                webView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                helveBoldTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
                SpUtil.saveValue(SPKey.TITLE_SIZE, "صغير");
                SpUtil.saveValue( SPKey.TITLE_SIZE, 22);
                break;
            case R.id.tv_pop_middle:
//                TongJiUtil.getInstance().putEntries("news_font",
//                        MyEntry.getIns("category_id", category_id),
//                        MyEntry.getIns("news_id", mId),
//                        MyEntry.getIns("fontsize", "中2"));
                changeTVColor(tv_pop_middle);
                webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                helveBoldTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,24);
                SpUtil.saveValue( SPKey.WEBVIEW_SIZE, "طبيعي");
                SpUtil.saveValue( SPKey.TITLE_SIZE, 24);
                break;
            case R.id.tv_pop_big:
//                TongJiUtil.getInstance().putEntries("news_font",
//                        MyEntry.getIns("category_id", category_id),
//                        MyEntry.getIns("news_id", mId),
//                        MyEntry.getIns("fontsize", "大3"));
                changeTVColor(tv_pop_big);
                webView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
                helveBoldTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,27);
                SpUtil.saveValue( SPKey.WEBVIEW_SIZE, "كبير");
                SpUtil.saveValue( SPKey.TITLE_SIZE, 27);
                break;
            case R.id.tv_pop_biger:
//                TongJiUtil.getInstance().putEntries("news_font",
//                        MyEntry.getIns("category_id", category_id),
//                        MyEntry.getIns("news_id", mId),
//                        MyEntry.getIns("fontsize", "特大4"));
                changeTVColor(tv_pop_biger);
                webView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
                helveBoldTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,29);
                SpUtil.saveValue( SPKey.WEBVIEW_SIZE, "الأكبر");
                SpUtil.saveValue( SPKey.TITLE_SIZE, 29);
                break;
        }
    }

    //增加 减少 的动画
    private void showMaopaoAnim(final View v) {
        v.setVisibility(View.VISIBLE);
        Animation set = AnimationUtils.loadAnimation(mContext, R.anim.maopao_anim);
        v.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AlphaAnimation aa = new AlphaAnimation(1.0f, 0f);
                // 设定播放时间
                aa.setDuration(1000);
                // 设定重复次数，实际次数是重复次数加一。Animation.INFINITE为-1，表示无限次循环
                aa.setRepeatCount(0);
                // 设定重复模式.Animation.REVERS=2表示倒序重复 ，，，Animation.RESTART=1，表示从头开始
                aa.setRepeatMode(Animation.RESTART);
                // 开启动画
                v.startAnimation(aa);
                aa.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        v.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void saveChanged() {
        //是否收藏---回显
        if (isSaved) {
            if (SpUtil.getBoolean(SPKey.MODE, false)) {
                iv_save.setImageResource(R.mipmap.details_setting_saved_night);
            } else {
                iv_save.setImageResource(R.mipmap.save_yellow);
            }
        } else {
            if (SpUtil.getBoolean(SPKey.MODE, false)) {
                iv_save.setImageResource(R.mipmap.details_setting_save_night);
            } else {
                iv_save.setImageResource(R.mipmap.details_setting_save);
            }
        }
//        isSaved=!isSaved;
        NewsDetailSettingBusEvent events = new NewsDetailSettingBusEvent(likeState,likeNum,disLikeNum,isSaved);
        EventBus.getDefault().post(events);
        //Todo 访问网络，告诉服务器最新的状态


    }

    //点赞的写入到服务器
    private void sendToNet(int i) {
        switch (i) {
            case 2://点赞
                setLike();
                break;
            case 3://点不赞
                setDislike();
                break;
        }

        //将消息通知
        NewsDetailSettingBusEvent events = new NewsDetailSettingBusEvent(i, likeNum, disLikeNum,isSaved);
        EventBus.getDefault().post(events);
    }

    private void setDislike() {
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.NEWS_SET_DISLIKE  + mId, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
            }

            @Override
            public void onObjError() {

            }
        });
    }

    private void setLike() {
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.NEWS_SET_LIKE + mId, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
            }

            @Override
            public void onObjError() {

            }
        });
    }

    //改变点赞的界面
    private void changeLikeView(int like) {

        switch (like) {
            case 1://不知道喜不喜欢
                if (SpUtil.getBoolean(SPKey.MODE, false)) {
                    iv_like.setImageResource(R.mipmap.details_setting_like_night);
                    iv_dislike.setImageResource(R.mipmap.details_setting_dislike_night);
                } else {
                    iv_like.setImageResource(R.mipmap.details_setting_like);
                    iv_dislike.setImageResource(R.mipmap.details_setting_dislike);
                }
                tv_like.setTextColor(Color.parseColor("#a1a6bb"));
                tv_dislike.setTextColor(Color.parseColor("#a1a6bb"));
                break;

            case 2://喜欢
                if (SpUtil.getBoolean(SPKey.MODE, false)) {
                    //夜间模式
                    iv_like.setImageResource(R.mipmap.details_setting_liked_night);
                    iv_dislike.setImageResource(R.mipmap.details_setting_dislike_night);
                    tv_like.setTextColor(Color.parseColor("#234A7D"));//夜间模式，喜欢亮，不喜欢不亮
                    tv_dislike.setTextColor(Color.parseColor("#a1a6bb"));
                } else {
                    iv_like.setImageResource(R.mipmap.details_setting_liked);
                    iv_dislike.setImageResource(R.mipmap.details_setting_dislike);
                    tv_like.setTextColor(Color.parseColor("#3E84E0"));//白天模式，喜欢亮，不喜欢不亮
                    tv_dislike.setTextColor(Color.parseColor("#a1a6bb"));
                }
                break;

            case 3://不喜欢
                if (SpUtil.getBoolean(SPKey.MODE, false)) {
                    iv_like.setImageResource(R.mipmap.details_setting_like_night);
                    iv_dislike.setImageResource(R.mipmap.details_setting_disliked_night);
                    tv_like.setTextColor(Color.parseColor("#a1a6bb"));//喜欢不亮，不喜欢亮
                    tv_dislike.setTextColor(Color.parseColor("#935656"));

                } else {
                    iv_like.setImageResource(R.mipmap.details_setting_like);
                    iv_dislike.setImageResource(R.mipmap.details_setting_disliked);

                    tv_like.setTextColor(Color.parseColor("#a1a6bb"));
                    tv_dislike.setTextColor(Color.parseColor("#EB4E35"));
                }
                break;

        }
        tv_dislike.setText(StringUtils.int2IndiaNum(Integer.toString(disLikeNum)));
        tv_like.setText(StringUtils.int2IndiaNum(Integer.toString(likeNum)));
        likeState = like;
    }

    @Override
    public void toggleToOn(SwitchView view) {
        view.toggleSwitch(true);

        Message obtain = Message.obtain();
        obtain.obj = true;
        handler2.sendMessageDelayed(obtain, 100);
//        TongJiUtil.getInstance().putEntries("news_night",
//                MyEntry.getIns("category_id", category_id),
//                MyEntry.getIns("news_id", mId),
//                MyEntry.getIns("night_type", "night"));
    }

    @Override
    public void toggleToOff(SwitchView view) {
        view.toggleSwitch(false);

        Message obtain = Message.obtain();
        obtain.obj = false;
        handler2.sendMessageDelayed(obtain, 100);
//        TongJiUtil.getInstance().putEntries("news_night",
//                MyEntry.getIns("category_id", category_id),
//                MyEntry.getIns("news_id", mId),
//                MyEntry.getIns("night_type", "day"));
    }


    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            boolean flag = (boolean) msg.obj;

            NightOwl.owlNewDress(mContext);
            //要存一下
            SpUtil.saveValue(SPKey.NIGHT_MODE, NightOwl.owlCurrentMode());

            if (flag) {
//                NightStyleUtil.getmInstance().doNightStyle(mContext,false);
                //夜间模式
                SpUtil.saveValue(SPKey.MODE, true);
                EventBus.getDefault().post(new NightMode(true));
                chekMode();
//                int sBrightness = ((MainActivity)mContext).getSystemBrightness();
//                int sBrightness = ((MainActivity) mContext).getSystemBrightness();
//                SpUtils.saveValue(mContext,"brightness",sBrightness);
//                ((MainActivity)mContext).changeAppBrightness(50);

            } else {
//                NightStyleUtil.getmInstance().doNightStyle(mContext,true);
                SpUtil.saveValue(SPKey.MODE, false);
                EventBus.getDefault().post(new NightMode(false));
                chekMode();
//                int sBrightness = ((MainActivity)mContext).getSystemBrightness();
//                int brightness = SpUtils.getInt(mContext, "brightness");
//                ((MainActivity)mContext).changeAppBrightness(brightness==0?80:brightness);
            }
        }
    };
}
