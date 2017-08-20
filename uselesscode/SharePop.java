package com.mysada.news.activity;

import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mysada.news.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by whf on 2016/11/07.
 */
public class SharePop{
    public Context context;
    public View ib;
    private GridView mSharedList;
    private TextView cancelButton;
    private SimpleAdapter saImageItems;
    private int[] image = {R.mipmap.details_more_twitter, R.mipmap.details_more_google, R.mipmap.details_more_fb
            , R.mipmap.details_more_wa,R.mipmap.ic_transparent, R.mipmap.details_more_cl, R.mipmap.details_more_sms,
    R.mipmap.details_more_email};
    private String[] name = {"Twitter", "Google+","Facebook", "WhatsApp","","نسخ الرابط","SMS","Email"};
    private final List<HashMap<String, Object>> shareList;
    private PopupWindow popupWindow;
    public SharePop(Context context, View ib,int type) {
        if (type==1){
            name= new String[]{"","","Facebook", "Google+"};
            image=new int[]{0,0,R.mipmap.details_more_fb, R.mipmap.details_more_google};
        }
        this.context = context;
        this.ib = ib;
        final View contentView = LayoutInflater.from(context).inflate(
                R.layout.popupwindow_share, null);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        popupWindow=new PopupWindow(contentView);


        // 计算这个PopupWindow的显示位置
        int[] screen_pos = new int[2];
        ib.getLocationOnScreen(screen_pos);
        Rect anchor_rect = new Rect(screen_pos[0], screen_pos[1], screen_pos[0] + ib.getWidth(), screen_pos[1] + ib.getHeight());
        int contentViewWidth = contentView.getMeasuredWidth();
        int contentViewHeight = contentView.getMeasuredHeight();
        int position_x = anchor_rect.centerX() - (contentViewWidth / 2);
        int position_y = anchor_rect.bottom - (anchor_rect.height() / 2);
        // 设置好参数之后再show
//        popupWindow.showAsDropDown(contentView);
        popupWindow.setAnimationStyle(R.style.PopMenuAnimation);
        popupWindow.showAtLocation(ib, Gravity.BOTTOM, position_x, contentViewHeight);
        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);
        contentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
                    popupWindow.dismiss();
                }
                return false;
            }
        });
//        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                    popupWindow.dismiss();
//                }
//                // 这个地方一定要返回false,你可能会说这里已经消费掉事件了啊应该返回true,参看PopupWindow的私有类PopupViewContainer
//                // 弹出的View都是交由这个类来管理的，因为它对onTouchEvent()这个回调方法已经提供了一些默认实现,当你是返回true,事件就在此被拦截
//                // 不会再继续派发了,所以onTouchEvent()无法被调用
//                return false;
//            }
//        });


        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {

                    popupWindow.dismiss();

                    popupWindow = null;

                }

                return contentView.onTouchEvent(event);
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
//        popupWindow.setContentView(contentView);

        mSharedList = (GridView) contentView.findViewById(R.id.listview);
        cancelButton = (TextView) contentView.findViewById(R.id.actionbar_back);
        shareList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < image.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", image[i]);//添加图像资源的ID
            map.put("ItemText", name[i]);//按序号做ItemText
            shareList.add(map);
        }
        saImageItems = new SimpleAdapter(context, shareList, R.layout.item_shareitem, new String[]{"ItemImage", "ItemText"}, new int[]{R.id.share_icon, R.id.share_name});
        mSharedList.setAdapter(saImageItems);
    }
    public void setCancelButtonOnClickListener(View.OnClickListener Listener) {
        cancelButton.setOnClickListener(Listener);
    }
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mSharedList.setOnItemClickListener(listener);
    }

    public PopupWindow getPopupWindow(){
        return popupWindow;
    }
    /**
     * 关闭对话框
     */
    public void dismiss() {
        popupWindow.dismiss();
    }
}
