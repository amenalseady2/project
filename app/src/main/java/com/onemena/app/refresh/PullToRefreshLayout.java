package com.onemena.app.refresh;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.arabsada.news.R;
import com.onemena.app.config.SPKey;
import com.onemena.utils.LogManager;
import com.onemena.utils.SpUtil;
import com.onemena.widght.HelveRomanTextView;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 自定义的布局，用来管理三个子控件，其中一个是下拉头，一个是包含内容的pullableView（可以是实现Pullable接口的的任何View），
 * 还有一个上拉头，更多详解见博客http://blog.csdn.net/zhongkejingwang/article/details/38868463
 *
 * @author 陈靖
 */
public class PullToRefreshLayout extends RelativeLayout {
    public static final String TAG = "PullToRefreshLayout";
    // 初始状态
    public static final int INIT = 0x0001;
    // 释放刷新
    public static final int RELEASE_TO_REFRESH = 0x0002;
    // 正在刷新
    public static final int REFRESHING = 0x0004;
    // 释放加载
//    public static final int RELEASE_TO_LOAD = 0x0008;
    // 正在加载
//    public static final int LOADING = 0x0016;
    // 操作完毕
    public static final int DONE = 0x0032;
    //没有更多数据
    public static final int NOTMORE = 0x0064;
    // 当前状态
    private int state = INIT;
    // 刷新回调接口
    private OnRefreshListener mListener;
    // 刷新成功
    public static final int SUCCEED = 0x0128;
    // 刷新失败
    public static final int FAIL = 0x0256;
    // 按下Y坐标，上一个事件点Y坐标
    private float downY, lastY;

    // 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
    public float pullDownY = 0;
    // 上拉的距离
//    private float pullUpY = 0;

    // 释放刷新的距离
    private float refreshDist = 150;
    // 释放加载的距离
    private float loadmoreDist = 150;

    private MyTimer timer;
    // 回滚速度
    public float MOVE_SPEED = 8;
    // 第一次执行布局
    private boolean isLayout = false;
    // 在刷新过程中滑动操作
    private boolean isTouch = false;
    // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
    private float radio = 2;

    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;

    // 下拉头
    private View refreshView;
    // 下拉的箭头
    private View pullView;
    // 正在刷新的图标
    private ImageView refreshingView;
    // 刷新结果图标
    private View refreshStateImageView;
    // 刷新结果：成功或失败
    private HelveRomanTextView refreshStateTextView;

    // 上拉头
//    private View loadmoreView;
    // 上拉的箭头
//    private View pullUpView;
    // 正在加载的图标
//    private View loadingView;
    // 加载结果图标
//    private View loadStateImageView;
    // 加载结果：成功或失败
//    private NewContentTextView loadStateTextView;

    // 实现了Pullable接口的View
    private View pullableView;
    // 过滤多点触碰
    private int mEvents;
    // 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
    private boolean canPullDown = true;
    private boolean canPullUp = true;
    private boolean isLoading = false;


