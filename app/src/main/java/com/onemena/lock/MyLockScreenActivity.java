package com.onemena.lock;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.asha.nightowllib.NightOwl;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onemena.app.activity.MainActivity;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.app.fragment.PreloadNewsDetailFragment;
import com.onemena.base.BaseActicity;
import com.onemena.base.BaseFragment;
import com.onemena.data.eventbus.SettingBean;
import com.onemena.home.model.javabean.LockNewsBean;
import com.onemena.http.Api;
import com.onemena.utils.GetPhoneInfoUtil;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.utils.ToastUtil;
import com.onemena.utils.TongJiUtil;
import com.voler.saber.FragmentFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * MyLockScreenActivity Created by voler on 2017/6/9.
 * 说明：
 */

public class MyLockScreenActivity extends BaseActicity {

    private TextView tvData;
    private ViewPager vpNews;
    private ArrayList<LockNewsBean.DocsBean> mList;
    private SimpleDraweeView ivFinish;
    private float initX;
    private ImageView ivSetting;
    private int item;
    private RelativeLayout rlFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // step1 before super.onCreate
        NightOwl.owlBeforeCreate(this);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);


//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_lock);
        // step2 after setContentView
        NightOwl.owlAfterCreate(this);


        tvData = (TextView) findViewById(R.id.tv_data);
        vpNews = (ViewPager) findViewById(R.id.vp_news);
        ivSetting = (ImageView) findViewById(R.id.iv_setting);
        ivSetting.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Observable.timer(500, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> EventBus.getDefault().post(new SettingBean())
                            , Throwable::printStackTrace);
        });
        ivFinish = (SimpleDraweeView) findViewById(R.id.iv_finish);
        rlFinish = (RelativeLayout) findViewById(R.id.rl_finish);

        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse("res://onemena/" + R.drawable.lock))
                .setAutoPlayAnimations(true)
                .build();
        ivFinish.setController(draweeController);//"file:///android_asset/localpicture"
        rlFinish.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
