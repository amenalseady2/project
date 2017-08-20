package com.onemena.widght;

import android.content.Context;
import android.text.Html;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arabsada.news.R;

/**
 * Created by Administrator on 2016/11/17.
 */

public class MyArabicTextview extends LinearLayout {
    private Context mContext;
    private TextView tv;

    public MyArabicTextview(Context context) {
        this(context, null);
    }

    public MyArabicTextview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyArabicTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        tv.addTextChangedListener(textWatcher);
    }

    public int getLineCount() {
        return tv.getLineCount();
    }

    private void init() {
        View view = View.inflate(mContext, R.layout.my_arabic_textview, null);
        tv = (TextView) view.findViewById(R.id.tv);
        addView(view);

    }


    public void setText(String Text) {
        tv.setText(Html.fromHtml(Text));
    }

    public void setTextColor(int color) {
        tv.setTextColor(color);
    }

    public void setMaxLines(int maxLines) {
        tv.setMaxLines(maxLines);
    }

}
