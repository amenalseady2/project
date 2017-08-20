package com.onemena.widght;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.onemena.app.NewsApplication;
import com.arabsada.news.R;
import com.onemena.app.adapter.HomePopwinGridViewAdapter;
import com.onemena.app.config.Config;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.TJKey;
import com.onemena.app.refresh.PullableListView;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.data.eventbus.POPWindow;
import com.onemena.listener.JsonObjectListener;
import com.onemena.service.PublicService;
import com.onemena.home.view.adapter.NewsListAdapter;
import com.onemena.utils.StringUtils;
import com.onemena.utils.TongJiUtil;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/12/6.
 */

public class PopwinDay implements HomePopwinGridViewAdapter.AddTagListener {
    private static PopwinDay popwinDay;
    private static Activity mContext;
    private static String titleId;
    private PopupWindow popWin;
    private View popview;
    private StringBuffer stringBuffer;
    private JSONArray tags;
    private PullableListView listview;

    private PopwinDay() {
    }

    public static PopwinDay getInstance(Activity context, String titleId) {
        mContext = context;
        PopwinDay.titleId = titleId;
        if (popwinDay == null) {
            synchronized (PopwinDay.class) {
                if (popwinDay == null) {
                    popwinDay = new PopwinDay();
                }
            }
        }
        return popwinDay;
    }

    public void showDayPopwindow(final View v, final NewsListAdapter itemAdapter,
                                 PullableListView pullable_listview, String titleName, final int position, String first_name, final String id) {
        listview = pullable_listview;

        if (popWin != null) {
            //确保只有一个popWin
            return;
        }
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        final int positionY = location[1];
        //得到数据

        tags = new JSONArray();
        tags.add(first_name);
        if (!titleName.equals("اخترنا لك")) {
            tags.add(titleName);
        }
        tags.add("محتوى مكرر، قديم");
        tags.add("جودة المحتوى متدنية");


        tags = changeArray(tags);
        // 得到popview
        popview = View.inflate(mContext.getApplicationContext(),
                R.layout.dialog_pop_home, null);

        RelativeLayout root_viewgroup = (RelativeLayout) popview.findViewById(R.id.root_viewgroup);
        TextView dislike_tv = (TextView) popview.findViewById(R.id.video_list_comment);
        GridView gridview = (GridView) popview.findViewById(R.id.gridview);

        dislike_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 发送数据给服务器
                //sendData();
                if (popWin != null && popWin.isShowing()) {
                    popWin.dismiss();

                    int headerCount = listview.getHeaderViewsCount();

                    showDialog();
                    int firstVisiblePosition = listview.getFirstVisiblePosition();
                    View view1 = listview.getChildAt(position + headerCount - firstVisiblePosition);
//                    listview.get
                    deleteCell(itemAdapter, view1, position);

                    submit(id, stringBuffer);
                }

                TongJiUtil.getInstance().putEntries(TJKey.REDUCE_SUBMIT,
                        MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE),
                        MyEntry.getIns(TJKey.CATEGORY_ID, titleId),
                        MyEntry.getIns(TJKey.RESOURCE_ID, id));


            }
        });
        HomePopwinGridViewAdapter GVAdapter = new HomePopwinGridViewAdapter(mContext);
        GVAdapter.SetAddTagListener(this);
        gridview.setAdapter(GVAdapter);
        GVAdapter.notifyDataSetChanged(0, tags);
        popview.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);


        final int popwin_margin_left = (int) mContext.getResources().getDimension(R.dimen.popwin_margin_left);//距离屏幕左边的距离
        if (positionY > Config.displayHeight / 2) {
            //按钮在屏幕下边，popwin在上边显示
            root_viewgroup.setBackgroundResource(R.mipmap.home_bg_ru);
            popWin = new PopupWindow(popview,
                    Config.displayWidth - popwin_margin_left * 2, ViewGroup.LayoutParams.WRAP_CONTENT);
            popWin.setBackgroundDrawable(new BitmapDrawable());
            int lr = (int) mContext.getResources().getDimension(R.dimen.popwiew_padding_lr);
            int tb = (int) mContext.getResources().getDimension(R.dimen.popwiew_padding_tb);
            popview.setPadding(lr, tb, lr, (int) (tb * 1.5));
            popWin.setFocusable(true);//点击空白处时，隐藏掉pop窗口
            int measuredHeight = popview.getMeasuredHeight();

            popWin.setAnimationStyle(R.style.video_popwin_anim_lu);
            int popwin_offdis = (int) mContext.getResources().getDimension(R.dimen.popwin_offdis);
            int popwin_base = (int) mContext.getResources().getDimension(R.dimen.popwin_base);

            int offset = (int) mContext.getResources().getDimension(R.dimen.popwiew_offset_ru);
            popWin.showAtLocation(v, Gravity.NO_GRAVITY, popwin_margin_left,
                    location[1] - popwin_offdis * tags.size() - popwin_base - offset);
        } else {
            //按钮在屏幕上边，popwin在下边显示
            root_viewgroup.setBackgroundResource(R.mipmap.home_bg_rd);
            popWin = new PopupWindow(popview,
                    Config.displayWidth - popwin_margin_left * 2, ViewGroup.LayoutParams.WRAP_CONTENT);
            popWin.setBackgroundDrawable(new BitmapDrawable());
            int lr = (int) mContext.getResources().getDimension(R.dimen.popwiew_padding_lr);
            int tb = (int) mContext.getResources().getDimension(R.dimen.popwiew_padding_tb);
            popview.setPadding(lr, (int) (tb * 1.5), lr, tb);
            popWin.setFocusable(true);//点击空白处时，隐藏掉pop窗口
//                popview.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.homepop_show_md));
            popWin.setAnimationStyle(R.style.video_popwin_anim_ld);

            int offset = (int) mContext.getResources().getDimension(R.dimen.popwiew_offset);
            popWin.showAtLocation(v, Gravity.NO_GRAVITY, popwin_margin_left,
                    location[1] + v.getHeight() + offset);
        }
        popview.setFocusable(true);
        popview.setFocusableInTouchMode(true);
        popview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    popWin.dismiss();
                }
                return false;
            }
        });
        popview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popWin != null && popWin.isShowing()) {
                    popWin.dismiss();
                }
                return popview.onTouchEvent(event);
            }
        });

