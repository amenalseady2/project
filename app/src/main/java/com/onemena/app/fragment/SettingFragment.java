package com.onemena.app.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.config.SPKey;
import com.onemena.base.BaseFragment;
import com.onemena.data.UserManager;
import com.onemena.http.Api;
import com.onemena.me.view.fragment.WebFragment;
import com.onemena.utils.NetworkUtil;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.widght.HelveRomanTextView;
import com.onemena.widght.SwitchView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by WHF on 2016-11-30.
 */

public class SettingFragment extends BaseFragment {


    @BindView(R.id.back_iv_set)
    ImageView backIvSet;
    @BindView(R.id.view_feekback_set)
    RelativeLayout viewFeekbackSet;
    @BindView(R.id.txt_count_exit)
    HelveRomanTextView txtCountExit;
    @BindView(R.id.rl_exit)
    RelativeLayout rlExit;
    @BindView(R.id.versionname)
    TextView versionName;
    @BindView(R.id.testjson)
    RelativeLayout testjson;
    @BindView(R.id.switch_yu)
    SwitchView switchYu;
    @BindView(R.id.lay_yu)
    RelativeLayout layYu;
    private JSONObject userinfo;
    private String login_from;
    private String users;
    public SettingFragment() {
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (SpUtil.getBoolean(SPKey.MODE, false)) {
//            return View.inflate(getContext(), R.layout.fragment_setting_night, null);
//        } else {
//
//        }
        return View.inflate(getContext(), R.layout.fragment_setting, null);
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        users = UserManager.getUserObj().getString("userinfo");
        try {
            versionName.setText("V " + mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotEmpty(users)) {
            userinfo = JSONObject.parseObject(users);
            login_from = userinfo.getString("login_from");
            if ("localhost".equals(login_from)) {
                rlExit.setVisibility(View.GONE);
            } else {
                rlExit.setVisibility(View.VISIBLE);
            }
        }
        switchYu.toggleSwitch(SpUtil.getBoolean(SPKey.SHOW_CONTENT, true));
        switchYu.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                view.toggleSwitch(true);
                SpUtil.saveValue(SPKey.SHOW_CONTENT, true);
            }

            @Override
            public void toggleToOff(SwitchView view) {
                view.toggleSwitch(false);
                SpUtil.saveValue(SPKey.SHOW_CONTENT, false);
            }
        });
        checkIsShow();
    }

    private void checkIsShow() {
        Api.getComApi().getIsShowContent()
                .subscribeOn(Schedulers.io())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> {
                    try {
                        String s = responseBody.string();
                        JSONObject object = JSONObject.parseObject(s);
                        JSONObject content = object.getJSONObject("content");
                        Boolean is_show_content = content.getBoolean("is_show_content");
                        if (is_show_content) {
                            layYu.setVisibility(View.VISIBLE);
                        }else {
                            layYu.setVisibility(View.GONE);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },throwable -> {

                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.back_iv_set, R.id.view_feekback_set, R.id.rl_exit, R.id.testjson, R.id.view_yinsi_set})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv_set:
                popBackStack();
                break;
            case R.id.view_feekback_set:
                addToBackStack(new FeedBackFragment());
                break;
            case R.id.rl_exit:
                logOutMethod();
                break;
            case R.id.testjson:
                break;
            case R.id.view_yinsi_set:
                addToBackStack(new WebFragment());
                break;
        }
    }

    private void logOutMethod() {
        if (NetworkUtil.checkNetWork(mContext)) {
            UserManager.clearUser();
            UserManager.register(false);
            popBackStack();
            EventBus.getDefault().post("logout_success");
        }
    }
}
