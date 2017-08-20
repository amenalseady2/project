package com.onemena.home.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.base.BaseFragment;
import com.onemena.listener.OnActionViewClickListener;
import com.onemena.service.PublicService;
import com.onemena.data.eventbus.DownloadBean;
import com.onemena.home.view.adapter.OfflineNewsAdapter;
import com.onemena.utils.GreenDaoUtils;
import com.onemena.utils.NetworkUtil;
import com.onemena.utils.SpUtil;
import com.onemena.utils.TongJiUtil;
import com.onemena.widght.HelveRomanTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by WHF on 2017-01-07.
 */

public class OffLineFragment extends BaseFragment implements OnActionViewClickListener {

    @BindView(R.id.txt_download)
    HelveRomanTextView txtDownload;
    @BindView(R.id.title_offline)
    HelveRomanTextView titleOffline;
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.lay_download_nodata)
    LinearLayout layDownloadNodata;
    @BindView(R.id.ll_offline_title)
    LinearLayout ll_offline_title;
    @BindView(R.id.lv_offline_news)
    ListView lvOfflineNews;
    @BindView(R.id.down_txt_btn_list)
    HelveRomanTextView downTxtBtnList;
    @BindView(R.id.down_cancel)
    HelveRomanTextView downCancel;
    @BindView(R.id.download_bottom_click)
    HelveRomanTextView downloadBottomClick;
    @BindView(R.id.progress_down)
    ProgressBar progressDown;


    private static OffLineFragment instance;
    OfflineNewsAdapter newsOfflineNewsAdapter;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        updateProgress(msg.arg1);
                        break;
                    case 1:
                        downloadFinish();
                        break;
            }
        }
    };

    public static OffLineFragment getInstance() {


        return new OffLineFragment();
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_offline, null);
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        newsOfflineNewsAdapter = new OfflineNewsAdapter(mContext);
        lvOfflineNews.setAdapter(newsOfflineNewsAdapter);
        newsOfflineNewsAdapter.setOnActionViewClickListener(this);
        getDownLoadData();
    }

    public void getDownLoadData() {
        if (!OffLineLoad.INSTANCE.isFinish()) {
            downTxtBtnList.setClickable(false);
            downCancel.setVisibility(View.VISIBLE);
//            downloadBottomClick.setVisibility(View.GONE);
            OffLineLoad.INSTANCE.setFragment(this);
            updateProgress(OffLineLoad.INSTANCE.progress);
            return;
        }
        List list = GreenDaoUtils.getInstance().searchNews("-1");
        if (list != null && list.size() > 0 && newsOfflineNewsAdapter != null) {
            newsOfflineNewsAdapter.notifyDataSetChanged(0, list);
            downTxtBtnList.setClickable(true);
            layDownloadNodata.setVisibility(View.GONE);
            downCancel.setVisibility(View.GONE);
//            downloadBottomClick.setVisibility(View.VISIBLE);
//            showCue(downloadBottomClick);

        } else if (getArguments() != null && newsOfflineNewsAdapter != null) {
            initSomeStage(list);
        }
    }


    public void updateProgress(int progress) {

        if (progress >= 100) {
            downTxtBtnList.setText("اضغط للتحميل");
            downTxtBtnList.setClickable(true);
            downCancel.setVisibility(View.GONE);
//            downloadBottomClick.setVisibility(View.VISIBLE);
        } else {
            downTxtBtnList.setText("يتم التحميل" + "% " + progress);
        }
        progressDown.setProgress(100 - progress);
    }


    /*
初始化相关下载状态
*/
    public void initSomeStage(List list) {
        newsOfflineNewsAdapter.notifyDataSetChanged(0, list);
        layDownloadNodata.setVisibility(View.VISIBLE);
        downTxtBtnList.setClickable(false);
        downCancel.setVisibility(View.VISIBLE);
//        downloadBottomClick.setVisibility(View.GONE);
        progressDown.setProgress(100);
        downTxtBtnList.setText("يتم التحميل" + "% 0");
        OffLineLoad.INSTANCE.init(this, getArguments().getStringArrayList("ids"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    public void downloadFinish() {
        List list = GreenDaoUtils.getInstance().searchNews("-1");
        if (list != null && list.size() > 0 && newsOfflineNewsAdapter != null) {
            newsOfflineNewsAdapter.notifyDataSetChanged(0, list);
            layDownloadNodata.setVisibility(View.GONE);
//            downloadBottomClick.setVisibility(View.VISIBLE);
//            showCue(downloadBottomClick);
        }else {
            layDownloadNodata.setVisibility(View.VISIBLE);
        }
        downTxtBtnList.setClickable(true);
        downCancel.setVisibility(View.GONE);
        downTxtBtnList.setText("اضغط للتحميل");
        progressDown.setProgress(100);
    }


    @OnClick({R.id.txt_download, R.id.back_iv, R.id.down_cancel,R.id.down_txt_btn_list, R.id.download_bottom_click})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.down_cancel:
                EventBus.getDefault().post(new DownloadBean(true));
                PublicService.getInstance().cancel();
                OffLineLoad.INSTANCE.setFinish(true);
                downloadFinish();
                TongJiUtil.getInstance().putEntries(TJKey.OL_DOWNLOAD_CANCEL);
                break;
            case R.id.down_txt_btn_list:
            case R.id.download_bottom_click:
                popBackStack();
                addToBackStack(NewOffLineFragment.getInstance());
                break;
            case R.id.back_iv:
                popBackAllStack();
                break;
            case R.id.item_lay_download:
                JSONObject object = (JSONObject) view.getTag();
                OfflineNewsDetailFragment instance = OfflineNewsDetailFragment.getInstance();
                Bundle bundle = new Bundle();
                bundle.putString(OfflineNewsDetailFragment.JSONSTRING, object.toJSONString());
                instance.setArguments(bundle);
                addToBackStack(instance);
                break;

        }
    }

    private void showCue(final TextView tv_refresh) {
        if (NetworkUtil.checkNetWork(mContext)) {
            if (SpUtil.getBoolean(SPKey.MODE, false)) {
                //夜间模式
                tv_refresh.setBackgroundColor(mContext.getResources().getColor(R.color.less_blue_night_96));
                tv_refresh.setTextColor(mContext.getResources().getColor(R.color.txt_234A7D));
            } else {
                //白天模式
                tv_refresh.setBackgroundColor(mContext.getResources().getColor(R.color.less_blue_96));
                tv_refresh.setTextColor(mContext.getResources().getColor(R.color.main_bule));
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //3秒完成动画
                    AnimationSet animationSet = new AnimationSet(true);
                    TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 300.0f);
                    animation.setDuration(2000);
                    animationSet.addAnimation(animation);
                    tv_refresh.startAnimation(animationSet);
                    tv_refresh.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tv_refresh.setVisibility(View.GONE);
                        }
                    },2000);
                }
            },10000);

        }else {
            tv_refresh.setVisibility(View.GONE);
        }
    }


}
