package com.onemena.app.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 新的引导页迭代器
 * 
 * @author yangshenghui
 * 
 */
public class NewGuideViewPagerAdapter extends PagerAdapter {
	private List<View> data;
	private Activity activity;


	public NewGuideViewPagerAdapter(List<View> data, Activity activity) {
		this.data = data;
		this.activity = activity;

	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		((ViewPager) container).addView(data.get(position));
		return data.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
