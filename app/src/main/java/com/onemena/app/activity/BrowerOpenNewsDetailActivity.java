package com.onemena.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;

import com.arabsada.news.R;
import com.onemena.app.fragment.HomeNewsDetailFragment;
import com.onemena.base.BaseActicity;

/**
 * Created by Administrator on 2016/11/16.
 */

public class BrowerOpenNewsDetailActivity extends BaseActicity {

    public static final String ID="id";
    public static final String NEWS_TYPE="news_type";
    public static final String FOLLOW="follow";

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private String newsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetail);
        /**
         * 拿到事务管理器并开启事务
         */
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

/**从网页获取数据
 //        Intent i_getvalue = getIntent();
 //        String action = i_getvalue.getAction();
 //
 //        if(Intent.ACTION_VIEW.equals(action)){
 //            Uri uri = i_getvalue.getData();
 //            if(uri != null){
 //                newsId = uri.getQueryParameter("id");
 //            }
 //        }
 **/
        Intent intent = getIntent();
        newsId = intent.getStringExtra(ID);
        String type = intent.getStringExtra(NEWS_TYPE);
        String follow = intent.getStringExtra(FOLLOW);

        HomeNewsDetailFragment instance = HomeNewsDetailFragment.newInstance(
        newsId,"","", type, follow);
        transaction.replace(R.id.content_fragment, instance);

        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private MainActivity.MyTouchListener myTouchListeners;

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void registerMyTouchListener(MainActivity.MyTouchListener listener) {
        myTouchListeners = listener;
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void unRegisterMyTouchListener(MainActivity.MyTouchListener listener) {
        myTouchListeners = null;
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (myTouchListeners != null) {
            myTouchListeners.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

}