//                        moveX = event.getX() - initX;
//                        requestLayout();
//                        invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (initX - event.getX() > 100) {
                            MyLockScreenActivity.this.finish();
                        }
                        break;
                }
                return true;
            }
        });


        mList = new ArrayList<>();

        vpNews.setOffscreenPageLimit(2);
        vpNews.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                item = mList.size() + 1 - position;
                SpUtil.saveValue(SPKey.LOCK_READ_ITEM, item);
                TongJiUtil.getInstance().putEntries(TJKey.LOCKREAD_SLIDE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        String string = SpUtil.getString(SPKey.LOCK_NEWS);
        if (string.isEmpty()) {
            getNews();
        } else {
            long lastTime = SpUtil.getLong(SPKey.LOCK_UPDATE_TIME);
            long interval = SystemClock.currentThreadTimeMillis() - lastTime;
            if (interval / 1000 / 60 > 5) {
                getNews();
            } else {
                List<LockNewsBean.DocsBean> docs = JSONArray.parseArray(string, LockNewsBean.DocsBean.class);
                mList.addAll(0, docs);
                vpNews.setAdapter(new MyAdapter(this));
                item = SpUtil.getInt(SPKey.LOCK_READ_ITEM);
                int position = mList.size() - item;
                if (position < 0) {
                    position = 0;
                }
                vpNews.setCurrentItem(position);//jkhfs
            }
        }
    }

    public String formatDateStampString() {
        long when = System.currentTimeMillis();
        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT
                | DateUtils.FORMAT_ABBREV_ALL
                | DateUtils.FORMAT_SHOW_DATE
                | DateUtils.FORMAT_SHOW_WEEKDAY;
        return DateUtils.formatDateTime(this, when, format_flags);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // step3 onResume
        NightOwl.owlResume(this);
        tvData.setText(formatDateStampString());
        TongJiUtil.getInstance().putEntries(TJKey.LOCKREAD_POP);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        setIntent(intent);
        BaseFragment.onPopBackAllStack(this);
    }

    private void getNews() {

        Api.getComApi().getLockData(GetPhoneInfoUtil.INSTANCE.getAndroidId())
                .subscribeOn(Schedulers.io())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lockNewsBean -> {
                    List<LockNewsBean.DocsBean> docs = lockNewsBean.getDocs();
                    for (LockNewsBean.DocsBean doc : docs) {
                        if ("n".equals(doc.getDType())) {
                            List<String> img_url = doc.getImg_url();
                            if (img_url != null && img_url.size() > 0) {
                                mList.add(0, doc);
                            }
                        }
                    }

                    SpUtil.saveValue(SPKey.LOCK_UPDATE_TIME, SystemClock.currentThreadTimeMillis());
                    SpUtil.saveValue(SPKey.LOCK_NEWS, JSONObject.toJSONString(mList));

                    vpNews.setAdapter(new MyAdapter(this));
                    vpNews.setCurrentItem(mList.size());
                }, Throwable::printStackTrace);
    }

    private void loadMore() {
        Api.getComApi().getLockData(GetPhoneInfoUtil.INSTANCE.getAndroidId())
                .subscribeOn(Schedulers.io())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lockNewsBean -> {
                    List<LockNewsBean.DocsBean> docs = lockNewsBean.getDocs();
                    for (LockNewsBean.DocsBean doc : docs) {
                        if ("n".equals(doc.getDType())) {
                            List<String> img_url = doc.getImg_url();
                            if (img_url != null && img_url.size() > 0) {
                                mList.add(0, doc);
                            }
                        }
                    }

                    SpUtil.saveValue(SPKey.LOCK_UPDATE_TIME, SystemClock.currentThreadTimeMillis());
                    SpUtil.saveValue(SPKey.LOCK_NEWS, JSONObject.toJSONString(mList));
                    vpNews.setAdapter(new MyAdapter(this));
                    vpNews.setCurrentItem(mList.size() + 1 - item);
                    ToastUtil.showDevShortToast("loadmore");
                }, Throwable::printStackTrace);
    }


    //PagerAdapter是object的子类
    class MyAdapter extends PagerAdapter {


        private final Context mContext;

        public MyAdapter(Context context) {
            mContext = context;
        }

        /**
         * PagerAdapter管理数据大小
         */
        @Override
        public int getCount() {
            return mList.size() + 2;
        }

        /**
         * 关联key 与 obj是否相等，即是否为同一个对象
         */
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj; // key
        }

        /**
         * 销毁当前page的相隔2个及2个以上的item时调用
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object); // 将view 类型 的object熊容器中移除,根据key
        }

        /**
         * 当前的page的前一页和后一页也会被调用，如果还没有调用或者已经调用了destroyItem
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view;
            position--;
            if (position == -1) {
                view = View.inflate(mContext, R.layout.item_lock_last, null);
                SimpleDraweeView load = (SimpleDraweeView) view.findViewById(R.id.img_load);

                DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                        .setUri(Uri.parse("res://onemena/" + R.drawable.img_new_loading))
                        .setAutoPlayAnimations(true)
                        .build();
                load.setController(draweeController);

                loadMore();
            } else if (position == mList.size()) {
                view = View.inflate(mContext, R.layout.item_lock_first, null);
                view.findViewById(R.id.tv_open_app).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        open app
                        startActivity(new Intent(MyLockScreenActivity.this, MainActivity.class));
                        finish();
                    }
                });
            } else {
                view = View.inflate(mContext, R.layout.item_lock_normal, null);
                SimpleDraweeView ivImage = (SimpleDraweeView) view.findViewById(R.id.iv_image);
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
                TextView tvCommentCount = (TextView) view.findViewById(R.id.tv_comment_count);
//                ImageView ivReadMore = (ImageView) view.findViewById(R.id.iv_read_more);
//                TextView tvReadMore = (TextView) view.findViewById(R.id.tv_read_more);

                LockNewsBean.DocsBean docsBean = mList.get(position);
                String image = docsBean.getImg_url().get(0);
                ivImage.setImageURI(Uri.parse(image));
                tvTitle.setText(docsBean.getTitle());

                Document doc = Jsoup.parse(docsBean.getContent());
                Elements links = doc.getElementsByTag("p");
                String contentStr = "";
                for (Element link : links) {
                    contentStr += link.text();
//                    ToastUtil.showDevShortToast(contentStr);
                }


                tvContent.setText(contentStr);
                tvCommentCount.setText(StringUtils.int2IndiaNum(docsBean.getComment_count()));
                view.setOnClickListener(v -> {
//                    PreloadNewsDetailFragment lockNewsDetailFragment = LockNewsDetailFragment.newInstance(docsBean.getId(), "", "", "12", "", docsBean.getContent(), docsBean.getTitle());

                    PreloadNewsDetailFragment preloadNewsDetailFragment = FragmentFactory.createPreloadNewsDetailFragment(docsBean.getId(), "", "", "12", docsBean.getContent(), docsBean.getTitle());
                    String className = preloadNewsDetailFragment.getClass().getName();
                    BaseFragment.addToBackStack(MyLockScreenActivity.this, className, preloadNewsDetailFragment);
                });
//                tvReadMore.setOnClickListener(v -> ToastUtil.showDevShortToast("gengduo"));
            }
            container.addView(view);
            return view; // 返回该view对象，作为key
        }
    }
}
