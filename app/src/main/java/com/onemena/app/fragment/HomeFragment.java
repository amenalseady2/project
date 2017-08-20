package com.onemena.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.custom.indictor.TabIndictorView;
import com.onemena.app.config.AppConfig;
import com.onemena.app.config.Config;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.base.BaseFragment;
import com.onemena.data.eventbus.DownloadBean;
import com.onemena.data.eventbus.NightMode;
import com.onemena.home.view.fragment.NewOffLineFragment;
import com.onemena.home.view.fragment.OffLineFragment;
import com.onemena.search.view.fragment.SearchFragment;
import com.onemena.utils.NetworkUtil;
import com.onemena.utils.SpUtil;
import com.onemena.utils.TongJiUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/10/25.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    /**
     * Tab标题
     */
    private List<Fragment> fragments = new ArrayList<Fragment>();
    ;
    private JSONArray titlesArray = new JSONArray();
    private FragmentPagerAdapter adapter;
    private ViewPager viewPager;
    private TabIndictorView indicator;
    private LinearLayout indictor_container;
    private RelativeLayout home_title;
    private ImageView title_logo;
    private View tabindictor_view;
    private ImageView icon_home_down;
    private ProgressBar progress_home;
    private ImageView iv_search;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        adapter = new TabPageIndicatorAdapter(getFragmentManager());
        iv_search = (ImageView) view.findViewById(R.id.iv_search);
        iv_search.setOnClickListener(this);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

        //实例化TabPageIndicator然后设置ViewPager与之关联
        indicator = (TabIndictorView) view.findViewById(R.id.indicator_home);
        indictor_container = (LinearLayout) view.findViewById(R.id.indictor_container);
        home_title = (RelativeLayout) view.findViewById(R.id.home_title);
        title_logo = (ImageView) view.findViewById(R.id.title_logo);
        icon_home_down = (ImageView) view.findViewById(R.id.icon_home_down);
        progress_home = (ProgressBar) view.findViewById(R.id.progress_home);
//        not_network = (TextView) view.findViewById(R.id.not_network);
        initTitle(viewPager, indicator);

        tabindictor_view = view.findViewById(R.id.tabindictor_view);
        icon_home_down.setOnClickListener(this);
        progress_home.setOnClickListener(this);

        EventBus.getDefault().register(this);

        checkMode();

//        Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.anim_notnet);
//
//        not_network.startAnimation(animation);
    }

    private void checkMode() {
        Boolean mode = SpUtil.getBoolean(SPKey.MODE, false);
        if (mode) {
            indicator.nightMode();
            title_logo.setImageResource(R.mipmap.title_logo_night);
            home_title.setBackgroundColor(mContext.getResources().getColor(R.color.txt_234A7D));
            indictor_container.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_1b1b1b));
            tabindictor_view.setBackgroundColor(Color.parseColor("#464646"));
            icon_home_down.setImageResource(R.mipmap.icon_download_navbar_night);

        } else {
            title_logo.setImageResource(R.mipmap.title_logo);
            indicator.dayMode();
            home_title.setBackgroundColor(mContext.getResources().getColor(R.color.main_bule));
            indictor_container.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            tabindictor_view.setBackgroundColor(Color.parseColor("#d6d6d6"));
            icon_home_down.setImageResource(R.mipmap.icon_download_navbar);
        }
    }


    //获取title数据，并初始化对应下边的fragment
    private void initTitle(ViewPager pager, TabIndictorView indicator) {
        String titls = SpUtil.getString(SPKey.TITLEARRAY, Config.DEFORT_NEWS_TITLE);
        titlesArray = JSON.parseArray(titls);
        Collections.reverse(titlesArray);

        JSONObject recommend = new JSONObject();
        recommend.put("title", "اخترنا لك");
        recommend.put("id", "0");

        JSONObject fine = new JSONObject();
        fine.put("title", AppConfig.FINE_NEWS_CATEGORY_NAME);
        fine.put("id", AppConfig.FINE_NEWS_CATEGORY_ID);
        if (NetworkUtil.checkNetWork(mContext)) {
            titlesArray.add(fine);
            titlesArray.add(recommend);
        } else {
            titlesArray.add(recommend);
            titlesArray.add(fine);
        }

        indicator.setViewPagerAndData(pager, titlesArray, titlesArray.size() - 1);

        fragments.clear();
        for (int i = 0; i < titlesArray.size(); i++) {//I am crazy,WTF.I'm still fucking working on this lousy code
            JSONObject jsonObject = titlesArray.getJSONObject(i);
            if (AppConfig.FINE_NEWS_CATEGORY_ID.equals(jsonObject.getString("id"))) {
                FinePagerFragment instance = FinePagerFragment.getInstance();
                fragments.add(instance);
            } else {
                HomePagerFragment fragment = HomePagerFragment.getInstance();
                Bundle bundle = new Bundle();
                bundle.putString(HomePagerFragment.TITLEID, jsonObject.getString("id"));
                bundle.putString(HomePagerFragment.NAME, jsonObject.getString("title"));
                if (i == titlesArray.size() - 1) {
                    bundle.putString(HomePagerFragment.FLAG, i + "");
                }
                fragment.setArguments(bundle);
                fragments.add(fragment);
            }
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.progress_home:
            case R.id.icon_home_down:
                TongJiUtil.getInstance().putEntries(TJKey.OL_ENT);
                if (SpUtil.getBoolean(SPKey.IS_DOWNLOAD, false)) {
                    addToBackStack(OffLineFragment.getInstance());
                } else {
                    addToBackStack(NewOffLineFragment.getInstance());
                }
                break;
            case R.id.iv_search:
                addToBackStack(new SearchFragment());
                break;
        }
    }

    /**
     * ViewPager适配器
     */
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //新建一个Fragment来展示ViewPager item的内容，并传递参数
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titlesArray.getJSONObject(position).getString("title");
        }

        @Override
        public int getCount() {
            return titlesArray.size();
        }
    }

    public void onEventMainThread(NightMode mode) {
        checkMode();
    }

//    public void onEventMainThread(String state) {
//
//        switch (state) {
//            case "0":
//                if (!titlesArray.getJSONObject(titlesArray.size() - 1).getString("title").equals(getString(R.string.local_good_title))) {
//
//                    JSONObject object = new JSONObject();
//                    object.put("title", getString(R.string.local_good_title));
//                    object.put("id", "-1");
//                    titlesArray.add(object);
//                    LocalPagerFragment instance = LocalPagerFragment.getInstance();
//                    fragments.add(instance);
//                    adapter.notifyDataSetChanged();
//                    indicator.setViewPagerAndData(viewPager, titlesArray, titlesArray.size() - 1);
//
//                } else {
//                    viewPager.setCurrentItem(titlesArray.size() - 1);
//                    indicator.selectTab(titlesArray.size() - 1);
//                }
//                break;
//            case "1":
//                if (titlesArray.getJSONObject(titlesArray.size() - 1).getString("title").equals(getString(R.string.local_good_title))) {
//                    titlesArray.remove(titlesArray.size() - 1);
//                    fragments.remove(fragments.size() - 1);
//                    adapter.notifyDataSetChanged();
//                    indicator.setViewPagerAndData(viewPager, titlesArray, titlesArray.size() - 1);
//                }
//                break;
//        }
//    }

    public void onEventMainThread(DownloadBean bean) {
        if (bean.isFinish) {
//            progress_home.setVisibility(View.GONE);
//            icon_home_down.setVisibility(View.VISIBLE);
        } else {
//            progress_home.setVisibility(View.VISIBLE);
//            icon_home_down.setVisibility(View.GONE);
        }
    }

}
