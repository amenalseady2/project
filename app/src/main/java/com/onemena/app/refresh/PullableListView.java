package com.onemena.app.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ListView;

import com.arabsada.news.R;
import com.onemena.utils.LogManager;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;

public class PullableListView extends ListView implements Pullable, AbsListView.OnScrollListener {


    /**
     * 底部布局
     */
    private View footerView;

    private boolean canPullUp = true;

    private PullToRefreshLayout pullToResfresh;
    private RotateAnimation rotateAnimation;
    private View loadingView;
    private Subscriber<? super Object> subscriber;

    public PullableListView(Context context) {
        super(context);
    }

    public PullableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        //设置滑动监听
        setOnScrollListener(this);
        //初始化为尾布局
        initFooterView();
        Observable.create(subscriber -> {
            //订阅没取消
            if (!subscriber.isUnsubscribed())
            //发送消息
            {
                this.subscriber = subscriber;
            }
        })
                .throttleFirst(2500, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    footerView.setVisibility(VISIBLE);
                    loadingView.clearAnimation();
                    loadingView.startAnimation(rotateAnimation);
                    pullToResfresh.loadMore();
                });
    }

    @Override
    public int getLayoutDirection() {
        return LAYOUT_DIRECTION_RTL;
    }

    //初始化底布局，与头布局同理
    private void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.load_more, null);
        loadingView = footerView.findViewById(R.id.loading_icon);
        rotateAnimation = new RotateAnimation(0, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatMode(RotateAnimation.RESTART);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        footerView.setVisibility(INVISIBLE);
        addFooterView(footerView);
    }


    @Override
    public boolean canPullDown() {

        if (getCount() < 3) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (getChildAt(0) != null && getFirstVisiblePosition() == 0
                && getChildAt(0).getTop() >= 0) {
            // 滑到ListView的顶部了
            return true;
        } else
            return false;
    }

    @Override
    public boolean canPullUp() {
        return canPullUp;
    }

    public void setCanPullUp(boolean canPullUp) {
        this.canPullUp = canPullUp;
        footerView.measure(0, 0);
        footerView.setPadding(0, -footerView.getMeasuredHeight(), 0, 0);
        LogManager.i("------------");
//        footerView.setVisibility(GONE);
//        ((BaseAdapter) getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void setPullToResfresh(PullToRefreshLayout pullToResfresh) {
        this.pullToResfresh = pullToResfresh;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.e("-----", String.valueOf(scrollState));
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.e("-----", String.valueOf(firstVisibleItem));
        if (canPullUp) {
            int count = getCount();
            if (count > 7 && getLastVisiblePosition() == (count - 1)) {
                this.subscriber.onNext(null);
            }
        }
    }

    public void invisibleFooter() {
        footerView.setVisibility(INVISIBLE);
    }
}