    /**
     * 执行自动回滚的handler
     */
    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
//                thouchMove();
                refresh();
                return;
            }
            // 回弹速度随下拉距离moveDeltaY增大而增大
            MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY)));
            if (!isTouch) {
                // 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
                if (state == REFRESHING && pullDownY <= refreshDist) {
                    pullDownY = refreshDist;
                    timer.cancel();
                }
            }
            if (pullDownY > 0)
                pullDownY -= MOVE_SPEED;
            if (pullDownY <= 0) {
                // 已完成回弹
                pullDownY = 0;
                pullView.clearAnimation();
                // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING)
                    changeState(INIT);
                timer.cancel();
            }
            // 刷新布局,会自动调用onLayout
            requestLayout();
        }

    };
    private Context mContext;
    private RelativeLayout head_bg;
    private AnimationDrawable loadingAnimation;
    private boolean isOnAnim = false;
    private int head_bg_height;
    private HelveRomanTextView resultTextView;
    private boolean isInit = false;

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public PullToRefreshLayout(Context context) {
        this(context, null);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        timer = new MyTimer(updateHandler);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        refreshingAnimation.setInterpolator(lir);
    }

    private void hide() {
        timer.schedule(5);
    }


    public void loadMore() {
        if (mListener != null && !isLoading) {
            isLoading = true;
            mListener.onLoadMore(this);
        }
    }

    public void loadmoreFinish(int state) {
        switch (state) {
            case SUCCEED:
                isLoading = false;
                break;
            case FAIL:
                isLoading = false;
                updateHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMore();
                    }
                },500);
                break;
            case NOTMORE:
                isLoading = false;
                ((PullableListView) pullableView).setCanPullUp(false);
                break;
        }
    }


    public void refreshFinish(int refreshResult, int num, boolean isLoadMore) {

        isLoading = false;//加载更多和刷新还没有分开
        if (isLoadMore) {
            loadmoreFinish(refreshResult);
            return;
        }
        if (state == INIT) return;
        if (refreshingView == null) return;
        refreshingView.clearAnimation();
        refreshingView.setVisibility(View.GONE);


        //刷新成功，高度变为原来的一般
        ViewGroup.LayoutParams layoutParams2 = head_bg.getLayoutParams();
        layoutParams2.height = head_bg_height / 3 * 2;
        head_bg.setLayoutParams(layoutParams2);
        refreshDist = head_bg_height / 3 * 2;
        hide();
        resultTextView.setGravity(Gravity.CENTER_VERTICAL);

        switch (refreshResult) {
            case SUCCEED:
                // 刷新成功
                refreshStateImageView.setVisibility(View.GONE);
                refreshStateTextView.setVisibility(View.GONE);
                resultTextView.setVisibility(View.VISIBLE);
//				refreshStateImageView
//						.setBackgroundResource(R.mipmap.refresh_succeed);

                if (num == 0) {
                    resultTextView.setText(getResources().getString(R.string.refresh_succeed));//刷新成功
                } else {
                    resultTextView.setText("لديك " + num + " أخبار قد تعجبك");//刷新成功，当前为你推荐X条新闻
                }
                if (SpUtil.getBoolean(SPKey.MODE, false)) {
                    //夜间模式
                    head_bg.setBackgroundColor(Color.parseColor("#788286"));
                    resultTextView.setTextColor(Color.parseColor("#23618E"));
                } else {
                    //白天模式
                    head_bg.setBackgroundColor(Color.parseColor("#E8F1FD"));
                    resultTextView.setTextColor(Color.parseColor("#3E84E0"));
                }

                break;
            case NOTMORE:
                // 刷新成功
                refreshStateImageView.setVisibility(View.GONE);
                refreshStateTextView.setVisibility(View.GONE);
                resultTextView.setVisibility(View.VISIBLE);
//				refreshStateImageView
//						.setBackgroundResource(R.mipmap.refresh_succeed);

                resultTextView.setText("لايوجد المزيد من الأخبار حالياً");//刷新成功，没有更多数据
                if (SpUtil.getBoolean(SPKey.MODE, false)) {
                    //夜间模式
                    head_bg.setBackgroundColor(Color.parseColor("#788286"));
                    resultTextView.setTextColor(Color.parseColor("#23618E"));
                } else {
                    //白天模式
                    head_bg.setBackgroundColor(Color.parseColor("#E8F1FD"));
                    resultTextView.setTextColor(Color.parseColor("#3E84E0"));
                }
                if (isLoadMore) {
                    ((PullableListView) pullableView).setCanPullUp(false);
                    loadMore();
                }
                break;
            case FAIL:
            default:
                // 刷新失败
                refreshStateImageView.setVisibility(View.GONE);
                refreshStateTextView.setVisibility(View.GONE);
                resultTextView.setVisibility(View.VISIBLE);
//				refreshStateImageView
//						.setBackgroundResource(R.mipmap.refresh_failed);
                resultTextView.setText(R.string.refresh_fail);
                if (SpUtil.getBoolean(SPKey.MODE, false)) {
                    head_bg.setBackgroundColor(Color.parseColor("#967e7e"));
                    resultTextView.setTextColor(Color.parseColor("#652c2c"));
                } else {
                    head_bg.setBackgroundColor(Color.parseColor("#FCD8D2"));
                    resultTextView.setTextColor(Color.parseColor("#EB4E35"));
                }
                if (isLoadMore) {
                    loadMore();
                }
                break;
        }


        // 开启动画  isOnAnim避免动画重复执行
        if (!isOnAnim) {
            ///动画效果
            ScaleAnimation sa = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            sa.setDuration(300);
            // 设定重复次数，实际次数是重复次数加一。Animation.INFINITE为-1，表示无限次循环
            sa.setRepeatCount(1);
            // 设定重复模式.Animation.REVERS=2表示倒序重复 ，，，Animation.RESTART=1，表示从头开始
            sa.setRepeatMode(Animation.REVERSE);
            if (!isOnAnim) {
                resultTextView.startAnimation(sa);
            }
            sa.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isOnAnim = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isOnAnim = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        // 刷新结果停留1秒
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                changeState(DONE);
                hide();
            }
        }.sendEmptyMessageDelayed(0, 2500);
    }


    private void changeState(int to) {
        state = to;
        Boolean mode = SpUtil.getBoolean(SPKey.MODE, false);
        if (mode) {//夜间模式
            refreshView.setBackgroundColor(Color.parseColor("#1b1b1b"));
        } else {
            refreshView.setBackgroundColor(Color.parseColor("#F8F8F8"));
        }
        switch (state) {
            case INIT:
                // 下拉布局初始状态
                ViewGroup.LayoutParams layoutParams = head_bg.getLayoutParams();
                layoutParams.height = head_bg_height;
                head_bg.setLayoutParams(layoutParams);

                refreshDist = head_bg_height;

                refreshStateImageView.setVisibility(View.GONE);
                refreshStateTextView.setText(R.string.pull_to_refresh);

                refreshStateTextView.setVisibility(View.VISIBLE);
                resultTextView.setVisibility(View.GONE);

//				pullView.clearAnimation();
                pullView.setVisibility(View.VISIBLE);
                refreshingView.setVisibility(View.GONE);
//                refreshingView.clearAnimation();//新闻应注释掉
                loadingAnimation.stop();//足球应注释掉
                break;
            case RELEASE_TO_REFRESH:
                // 释放刷新状态
                refreshStateTextView.setText(R.string.release_to_refresh);

//				pullView.startAnimation(rotateAnimation);
                break;
            case REFRESHING:
                // 正在刷新状态
//				pullView.clearAnimation();
                pullView.setVisibility(View.INVISIBLE);
                refreshingView.setVisibility(View.VISIBLE);
                loadingAnimation.start();//足球应注释掉
//				refreshingView.startAnimation(refreshingAnimation);//新闻应注释掉
                refreshStateTextView.setText(R.string.refreshing);
                refreshStateTextView.setTextColor(Color.parseColor("#A1A6BB"));

                break;
            case DONE:
                // 刷新或加载完毕，啥都不做
                break;
        }
        if (SpUtil.getBoolean(SPKey.MODE, false)) {
            //夜间模式
            refreshStateTextView.setTextColor(Color.parseColor("#707070"));
        } else {
            refreshStateTextView.setTextColor(Color.parseColor("#A1A6BB"));
        }
    }

    /**
     * 不限制上拉或下拉
     */
    private void releasePull() {
        canPullDown = true;
        canPullUp = true;
    }

    private void setOnlyCanPullDown() {
        canPullDown = true;
        canPullUp = false;
    }

    private void setOnlyCanPullUp() {
        canPullDown = false;
        canPullUp = true;
    }

    public void setCannotPullDown() {
        canPullDown = false;
    }


    private void setCannotPullUpAndDown() {
        canPullDown = false;
        canPullUp = false;
    }

    /*
     * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突
     *
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                head_bg.setBackgroundColor(Color.TRANSPARENT);
                refreshStateTextView.setTextColor(Color.parseColor("#A1A6BB"));

                if (state != REFRESHING) {
                    changeState(INIT);
                    timer.cancel();
                }

                downY = ev.getY();
                lastY = downY;

                mEvents = 0;
                //releasePull();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (thouchMove(ev)) return true;
                break;
            case MotionEvent.ACTION_UP:
                thouchUp();
            default:
                break;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

    private void thouchUp() {
        if (pullDownY > refreshDist)
            // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
            isTouch = false;
        if (state == RELEASE_TO_REFRESH) {
            changeState(REFRESHING);
            // 刷新操作
            if (mListener != null)
                mListener.onRefresh(this);
        }

        hide();
    }

    public void refresh() {
        if (isInit) {
            if (REFRESHING != state) {

                changeState(REFRESHING);
                // 刷新操作
                if (mListener != null)
                    mListener.onRefresh(this);
                hide();
            }
        } else {
            Message obtain = Message.obtain();
            obtain.what = 1;
            updateHandler.sendMessageDelayed(obtain, 300);
        }
    }


    private boolean thouchMove(MotionEvent ev) {
        return thouchMove(ev, 0);
    }

    private boolean thouchMove(MotionEvent ev, float y) {
        if (ev != null) {
            y = ev.getY();
        }
        if (mEvents == 0) {
            if (((Pullable) pullableView).canPullDown() && canPullDown) {
                // 可以下拉，正在加载时不能下拉
                // 对实际滑动距离做缩小，造成用力拉的感觉
                pullDownY = pullDownY + (y - lastY) / radio;
                LogManager.i(pullDownY + "");
                if (pullDownY < 0) {
                    pullDownY = 0;
//							canPullDown = false;
//							canPullUp = true;
                }
                if (pullDownY > getMeasuredHeight())
                    pullDownY = getMeasuredHeight();
                if (state == REFRESHING) {
                    // 正在刷新的时候触摸移动
                    isTouch = true;
                }
            }
        } else
            mEvents = 0;
        lastY = y;
        //下拉距离小的时候，将事件交给父布局处理，避免跟viewpager左右滑动冲突
        if (lastY - downY < 100 && lastY - downY > -100) {
            if (ev != null) {
                super.dispatchTouchEvent(ev);
                return true;
            }
        }
        // 根据下拉距离改变比例
        radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
                * (pullDownY)));
        requestLayout();

        if (pullDownY <= refreshDist && state == RELEASE_TO_REFRESH) {
            // 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
            changeState(INIT);
        }

        LogManager.i(pullDownY + "===" + refreshDist + "~~~~" + state);
        if (pullDownY >= refreshDist && state == INIT) {
            // 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
            changeState(RELEASE_TO_REFRESH);
        }


        // 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
        // Math.abs(pullUpY))就可以不对当前状态作区分了
        if ((pullDownY) > 8) {
            // 防止下拉过程中误触发长按事件和点击事件
            if (ev != null) {
                ev.setAction(MotionEvent.ACTION_CANCEL);
            }
        }
        return false;
    }

    private void initView() {
        // 初始化下拉布局
        pullView = refreshView.findViewById(R.id.pull_icon);
        refreshStateTextView = (HelveRomanTextView) refreshView
                .findViewById(R.id.state_tv);
        resultTextView = (HelveRomanTextView) refreshView
                .findViewById(R.id.tv_result);
        refreshStateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        resultTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);


        head_bg = (RelativeLayout) refreshView.findViewById(R.id.head_bg);
        head_bg_height = head_bg.getMeasuredHeight();
        refreshingView = (ImageView) refreshView.findViewById(R.id.refreshing_icon);
//		Glide.with(mContext).load(R.mipmap.loadinggif).into(refreshingView);
        refreshingView.setBackgroundResource(R.drawable.loading_anim);//足球应注释掉

        //[3]获取AnimationDrawable类型  获取动画的资源  就可以开始播放动画了
        loadingAnimation = (AnimationDrawable) refreshingView.getBackground();//足球应注释掉


        refreshStateImageView = refreshView.findViewById(R.id.state_iv);
        // 初始化上拉布局

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isLayout) {
            // 这里是第一次进来的时候做一些初始化
            refreshView = getChildAt(0);
            pullableView = getChildAt(1);
            ((Pullable) pullableView).setPullToResfresh(this);
            isLayout = true;
            initView();

            refreshDist = ((ViewGroup) refreshView).getChildAt(0)
                    .getMeasuredHeight();
        }

        // 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分

        refreshView.layout(0,
                (int) (pullDownY) - refreshView.getMeasuredHeight(),
                refreshView.getMeasuredWidth(), (int) (pullDownY));
        pullableView.layout(0, (int) (pullDownY),
                pullableView.getMeasuredWidth(), (int) (pullDownY)
                        + pullableView.getMeasuredHeight());

        isInit = true;
    }

    class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTask mTask;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        public void schedule(long period) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask(handler);
            timer.schedule(mTask, 0, period);
        }

        public void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        class MyTask extends TimerTask {
            private Handler handler;

            public MyTask(Handler handler) {
                this.handler = handler;
            }

            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }

        }
    }

    /**
     * 刷新加载回调接口
     *
     * @author chenjing
     */
    public interface OnRefreshListener {
        /**
         * 刷新操作
         */
        void onRefresh(PullToRefreshLayout pullToRefreshLayout);

        /**
         * 加载操作
         */
        void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
    }

    public int getState() {
        return state;
    }
}
