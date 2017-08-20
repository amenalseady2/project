package com.onemena.app.fragment;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.config.AppConfig;
import com.onemena.app.config.Config;
import com.onemena.app.config.Point;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.app.refresh.PullToRefreshLayout;
import com.onemena.app.refresh.PullableListView;
import com.onemena.base.BaseAdapter;
import com.onemena.base.BaseFragment;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.data.eventbus.NightMode;
import com.onemena.home.model.javabean.NewsItemBean;
import com.onemena.home.view.adapter.NewsListAdapter;
import com.onemena.http.Api;
import com.onemena.util.ReportUtil;
import com.onemena.utils.FileUtils;
import com.onemena.utils.GreenDaoUtils;
import com.onemena.utils.NetworkUtil;
import com.onemena.utils.SpUtil;
import com.onemena.utils.TongJiUtil;
import com.onemena.video.view.activity.VideoNewsDetailActivity;
import com.onemena.widght.dialog.CancelFollowDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import shanggame.news.greendao.News;

import static com.arabsada.news.R.id.tv_refresh_fine_news;
import static com.onemena.app.fragment.HomeNewsDetailFragment.newInstance;

/**
 * Created by Administrator on 2017/7/24.
 * <p>
 * This function is really bad.Disposable code.
 */
public class FinePagerFragment extends BaseFragment implements PullToRefreshLayout.OnRefreshListener, BaseAdapter.OnViewClickListener, View.OnClickListener {

    public static final int MAX_COUNT = 200;

    private PullableListView pullable_listview;
    private NewsListAdapter itemAdapter;
    private PullToRefreshLayout refreshLayout;
    private TextView tvRefreshFineNews;
    private List<NewsItemBean> mList;
    private String titleId = AppConfig.FINE_NEWS_CATEGORY_ID;
    private String titleId_Name = AppConfig.FINE_NEWS_CATEGORY_NAME;
    private String dirPath = "asset:///localpicture/";
    private ImageView imgDivider;
    private ImageView ivFineNews;
    private TextView tvBoutique;
    private View dividerLine;
    private boolean isButtonRefresh;

    public static FinePagerFragment getInstance() {
        return new FinePagerFragment();
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fine_pager, container, false);
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        //二级标签Id
        //初始化下拉刷新LV
        EventBus.getDefault().register(this);
        imgDivider = (ImageView) view.findViewById(R.id.img_divider);
        ivFineNews = (ImageView) view.findViewById(R.id.iv_fine_news);
        tvBoutique = (TextView) view.findViewById(R.id.tv_boutique);
        dividerLine = view.findViewById(R.id.divider_line);
        pullable_listview = (PullableListView) view.findViewById(R.id.pullable_listview_home);

        itemAdapter = new NewsListAdapter(mContext);
        itemAdapter.setOnViewClickListener(this);//点击事件
        pullable_listview.setAdapter(itemAdapter);

        refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refreashLayout);
        refreshLayout.setOnRefreshListener(this);

        tvRefreshFineNews = (TextView) view.findViewById(tv_refresh_fine_news);
        tvRefreshFineNews.setOnClickListener(this);

        mList = new ArrayList<>();
        List<News> newsList = GreenDaoUtils.getInstance().searchNews("2");
        if (newsList == null || newsList.isEmpty()) {
            //load local
            JSONArray content = FileUtils.getLocalJsonArray(mContext, R.raw.article);

            mList = content.toJavaList(NewsItemBean.class);

            for (NewsItemBean newsItemBean : mList) {
                newsItemBean.setLocal(true);
                List<String> rect_thumb_meta = newsItemBean.getRect_thumb_meta();
                if (rect_thumb_meta != null) {
                    for (int i = 0; i < rect_thumb_meta.size(); i++) {
                        rect_thumb_meta.set(i, dirPath + rect_thumb_meta.get(i));
                    }
                }
            }
            itemAdapter.notifyDataSetChanged(0, mList);
        } else {
            //对象不统一，后期优化吧
            for (News news : newsList) {
                String newscontent = news.getNewscontent();
                try {
                    NewsItemBean newsItemBean = JSONObject.parseObject(newscontent, NewsItemBean.class);
                    mList.add(newsItemBean);
                } catch (Exception e) {
                    Log.e("fastjson", newscontent);
                }
            }
            itemAdapter.notifyDataSetChanged(0, mList);
        }

        int today = Point.getToday();
        if (SpUtil.getInt(SPKey.LAST_OPEN) != today) {
            SpUtil.saveValue(SPKey.LAST_OPEN, today);
            SpUtil.saveValue(SPKey.REFRESH_TIMES, 3);
        }

        Boolean mode = SpUtil.getBoolean(SPKey.MODE, false);
        if (mode) {
            nightMode();
        } else {
            dayMode();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void pullRefresh(boolean buttonRefresh) {
        if (getUserVisibleHint()) {
            pullable_listview.setSelectionAfterHeaderView();
            refreshLayout.refresh();
            isButtonRefresh = buttonRefresh;
        }
    }

    private void getListDataFromNet(boolean isLoadMore) {

        Api.getComApi().getFineNews()
                .subscribeOn(Schedulers.io())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fineNewsBean -> {
                    if (fineNewsBean.isSuccess()) {
                        List<NewsItemBean> content = fineNewsBean.getContent();
                        if (content == null || content.isEmpty()) {
                            refreshLayout.refreshFinish(PullToRefreshLayout.NOTMORE, 0, isLoadMore);
                            return;
                        }
                        for (NewsItemBean newsItemBean : content) {
                            GreenDaoUtils.getInstance().addNote(newsItemBean.getId(), JSONObject.toJSONString(newsItemBean), "2");
                        }
                        int times = SpUtil.getInt(SPKey.REFRESH_TIMES);
                        SpUtil.saveValue(SPKey.REFRESH_TIMES, times - 1);
                        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED, content.size(), isLoadMore);
                        if (isLoadMore) {
                            itemAdapter.notifyDataSetChanged(1, content);
                        } else {
                            itemAdapter.notifyDataSetChanged(-1, content);
                        }
                    } else if (fineNewsBean.getCode() == -3) {
                        refreshLayout.refreshFinish(PullToRefreshLayout.NOTMORE, 0, isLoadMore);
                        showToast(getString(R.string.no_refresh_times));
                    } else {
                        refreshLayout.refreshFinish(PullToRefreshLayout.FAIL, 0, isLoadMore);
                    }
                    if (isButtonRefresh) {
                        ReportUtil.load_button();
                        isButtonRefresh = false;
                    }

                }, throwable -> {
                    refreshLayout.refreshFinish(PullToRefreshLayout.FAIL, 0, isLoadMore);
                    if (isButtonRefresh) {
                        ReportUtil.load_button_over("2");
                        isButtonRefresh = false;
                    }
                });
    }

    private void showToast(String info) {
        Toast toast = Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT);
        View layout = toast.getView();
        TextView v = (TextView) layout.findViewById(android.R.id.message);

        AssetManager mgr = mContext.getAssets();//得到AssetManager
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/HelveticaNeueLTArabic-Roman.ttf");//根据路径得到Typeface
        v.setTypeface(tf);//设置字体
        v.setTextSize(16);

        toast.show();
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

        if (NetworkUtil.checkNetWork(mContext)) {
            boolean isHaveRefreshTimes = SpUtil.getInt(SPKey.REFRESH_TIMES) > 0;
            if (isHaveRefreshTimes) {
                if (mList.size() > MAX_COUNT) {
                    showClearNews();
                    refreshLayout.refreshFinish(PullToRefreshLayout.FAIL, 0, false);
                } else {
                    getListDataFromNet(false);
                }
            } else {
                showToast(getString(R.string.no_refresh_times));

                refreshLayout.refreshFinish(PullToRefreshLayout.FAIL, 0, false);
            }
        } else {
            showToast(getString(R.string.no_network));
            refreshLayout.refreshFinish(PullToRefreshLayout.FAIL, 0, false);
        }

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

        Observable.just("")
                .throttleFirst(4000, TimeUnit.MILLISECONDS)
                .subscribe(s -> {
                    if (NetworkUtil.checkNetWork(mContext)) {
                        boolean isHaveRefreshTimes = SpUtil.getInt(SPKey.REFRESH_TIMES) > 0;
                        if (isHaveRefreshTimes) {
                            if (mList.size() > MAX_COUNT) {
                                showClearNews();
                                refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED, 0, true);
                            } else {
                                getListDataFromNet(true);
                            }
                        } else {
                            showToast(getString(R.string.no_refresh_times));
                            refreshLayout.refreshFinish(PullToRefreshLayout.NOTMORE, 0, true);
                        }
                    } else {
                        showToast(getString(R.string.no_network));
                        refreshLayout.refreshFinish(PullToRefreshLayout.NOTMORE, 0, true);
                    }

                    TongJiUtil.getInstance().putEntries(TJKey.LOAD_MORE,
                            MyEntry.getIns(TJKey.CATEGORY_ID, titleId),
                            MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE));
                    TongJiUtil.getInstance().putStatistics(
                            MyEntry.getIns(TJKey.ACTYPE, "upLoad"),
                            MyEntry.getIns(TJKey.DOCTYPE, "news"),
                            MyEntry.getIns(TJKey.CATGID, titleId));
                });
    }


    public void onEventMainThread(NightMode mode) {
        boolean mode1 = mode.getMode();
        if (mode1) {
            nightMode();
        } else {
            dayMode();
        }
        itemAdapter.notifyDataSetChanged();
    }

    private void nightMode() {
        pullable_listview.setDivider(new ColorDrawable(Color.parseColor("#464646")));
        tvRefreshFineNews.setTextColor(getResources().getColor(R.color.gray_cacaca));
        tvRefreshFineNews.setBackgroundResource(R.drawable.shape_6dp_4d6380);
        imgDivider.setImageResource(R.drawable.img_divider_night);
        ivFineNews.setImageResource(R.drawable.icon_boutique_night);
        tvBoutique.setTextColor(getResources().getColor(R.color.nighttxt_707070));
        dividerLine.setBackgroundColor(getResources().getColor(R.color.txt_464646));
    }


    private void dayMode() {
        pullable_listview.setDivider(new ColorDrawable(Color.parseColor("#E4E7F0")));
        tvRefreshFineNews.setTextColor(getResources().getColor(R.color.white));
        tvRefreshFineNews.setBackgroundResource(R.drawable.shape_6dp_3e84e0);
        imgDivider.setImageResource(R.drawable.img_divider);
        ivFineNews.setImageResource(R.drawable.icon_boutique);
        tvBoutique.setTextColor(getResources().getColor(R.color.black_222222));
        dividerLine.setBackgroundColor(getResources().getColor(R.color.gray_e3e3e3));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewClick(View view, int position) {
        NewsItemBean newsItemBean = itemAdapter.getData().get(position);

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
            case R.id.style1:
            case R.id.style3:
            case R.id.style4:
                newsItemBean.setIs_read(true);
                itemAdapter.notifyDataSetChanged();

                if (newsItemBean.isLocal() || !NetworkUtil.checkNetWork(mContext)) {
                    LocalNewsDetailFragment instance = LocalNewsDetailFragment.getInstance();

                    Bundle bundle = new Bundle();
                    bundle.putString(LocalNewsDetailFragment.ARTICLEDETAIL, JSONObject.toJSONString(newsItemBean));
                    instance.setArguments(bundle);
                    addToBackStack(instance);
                } else {
                    HomeNewsDetailFragment instance = newInstance(
                            newsItemBean.getId(), titleId, String.valueOf(position), "1", "", true);
                    addToBackStack(instance);
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
                int headerCount = pullable_listview.getHeaderViewsCount();
                int firstVisiblePosition = pullable_listview.getFirstVisiblePosition();
                View itemView = pullable_listview.getChildAt(position + headerCount - firstVisiblePosition);
                deleteCell(itemView, newsItemBean);

                TongJiUtil.getInstance().putEntries(TJKey.REDUCE_SUBMIT,
                        MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE),
                        MyEntry.getIns(TJKey.CATEGORY_ID, titleId),
                        MyEntry.getIns(TJKey.RESOURCE_ID, newsItemBean.getId()));
                break;
        }
    }

    private void deleteCell(final View v, NewsItemBean newsItemBean) {
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                itemAdapter.getData().remove(newsItemBean);
                GreenDaoUtils.getInstance().deleteByKey(Long.parseLong(newsItemBean.getId()));
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        };

        Animation out = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_out);
        if (al != null) {
            out.setAnimationListener(al);
        }
        out.setDuration(600);
        if (v != null) {
            v.startAnimation(out);
        }
    }


    @Override
    public void onClick(View v) {
        if (NetworkUtil.checkNetWork(mContext)) {
            boolean isHaveRefreshTimes = SpUtil.getInt(SPKey.REFRESH_TIMES) > 0;
            if (isHaveRefreshTimes) {
                if (mList.size() > MAX_COUNT) {
                    showClearNews();
                } else {
                    pullRefresh(true);
                }
            } else {
                showToast(getString(R.string.no_refresh_times));
                ReportUtil.load_button_over("1");
            }
        } else {
            showToast(getString(R.string.no_network));
            ReportUtil.load_button_over("0");
        }
    }

    private void showClearNews() {
        CancelFollowDialog dialog = new CancelFollowDialog(mContext);
        dialog.setText(R.string.clear_read_article);
        dialog.setCancelListener(v -> {
            dialog.dismiss();
//            ReportUtil.cancel_attention("0");
        });
        dialog.setOnConfirmListener(v -> {
            dialog.dismiss();
            clearData();
        });
        dialog.show();
    }

    /**
     * Waiting to rewrite the database
     */
    private void clearData() {
        showProgressDialog(mContext);
        Observable.timer(50, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .compose(this.bindToLifecycle())
                .map(s -> {
                    List<NewsItemBean> list = new ArrayList<>();
                    List<NewsItemBean> readList = new ArrayList<>();
                    for (NewsItemBean newsItemBean : mList) {
                        if (newsItemBean.is_read()) {
                            readList.add(newsItemBean);
                        } else {
                            list.add(newsItemBean);
                        }
                    }

                    int size = list.size();
                    if (size <= 20) {
                        Collections.sort(readList, (lhs, rhs) -> {
                            int l = Integer.parseInt(lhs.getId());
                            int r = Integer.parseInt(rhs.getId());
                            return r - l;
                        });

                        int deleteCount = readList.size() - (20 - size);
                        readList.subList(0, deleteCount);

                        mList = list;
                        mList.addAll(readList);

                    } else {
                        mList = list;
                    }
                    GreenDaoUtils.getInstance().deleteAll("2");
                    for (NewsItemBean newsItemBean : mList) {
                        GreenDaoUtils.getInstance().addNote(newsItemBean.getId(), JSONObject.toJSONString(newsItemBean), "2");
                    }
                    return "";
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(s -> {
                    itemAdapter.notifyDataSetChanged(0, mList);
                    dismissProgressDialog();
                    return "";
                })
                .subscribe(s -> {
                    pullRefresh(isButtonRefresh);
                }, Throwable::printStackTrace);

    }
}
