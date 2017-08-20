package com.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by Administrator on 2017/4/7.
 */

public class NewsDetailHorizontalScrollView extends HorizontalScrollView {
    public boolean isIntercept() {
        return isIntercept;
    }

    public void setIntercept(boolean intercept) {
        isIntercept = intercept;
    }

    private boolean isIntercept = false;

    public NewsDetailHorizontalScrollView(Context context) {
        super(context);
    }

    public NewsDetailHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsDetailHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isIntercept = true;
                break;
            case MotionEvent.ACTION_MOVE:
                isIntercept = true;
                break;
            case MotionEvent.ACTION_CANCEL:
                isIntercept = false;
                break;
            case MotionEvent.ACTION_UP:
                isIntercept = false;
        }
        return super.onTouchEvent(event);
    }

}
