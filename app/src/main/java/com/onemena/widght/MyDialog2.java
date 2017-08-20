package com.onemena.widght;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/6/21.
 */
public class MyDialog2 extends Dialog {

    private static int default_width = 560; //默认宽度
    private static int default_height = 560;//默认高度

    public MyDialog2(Context context, View layout, int style) {
        this(context, default_width, default_height, layout, style);
    }

    public MyDialog2(Context context, int width, int height, View layout, int style) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

}