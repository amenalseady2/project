package com.onemena.widght;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.arabsada.news.R;
import com.onemena.app.config.SPKey;
import com.onemena.data.eventbus.POPWindow;
import com.onemena.utils.SpUtil;

import de.greenrobot.event.EventBus;

/**
 * Created by WHF on 2016-11-29.
 */


public class PopwinSettingstyle extends PopupWindow implements View.OnClickListener {

    private Activity mContext;
    private final View popView;

    private OnFontSizeChangListener changListener;
    public PopwinSettingstyle(Activity context) {

        mContext = context;
        popView = LayoutInflater.from(mContext).inflate(R.layout.fontstyle_setting_pop, null);
        initPopWindow();
        setContentView(popView);
    }
    private void initPopWindow() {
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        setBackgroundDrawable(new ColorDrawable(0));
        //被键盘顶起的两行代码
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //设置popwindow出现和消失动画
        setAnimationStyle(R.style.PopMenuAnimation);


        //获取popwindow焦点
        setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        setOutsideTouchable(true);
        update();
    }
    public void show(View parent) {

        //设置popwindow显示位置
        showAtLocation(parent, Gravity.CENTER | Gravity.BOTTOM, 0, 0);


        popView.findViewById(R.id.sty_1).setOnClickListener(this);
        popView.findViewById(R.id.sty_2).setOnClickListener(this);
        popView.findViewById(R.id.sty_3).setOnClickListener(this);
        popView.findViewById(R.id.sty_4).setOnClickListener(this);

//        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
//        lp.alpha = 0.5f; //0.0-1.0
//        lp.gravity = Gravity.CENTER_HORIZONTAL;
//        mContext.getWindow().setAttributes(lp);
//        setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
//                lp.alpha = 1.0f;
//                mContext.getWindow().setAttributes(lp);
//            }
//        });


        EventBus.getDefault().post(new POPWindow(true));
        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                EventBus.getDefault().post(new POPWindow(false));
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sty_1:
                changListener.setChangListener("SMALLER");
                SpUtil.saveValue(SPKey.WEBVIEW_SIZE, "SMALLER");
                this.dismiss();
                break;
            case R.id.sty_2:
                changListener.setChangListener("NORMAL");
                SpUtil.saveValue( SPKey.WEBVIEW_SIZE, "NORMAL");
                this.dismiss();
                break;
            case R.id.sty_3:
                changListener.setChangListener("LARGER");
                SpUtil.saveValue(SPKey.WEBVIEW_SIZE, "LARGER");
                this.dismiss();
                break;
            case R.id.sty_4:
                changListener.setChangListener("LARGEST");
                SpUtil.saveValue(SPKey.WEBVIEW_SIZE, "LARGEST");
                this.dismiss();
                break;
        }
    }

    public void setFontSizeChangListener(OnFontSizeChangListener changListener){
        this.changListener=changListener;
    }

    public interface OnFontSizeChangListener{
        void setChangListener(String style);
    }
}
