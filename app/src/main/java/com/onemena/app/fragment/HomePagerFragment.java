package com.onemena.app.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.custom.indictor.CirclePageIndicator;
import com.onemena.app.activity.MainActivity;
import com.onemena.app.adapter.AutoViewPagerAdapter;
import com.onemena.app.config.Config;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.app.refresh.PullToRefreshLayout;
import com.onemena.app.refresh.PullableListView;
import com.onemena.base.BaseAdapter;
import com.onemena.base.BaseFragment;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.data.eventbus.NightMode;
import com.onemena.data.eventbus.POPWindow;
import com.onemena.data.eventbus.RefreshBean;
import com.onemena.home.model.javabean.NewsItemBean;
import com.onemena.home.view.adapter.NewsListAdapter;
import com.onemena.home.view.fragment.SpecialFragment;
import com.onemena.http.Api;
import com.onemena.listener.JsonObjectListener;
import com.onemena.listener.OnActionViewClickListener;
import com.onemena.service.PublicService;
import com.onemena.utils.GetPhoneInfoUtil;
import com.onemena.utils.LogManager;
import com.onemena.utils.NetworkUtil;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.utils.TongJiUtil;
import com.onemena.video.view.activity.VideoNewsDetailActivity;
import com.onemena.widght.AutoScrollViewPager;
import com.onemena.widght.PopwinDay;
import com.onemena.widght.PopwinNight;
import com.voler.saber.FragmentFactory;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Administrator on 2016/10/26.
 */
public class HomePagerFragment extends BaseFragment implements OnActionViewClickListener, PullToRefreshLayout.OnRefreshListener, BaseAdapter.OnViewClickListener {

    public static final String TITLEID = "titleId";
    public static final String NAME = "name";
    public static final String FLAG = "flag";

    private String titleId, titleId_Name;
    private AutoScrollViewPager auto_viewPager;
    private AutoViewPagerAdapter mPagerAdapter;
    private CirclePageIndicator pageIndicator;
    private PullableListView pullable_listview;
    private NewsListAdapter itemAdapter;
    private PullToRefreshLayout refreshLayout;
    private View lv_head;
    private RelativeLayout rl_unsuccess;
    private TextView auto_vp_news_title;
    private String flag;
    private boolean isHaveHeader;
    private boolean isRefresh = false;
    private boolean isListSuccess = false;
    private boolean isViewPrepared;
    private boolean isFirstData = true;
    private TextView tv_refresh;
    private LinearLayout ll_refresh;
    private ImageView img_not_data;
    private TextView txt_not_data;
    private List<NewsItemBean> topList;

    public static HomePagerFragment getInstance() {
        return new HomePagerFragment();
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homefragment_item, container, false);
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        //二级标签Id
        titleId = getArguments().getString(TITLEID);
        titleId_Name = getArguments().getString(NAME);
        flag = getArguments().getString(FLAG);
//        TongJiUtil.getInstance().putEntries("home_category_change",
//                MyEntry.getIns("category_id", titleId));
        //初始化下拉刷新LV
        pullable_listview = (PullableListView) view.findViewById(R.id.pullable_listview_home);
        tv_refresh = (TextView) view.findViewById(R.id.tv_refresh);
        ll_refresh = (LinearLayout) view.findViewById(R.id.ll_refresh);
        ll_refresh.setOnClickListener(this);
        itemAdapter = new NewsListAdapter(getContext());
        itemAdapter.setOnViewClickListener(this);//点击事件


        //初始化轮播图
        lv_head = View.inflate(getActivity(), R.layout.auto_viewpager, null);

        auto_viewPager = (AutoScrollViewPager) lv_head.findViewById(R.id.auto_viewpager);
        mPagerAdapter = new AutoViewPagerAdapter(getActivity());
        mPagerAdapter.setOnActionViewClickListener(this);
        auto_viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
        auto_viewPager.startAutoScroll();
        auto_viewPager.setCycle(true);

//        auto_vp宽高适配
        ViewGroup.LayoutParams layoutParams = auto_viewPager.getLayoutParams();
        layoutParams.height = Config.displayWidth * 180 / 375;
        auto_viewPager.setLayoutParams(layoutParams);

        auto_viewPager.setAdapter(mPagerAdapter);
        auto_vp_news_title = (TextView) lv_head.findViewById(R.id.auto_vp_news_title);


        pageIndicator = (CirclePageIndicator) lv_head
                .findViewById(R.id.indicator);
        pageIndicator.setViewPager(auto_viewPager);