//        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
//
//        lp.alpha = 0.5f; //0.0-1.0
//        lp.gravity= Gravity.CENTER_HORIZONTAL;
//        mContext.getWindow().setAttributes(lp);
        EventBus.getDefault().post(new POPWindow(true));
        popWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popWin = null;
//                WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
//                lp.alpha = 1.0f;
//                mContext.getWindow().setAttributes(lp);
                EventBus.getDefault().post(new POPWindow(false));
            }
        });

//        TongJiUtil.getInstance().putEntries("home_reduce",
//                MyEntry.getIns("category_id", titleId),
//                MyEntry.getIns("news_id", split[1].toString()));
    }


    private void deleteCell(final NewsListAdapter itemAdapter, final View v, final int position) {
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                itemAdapter.getData().remove(position);
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        };

        Animation out = AnimationUtils.loadAnimation(mContext, R.anim.slide_left_out);
        if (al != null) {
            out.setAnimationListener(al);
        }
        out.setDuration(600);
        if (v != null) {

            v.startAnimation(out);
        }
    }

    //private void collapse(final View v, Animation.AnimationListener al) {
    //      final int initialHeight = v.getMeasuredHeight();

//        //Animation anim = new Animation() {
//            @Override
//            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                if (interpolatedTime == 1) {
//                    //v.setVisibility(View.GONE);
//                }
//                else {
//                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
//                    v.requestLayout();
//                }
//            }
//
//            @Override
//            public boolean willChangeBounds() {
//                return true;
//            }
//        };
    //}

    private void showDialog() {

        final View view = View.inflate(mContext, R.layout.never_suggest_dialog, null);

        ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        iv_icon.setImageResource(R.mipmap.home_reduce_push);

        final MyDialog2 dialog = new MyDialog2(mContext, view, R.style.dialog);
        dialog.getWindow().setWindowAnimations(R.style.clearcache_dismiss_animation);


        NewsApplication.getInstance().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1000);

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void submit(String post_id, StringBuffer des) {
        String str = "";
        if (des != null) {
            str = des.substring(0, des.toString().length());
            stringBuffer = null;
        } else {
            str = "";
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("post_id", post_id);
        map.put("description", str);
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.NEWS_ADDBLOCK, map, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {

            }

            @Override
            public void onObjError() {

            }
        });

    }


    //处理jsonArray，以适应阿拉伯的语言习惯
    private JSONArray changeArray(JSONArray tags) {
        if (tags.size() % 2 == 1) {
            //奇数个，增加一个空的
            Object o = tags.get(0);
            if (o instanceof String) {
                tags.add("");
            } else {
                tags.add(new JSONObject());
            }
        }
        //第奇数个和第偶数个交换位置
        Object objMid = new Object();
        for (int i = 0; i < tags.size(); i++) {
            Object o = tags.get(i);
            if (i % 2 == 0) {
                objMid = o;
            } else {
                tags.set(i - 1, o);
                tags.set(i, objMid);
            }

        }

        return tags;
    }


    @Override
    public void setTags(HashMap<Integer, String> map) {
        stringBuffer = new StringBuffer();
        for (int i = 0; i <= tags.size(); i++) {
            if (StringUtils.isNotEmpty(map.get(i))) {
                stringBuffer.append("," + map.get(i));
            }

        }
    }
}
