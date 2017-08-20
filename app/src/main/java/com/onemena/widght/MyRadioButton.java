package com.onemena.widght;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by WHF on 2016-12-10.
 */

public class MyRadioButton extends RadioButton {
    private Context mContext;

    public MyRadioButton(Context context) {
        this(context,null);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
//        this.attr_textAppearance = ViewAttributeUtil.getTextApperanceAttribute(attrs);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        this.attr_textAppearance = ViewAttributeUtil.getTextApperanceAttribute(attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        AssetManager mgr=mContext.getAssets();//得到AssetManager
        Typeface tf=Typeface.createFromAsset(mgr, "fonts/jazeera-light.ttf");//根据路径得到Typeface
        setTypeface(tf);//设置字体
    }
}
