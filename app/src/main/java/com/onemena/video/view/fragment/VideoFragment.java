package com.onemena.video.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.config.Config;
import com.onemena.app.config.SPKey;
import com.onemena.base.BaseFragment;
import com.custom.indictor.TabIndictorView;
import com.onemena.utils.SpUtil;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/10/25.
 */
public class VideoFragment extends BaseFragment {

    /**
     * Tab标题
     */
    private ArrayList<String>  TITLE = new ArrayList<String> ();

    private ArrayList<VideoPagerFragment> fragments=new ArrayList<VideoPagerFragment>();
    private int mCurrentFragment=TITLE.size()-1;
    private JSONArray titlesArray;
    private FragmentPagerAdapter adapter;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(),R.layout.fragment_video,null);
    }



    protected void initData(View view, Bundle savedInstanceState) {

        adapter = new TabPageIndicatorAdapter(getFragmentManager());
        ViewPager pager = (ViewPager)view.findViewById(R.id.view_pager_video);
        pager.setAdapter(adapter);

        //实例化TabPageIndicator然后设置ViewPager与之关联
        TabIndictorView indicator = (TabIndictorView)view.findViewById(R.id.indicator_video);
        initTitle(pager,indicator);




    }
    //获取title数据，并初始化对应下边的fragment
    private void initTitle(ViewPager pager, TabIndictorView indicator) {
        String titls= SpUtil.getString(SPKey.VIDEO_TITLE, Config.DEFORT_VIDEO_TITLE);
        titlesArray = JSON.parseArray(titls);
//        titlesArray.clear();
        TITLE.clear();
        for (int i= titlesArray.size()-1;i>=0;i--){
            JSONObject obj = titlesArray.getJSONObject(i);
            String name = ((JSONObject) obj).getString("category_title");
            TITLE.add(name);
        }
        mCurrentFragment=TITLE.size()-1;
        indicator.setViewPagerAndData(pager,TITLE,TITLE.size()-1);

        fragments.clear();
        for (int i=0;i<TITLE.size();i++){
            VideoPagerFragment fragment =new VideoPagerFragment();
            Bundle bundle = new Bundle();
            bundle.putString("titleId",titlesArray.getJSONObject(TITLE.size()-1-i).getString("category_id"));
            fragment.setArguments(bundle);
            fragments.add(fragment);

            adapter.notifyDataSetChanged();
        }
    }


    //fragment隐藏的时候，关闭video
    public void hidden() {
        fragments.get(mCurrentFragment).hidden();
    }

    /**
     * ViewPager适配器
     * @author len
     *
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
            return TITLE.get(position % TITLE.size());
        }

        @Override
        public int getCount() {
            return TITLE.size();
        }
    }
}
