package com.tabindictor.viewpagertabindictor;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by Administrator on 2016/11/3.
 */

public class NewContentTextView extends TextView {
    private Context mContext;

    public NewContentTextView(Context context) {
        this(context,null);
        this.mContext = context;
        init();
    }

    public NewContentTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
//        this.attr_textAppearance = ViewAttributeUtil.getTextApperanceAttribute(attrs);
    }

    public NewContentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        this.attr_textAppearance = ViewAttributeUtil.getTextApperanceAttribute(attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        AssetManager mgr=mContext.getAssets();//得到AssetManager
        Typeface tf=Typeface.createFromAsset(mgr, "fonts/HelveticaNeueLTArabic-Roman.ttf");//根据路径得到Typeface
        setTypeface(tf);//设置字体
    }


}