        auto_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                JSONArray array = mPagerAdapter.getArray();
                JSONObject jsonObject = array.getJSONObject(position % array.size());
                auto_vp_news_title.setText(jsonObject.getString("title"));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pullable_listview.setAdapter(itemAdapter);

        refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refreashLayout);
        refreshLayout.setOnRefreshListener(this);

        rl_unsuccess = (RelativeLayout) view.findViewById(R.id.rl_unsuccess);
        img_not_data = (ImageView) view.findViewById(R.id.img_home_news_not_data);
        txt_not_data = (TextView) view.findViewById(R.id.txt_home_news_not_data);

        rl_unsuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pullRefresh();
//                showProgressDialog(getContext());
                rl_unsuccess.setVisibility(View.GONE);
//                if (StringUtils.isNotEmpty(flag)) {
//                    getListDataFromNet(0, false);
//                } else {
//                    getListDataFromNet(titleId, "0", 0, false);
//                }
            }
        });


        EventBus.getDefault().register(this);

        Boolean mode = SpUtil.getBoolean(SPKey.MODE, false);
        if (mode) {
            nightMode();
        } else {
            dayMode();
        }
        SpUtil.getDefaultEditor().putLong(SPKey.REFRESH_TIME + titleId, 0l).commit();//代表冷启动,热启动不走

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            long refresh_time = SpUtil.getDefault().getLong(SPKey.REFRESH_TIME + titleId, 0);
            if (System.currentTimeMillis() - refresh_time > 3600000) {
                pullRefresh(); // 在此请求数据
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        long refresh_time = SpUtil.getDefault().getLong(SPKey.REFRESH_TIME + titleId, 0);
        isViewPrepared = true;
        if (getUserVisibleHint()) {
            //network?冷热？
            if (NetworkUtil.checkNetWork(mContext)) {
                if (System.currentTimeMillis() - refresh_time > 3600000) {
                    if (pullable_listview.canPullDown()) {
                        pullRefresh();
                    } else {
                        //tan
                        tv_refresh.setText(R.string.find_new_contant);//dianji
                        showCue(tv_refresh, ll_refresh);
                    }
                }
            } else {//冷热？
                MainActivity mContext = (MainActivity) this.mContext;
                mContext.setHot(false);
            }
            if (mContext instanceof MainActivity) {
                ((MainActivity) mContext).setCold(false);
            }
        }
    }

    private void showCue(TextView tv_refresh, final LinearLayout linearLayout) {
        if (SpUtil.getBoolean(SPKey.MODE, false)) {
            //夜间模式
            linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.less_blue_night_96));
            tv_refresh.setTextColor(mContext.getResources().getColor(R.color.txt_234A7D));
        } else {
            //白天模式
            linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.less_blue_96));
            tv_refresh.setTextColor(mContext.getResources().getColor(R.color.main_bule));
        }
        if (linearLayout.getVisibility() != View.VISIBLE) {
            linearLayout.setVisibility(View.VISIBLE);
            final float dimension = getResources().getDimension(R.dimen.refresh_tip);
            ObjectAnimator.ofFloat(linearLayout, "Y", -dimension, 0, 0).setDuration(1300).start();
            linearLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator.ofFloat(linearLayout, "Y", 0, -dimension).setDuration(500).start();
                }
            }, 8500);
            linearLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    linearLayout.setVisibility(View.GONE);
                }
            }, 9000);
        }
    }

    private void pullRefresh() {
        if (getUserVisibleHint() && isViewPrepared) {
            pullable_listview.setSelectionAfterHeaderView();
            if (isFirstData) {
                if (StringUtils.isNotEmpty(flag)) {
                    getVPDataFromNet("0");
                } else {
                    getVPDataFromNet(titleId);
                }
            }
            SpUtil.getDefaultEditor().putLong(SPKey.REFRESH_TIME + titleId, System.currentTimeMillis()).commit();
            refreshLayout.refresh();
        }
    }

    private void getVPDataFromNet(String id) {
        PublicService.getInstance().postJsonObjectRequest(true, ConfigUrls.BANNER_LIST + id, null, new JsonObjectListener() {

            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) throws Exception {

                JSONArray content = obj.getJSONArray("content");

                if (content != null && content.size() > 0) {
                    mPagerAdapter.updateNotifyDataSetChanged(content);
                    auto_viewPager.setCurrentItem(content.size() * 200);
                    pageIndicator.setReadDataNum(content.size());
                    isHaveHeader = true;
                    if (isListSuccess && !isRefresh && pullable_listview.getHeaderViewsCount() < 1) {
                        pullable_listview.setAdapter(null);
                        pullable_listview.addHeaderView(lv_head);//呵呵
                        pullable_listview.setAdapter(itemAdapter);
                        itemAdapter.notifyDataSetChanged();
                    }
                } else {
                    isHaveHeader = false;
                    pullable_listview.removeHeaderView(lv_head);
                }
            }

            @Override
            public void onObjError() {
                pullable_listview.removeHeaderView(lv_head);//没有vp数据，去掉lv的头
                isHaveHeader = false;
            }
        });
    }

    //推荐 ,上拉和下拉区分不清晰
    private void getListDataFromNet(final int changeItemNum, final boolean isLoadMore) {

        int isNeedContent=0;
        if (SpUtil.getBoolean(SPKey.SHOW_CONTENT,true)) {
            isNeedContent=1;
        }
        PublicService.getInstance().getJsonObjectRequest(ConfigUrls.NEWS_LIST_WANG + GetPhoneInfoUtil.INSTANCE.getAndroidId()+"&isNeedContent="+isNeedContent
                , null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {

                try {
                    String top = obj.getString("tops");
                    String data = obj.getString("docs");

                    if (topList == null) {
                        if (StringUtils.isNotEmpty(top)) {
                            topList = JSONArray.parseArray(top, NewsItemBean.class);
                            String s = JSONObject.parseObject("", String.class);
                        } else {
                            topList = new ArrayList<>();
                        }
                        for (NewsItemBean newsItemBean : topList) {
                            newsItemBean.setTop("top");
                        }
                    }

//                    if (SpUtil.getBoolean(SPKey.EXAMINATION, false)) {
//                        topList.add(0, JSONObject.parseObject(Config.speciallist, NewsItemBean.class));
//                    }
                    List<NewsItemBean> dataList = JSONArray.parseArray(data, NewsItemBean.class);
                    if (dataList.size() > 0) {
                        if (!SpUtil.getBoolean(SPKey.FIRST_UPDATE, false)) {
                            TongJiUtil.getInstance().putEntries(TJKey.FIRST_UPDATE);
                            SpUtil.saveValue(SPKey.FIRST_UPDATE, true);
                        }
                        rl_unsuccess.setVisibility(View.INVISIBLE);
                        isListSuccess = true;
                        //将轮播图的view加到lv上
                        if (pullable_listview.getHeaderViewsCount() < 1 && isHaveHeader && !isRefresh) {
                            pullable_listview.setAdapter(null);
                            pullable_listview.addHeaderView(lv_head);//呵呵
                            pullable_listview.setAdapter(itemAdapter);
                            itemAdapter.notifyDataSetChanged();
                        }
                    } else {
                        if (changeItemNum == 0 && itemAdapter.getData().size() == 0) {
                            //下拉刷新的时候
                            rl_unsuccess.setVisibility(View.VISIBLE);
                            pullable_listview.removeHeaderView(lv_head);
                        } else if (changeItemNum != 0) {
                            //上拉加载更多的时候，没有数据了，不做任何动作
                            refreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                        }
                    }

                    itemAdapter.setCreateTimeVisibility(false);
                    if (isCacheData) {
                        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED, dataList.size(), isLoadMore);
                        itemAdapter.notifyDataSetChanged(changeItemNum, dataList, topList);
                    } else {
                        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED, dataList.size(), isLoadMore);
                        itemAdapter.notifyDataSetChanged(changeItemNum, dataList, topList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    refreshLayout.refreshFinish(PullToRefreshLayout.FAIL, 0, isLoadMore);
                }

            }

            @Override
            public void onObjError() {
                SpUtil.saveValue(SPKey.FIRST_UPDATE, true);
                isListSuccess = false;
                refreshLayout.refreshFinish(PullToRefreshLayout.FAIL, 0, isLoadMore);
                if (itemAdapter.getData().size() == 0) {
                    //下拉刷新的时候
                    rl_unsuccess.setVisibility(View.VISIBLE);
                    pullable_listview.removeHeaderView(lv_head);
                } else {
                    rl_unsuccess.setVisibility(View.GONE);
                }
            }
        });
    }


    private void getListDataFromNet(final String id, String size, final int changeItemNum, final boolean isLoadMore) {

        Api.getComApi().getListDataFromNet(id, size, String.valueOf(SpUtil.getBoolean(SPKey.SHOW_CONTENT, true)))
                .subscribeOn(Schedulers.io())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsItemBean -> {
                    rl_unsuccess.setVisibility(View.GONE);
                    List<NewsItemBean> itemBeanList = newsItemBean.getContent().getList();
                    isListSuccess = true;
                    refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED, itemBeanList.size(), isLoadMore);
                    itemAdapter.notifyDataSetChanged(changeItemNum, itemBeanList);
                }, throwable -> {
                    isListSuccess = false;
                    if (itemAdapter.getData().size() == 0) {
                        //下拉刷新的时候
                        rl_unsuccess.setVisibility(View.VISIBLE);
                    } else {
                        //上拉加载更多的时候，没有数据了，不做任何动作
                        rl_unsuccess.setVisibility(View.GONE);
                    }
                    refreshLayout.refreshFinish(PullToRefreshLayout.FAIL, 0, isLoadMore);
                });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        LogManager.i("-----", "dian");
        Log.e("-----", "dian");
        switch (v.getId()) {

            case R.id.ll_refresh:
                pullRefresh();
                ll_refresh.setVisibility(View.GONE);
                break;

            case R.id.simple_img:
                Bundle bundle2 = (Bundle) v.getTag();
                HomeNewsDetailFragment instance2 = HomeNewsDetailFragment.getInstance();
                bundle2.putString(HomeNewsDetailFragment.CATEGORY_ID, titleId);
                bundle2.putString(HomeNewsDetailFragment.NEWS_TYPE, "2");
                instance2.setArguments(bundle2);
                addToBackStack(instance2);
                break;
        }

    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

        if (StringUtils.isNotEmpty(flag)) {
            getListDataFromNet(-1, false);
        } else {
            getListDataFromNet(titleId, "0", -1, false);
        }
        if (isFirstData) {
            isFirstData = false;
            return;
        }
        pullable_listview.removeHeaderView(lv_head);
        isRefresh = true;
        TongJiUtil.getInstance().putEntries(TJKey.UPDATE,
                MyEntry.getIns(TJKey.CATEGORY_ID, titleId),
                MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE));
        TongJiUtil.getInstance().putStatistics(
                MyEntry.getIns(TJKey.ACTYPE, "downFresh"),
                MyEntry.getIns(TJKey.DOCTYPE, "news"),
                MyEntry.getIns(TJKey.CATGID, titleId));

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

        if (StringUtils.isNotEmpty(flag)) {
            getListDataFromNet(itemAdapter.getData().size(), true);
        } else {
            getListDataFromNet(titleId, itemAdapter.getData().size() + "", itemAdapter.getData().size(), true);
        }
        TongJiUtil.getInstance().putEntries(TJKey.LOAD_MORE,
                MyEntry.getIns(TJKey.CATEGORY_ID, titleId),
                MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE));
        TongJiUtil.getInstance().putStatistics(
                MyEntry.getIns(TJKey.ACTYPE, "upLoad"),
                MyEntry.getIns(TJKey.DOCTYPE, "news"),
                MyEntry.getIns(TJKey.CATGID, titleId));
    }

    public void onEventMainThread(RefreshBean refreshBean) {
        if (refreshBean.getFlag() != 0) return;
        if ((refreshLayout.getState() & (PullToRefreshLayout.REFRESHING)) == 0) {
            rl_unsuccess.setVisibility(View.GONE);
            pullRefresh();
        }
    }


    public void onEventMainThread(NightMode mode) {
        boolean mode1 = mode.getMode();
        if (mode1) {
            nightMode();
        } else {
            dayMode();
        }
        itemAdapter.notifyDataSetChanged(0, itemAdapter.getData());
    }

    private void nightMode() {
        pullable_listview.setDivider(new ColorDrawable(Color.parseColor("#464646")));
        rl_unsuccess.setBackgroundColor(getResources().getColor(R.color.nightbg_1b1b1b));
        img_not_data.setImageResource(R.mipmap.details_failed_to_load_img_night);
        txt_not_data.setTextColor(getResources().getColor(R.color.textcolor_707070));
    }

    /**
     * android:src="@mipmap/details_failed_to_load_img"
     * app:night_src="@mipmap/details_failed_to_load_img_night"
     */

    private void dayMode() {
        pullable_listview.setDivider(new ColorDrawable(Color.parseColor("#E4E7F0")));
        rl_unsuccess.setBackgroundColor(getResources().getColor(R.color.activity_bg_f5));
        img_not_data.setImageResource(R.mipmap.details_failed_to_load_img);
        txt_not_data.setTextColor(getResources().getColor(R.color.textcolor_a1a6bb));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void showPullDown(View view) {
        SpUtil.saveValue(SPKey.DOWNPUSH, true);
        View popview = View.inflate(mContext, R.layout.pulldowndialog, null);
        final PopupWindow pop = new PopupWindow(popview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setBackgroundDrawable(new ColorDrawable(0));
        ImageView im = (ImageView) popview.findViewById(R.id.pulldpwon);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });
        im.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        pop.dismiss();
                        break;
                }
                return true;
            }
        });
        ((AnimationDrawable) im.getBackground()).start();
        //获取popwindow焦点
        pop.setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        pop.setOutsideTouchable(true);
        pop.update();
        pop.showAtLocation(view, Gravity.CENTER, 0, 0);
        EventBus.getDefault().post(new POPWindow(true));
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                EventBus.getDefault().post(new POPWindow(false));
            }
        });
    }

    public void onEventMainThread(JSONObject object) {
        String category_id = object.getString("category_id");
        if (StringUtils.isNotEmpty(category_id) && category_id.equals(this.titleId)) {
            String position = object.getString("position");
            if (StringUtils.isNotEmpty(position)) {
                itemAdapter.getData().get(Integer.parseInt(position)).setComment_count(object.getString("comment_count"));
                itemAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onViewClick(View view, int position) {
        NewsItemBean newsItemBean = itemAdapter.getData().get(position);
        LogManager.i("-----", "ji");
        Log.e("-----", "ji");
        switch (view.getId()) {
            case R.id.style2:
                newsItemBean.setIs_read(true);
                itemAdapter.notifyDataSetChanged();
                Intent intent = new Intent(mContext, VideoNewsDetailActivity.class);
                intent.putExtra(VideoNewsDetailActivity.ID, newsItemBean.getId());
                intent.putExtra(VideoNewsDetailActivity.POSITION, position + "");
                intent.putExtra(VideoNewsDetailActivity.NEWS_TYPE, "1");
                intent.putExtra(VideoNewsDetailActivity.CATEGORY_ID, titleId);
                startActivity(intent);

                TongJiUtil.getInstance().putStatistics(
                        MyEntry.getIns(TJKey.ACTYPE, "click"),
                        MyEntry.getIns(TJKey.DOCTYPE, "video"),
                        MyEntry.getIns(TJKey.RECOM_ID, newsItemBean.getRecomId()),
                        MyEntry.getIns(TJKey.DOCID, newsItemBean.getId()));
                break;
            case R.id.styles:
                TongJiUtil.getInstance().putEntries(TJKey.EXAM_ENT);
                Bundle bundle = new Bundle();
                bundle.putString(SpecialFragment.URL, ConfigUrls.EXAMINATION);
                SpecialFragment specialFragment = new SpecialFragment();
                specialFragment.setArguments(bundle);
                addToBackStack(specialFragment);
                break;
            case R.id.style1:
            case R.id.style3:
            case R.id.style4:
                newsItemBean.setIs_read(true);
                itemAdapter.notifyDataSetChanged();
                String type = "1";
                if (StringUtils.isNotEmpty(itemAdapter.getItem(position).getTop())) {
                    type = "10";
                }

//                newsItemBean
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(newsItemBean.getContent())) {
                    PreloadNewsDetailFragment preloadNewsDetailFragment = FragmentFactory.createPreloadNewsDetailFragment(
                            newsItemBean.getId(),
                            titleId,
                            String.valueOf(position),
                            type,
                            newsItemBean.getContent(),
                            newsItemBean.getTitle());
                    addToBackStack(preloadNewsDetailFragment);
                }else {
                    HomeNewsDetailFragment instance_1 = HomeNewsDetailFragment.newInstance(
                            newsItemBean.getId(),
                            titleId,
                            String.valueOf(position),
                            type, "");
                    addToBackStack(instance_1);
                }

                TongJiUtil.getInstance().putStatistics(
                        MyEntry.getIns(TJKey.ACTYPE, "click"),
                        MyEntry.getIns(TJKey.DOCTYPE, "news"),
                        MyEntry.getIns(TJKey.RECOM_ID, newsItemBean.getRecomId()),
                        MyEntry.getIns(TJKey.DOCID, newsItemBean.getId()));
                break;
            case R.id.show_pop_iv:
            case R.id.show_pop_iv_1:
            case R.id.show_pop_iv_2:
            case R.id.show_pop_iv_3:
            case R.id.show_pop_iv_4:
                if (SpUtil.getBoolean(SPKey.MODE, false)) {
                    PopwinNight.getInstance(mContext, titleId).showNightPopwindow(view, itemAdapter, pullable_listview, titleId_Name, position, newsItemBean.getFirst_name(), newsItemBean.getId());
                } else {
                    PopwinDay.getInstance(mContext, titleId).showDayPopwindow(view, itemAdapter, pullable_listview, titleId_Name, position, newsItemBean.getFirst_name(), newsItemBean.getId());
                }
                break;

        }
    }
}
