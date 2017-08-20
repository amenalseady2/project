package com.onemena.widght;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/11/9.
 */

public class InteruptContainer extends RelativeLayout {
    private ViewPager viewPager;
    public InteruptContainer(Context context) {
        super(context);
    }

    public InteruptContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (viewPager!=null) {
            if (ev.getAction() == MotionEvent.ACTION_UP) {

                viewPager.requestDisallowInterceptTouchEvent(false);
            } else {
                viewPager.requestDisallowInterceptTouchEvent(true);
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
