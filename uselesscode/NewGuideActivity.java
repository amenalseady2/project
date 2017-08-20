package com.onemena.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.arabsada.news.NewsApplication;
import com.arabsada.news.R;
import com.onemena.app.adapter.NewGuideViewPagerAdapter;
import com.onemena.app.config.SPKey;
import com.onemena.utils.SpUtil;
import com.onemena.utils.Utility;

import java.util.ArrayList;
import java.util.List;


/**
 * 导航页
 *
 * @author zhangyushui
 * @Description:
 * @date 2016-8-5 下午1:29:00
 */
public class NewGuideActivity extends BaseActicity implements OnPageChangeListener, OnTouchListener {
    private NewGuideViewPagerAdapter adapter;
    private ViewPager pager;
    private List<View> views;//视图数据
    private ImageView[] point;//底部小圆点
    private int currentId = 0;//当前ID
    private int lastX = 0;

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, NewGuideActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			// 透明状态栏
//			getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		}
        setContentView(R.layout.activity_new_guide);
        ((NewsApplication) getApplication()).addActivity(this);
        initView();
//		setPoint();//第一次设置小圆点位置
    }


    /**
     * 初始化view
     */
    private void initView() {
        pager = (ViewPager) this.findViewById(R.id.vp);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        views = new ArrayList<View>();


        views.add(inflater.inflate(R.layout.user_guide_two, null));
        views.add(inflater.inflate(R.layout.user_guide_one, null));


        pager.setOnTouchListener(this);
        adapter = new NewGuideViewPagerAdapter(views, NewGuideActivity.this);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);

        pager.setCurrentItem(views.size());
    }


    /**
     * 设置小圆点
     */
    private void setPoint() {
        LinearLayout ll = (LinearLayout) this.findViewById(R.id.viewpager_ll);
        point = new ImageView[ll.getChildCount()];
        for (int i = 0; i < ll.getChildCount(); i++) {
            if (currentId == i) {
                point[i] = (ImageView) ll.getChildAt(i);
                point[i].setImageResource(R.drawable.guide_point_select);
            } else {
                point[i] = (ImageView) ll.getChildAt(i);
                point[i].setImageResource(R.drawable.guide_point_normal);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO 滑动状态监听方法

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        //TODO 选择页面的监听方法
        currentId = arg0;
//		setPoint();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if ((lastX - event.getX()) < -50 && (currentId == 0)) {
                    start();
                }
                break;
            default:
                break;
        }
        return false;
    }


    private void start() {
//		MainActivity.startActivity(NewGuideActivity.this);
        InterestChoseActivity.startActivity(NewGuideActivity.this);
        SpUtil.saveValue(SPKey.USER_GUIDE + Utility.getAppVersionCode(NewGuideActivity.this), true);
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // do nothing
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        ((NewsApplication) getApplication()).removeActivity(this);
    }

}

