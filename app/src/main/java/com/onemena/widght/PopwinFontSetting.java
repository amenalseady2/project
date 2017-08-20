package com.onemena.widght;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.arabsada.news.R;
import com.onemena.app.config.Config;
import com.onemena.app.config.SPKey;
import com.onemena.data.eventbus.POPWindow;
import com.onemena.utils.SpUtil;

import de.greenrobot.event.EventBus;

/**
 * Created by WHF on 2016-11-29.
 */


public class PopwinFontSetting extends PopupWindow implements View.OnClickListener {

    private Activity mContext;
    private final View popView;

    private OnFontSizeChangListener changListener;
    private RadioButton radioBtn1, radioBtn2, radioBtn3, radioBtn4;
    private RadioGroup radio_group;

    public PopwinFontSetting(Activity context) {

        mContext = context;
        if (SpUtil.getBoolean(SPKey.MODE,false)){

            popView = LayoutInflater.from(mContext).inflate(R.layout.fontstyle_setting_pop_night, null);
        }else {
            popView = LayoutInflater.from(mContext).inflate(R.layout.fontstyle_setting_pop, null);
        }
        initPopWindow();
        setContentView(popView);
    }
    private void initPopWindow() {
        setWidth(Config.displayWidth-100);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        setBackgroundDrawable(new ColorDrawable(0));

        //设置popwindow出现和消失动画
//        setAnimationStyle(R.style.PopMenuAnimation);


        //获取popwindow焦点
        setFocusable(true);
        //设置popwindow如果点击外面区域，便关闭。
        setOutsideTouchable(true);
        update();

        RadioGroup radio_group = (RadioGroup) popView.findViewById(R.id.radio_group);


    }
    public void show(View parent) {
        final AssetManager mgr=mContext.getAssets();//得到AssetManager
        Typeface tf=Typeface.createFromAsset(mgr, "fonts/HelveticaNeueLTArabic-Roman.ttf");//根据路径得到Typeface

        //设置popwindow显示位置
        showAtLocation(parent, Gravity.CENTER, 0, 0);

        radio_group = (RadioGroup) popView.findViewById(R.id.radio_group);

        radioBtn1 = (RadioButton) popView.findViewById(R.id.sty_1);
        radioBtn2 = (RadioButton) popView.findViewById(R.id.sty_2);
        radioBtn3 = (RadioButton) popView.findViewById(R.id.sty_3);
        radioBtn4 = (RadioButton) popView.findViewById(R.id.sty_4);

        radioBtn1.setTypeface(tf);//设置字体
        radioBtn2.setTypeface(tf);//设置字体
        radioBtn3.setTypeface(tf);//设置字体
        radioBtn4.setTypeface(tf);//设置字体
        popView.findViewById(R.id.tv_ok).setOnClickListener(this);

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


        String fontSize = SpUtil.getString(SPKey.WEBVIEW_SIZE, "صغير");
        switch (fontSize){
            case "صغير":
                radioBtn1.setChecked(true);
                radioBtn1.setTextColor(mContext.getResources().getColor(R.color.main_bule));
                break;
            case "طبيعي":
                radioBtn2.setChecked(true);
                radioBtn2.setTextColor(mContext.getResources().getColor(R.color.main_bule));
                break;
            case "كبير":
                radioBtn3.setChecked(true);
                radioBtn3.setTextColor(mContext.getResources().getColor(R.color.main_bule));
                break;
            case "الأكبر":
                radioBtn4.setChecked(true);
                radioBtn4.setTextColor(mContext.getResources().getColor(R.color.main_bule));
                break;
        }
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.sty_1:
//                        TongJiUtil.getInstance().putEntries("uc_font", MyEntry.getIns("fontsize","小1"));
                        changListener.setChangListener("صغير");
                        SpUtil.saveValue(SPKey.WEBVIEW_SIZE, "صغير");
                        SpUtil.saveValue( SPKey.TITLE_SIZE, 22);
                        dismiss();
                        break;
                    case R.id.sty_2:
//                        TongJiUtil.getInstance().putEntries("uc_font", MyEntry.getIns("fontsize","中2"));
                        changListener.setChangListener("طبيعي");
                        SpUtil.saveValue( SPKey.WEBVIEW_SIZE, "طبيعي");
                        SpUtil.saveValue( SPKey.TITLE_SIZE, 24);
                        dismiss();
                        break;
                    case R.id.sty_3:
//                        TongJiUtil.getInstance().putEntries("uc_font", MyEntry.getIns("fontsize","大3"));
                        changListener.setChangListener("كبير");
                        SpUtil.saveValue( SPKey.WEBVIEW_SIZE, "كبير");
                        SpUtil.saveValue( SPKey.TITLE_SIZE, 27);
                        dismiss();
                        break;
                    case R.id.sty_4:
//                        TongJiUtil.getInstance().putEntries("uc_font", MyEntry.getIns("fontsize","特大4"));
                        changListener.setChangListener("الأكبر");
                        SpUtil.saveValue(SPKey.WEBVIEW_SIZE, "الأكبر");
                        SpUtil.saveValue( SPKey.TITLE_SIZE, 29);
                        dismiss();
                        break;

                }
            }
        });

    }



    public void setFontSizeChangListener(OnFontSizeChangListener changListener){
        this.changListener=changListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                dismiss();
                break;
        }
    }

    public interface OnFontSizeChangListener{
        void setChangListener(String style);
    }
}
