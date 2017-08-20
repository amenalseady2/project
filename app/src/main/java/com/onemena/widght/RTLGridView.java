package com.onemena.widght;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2017/1/6.
 */

public class RTLGridView extends GridView {

    public RTLGridView(Context context) {
        super(context);
    }

    public RTLGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RTLGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public int getLayoutDirection() {
        return LAYOUT_DIRECTION_RTL;
    }

}
