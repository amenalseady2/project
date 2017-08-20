package com.onemena.widght.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.arabsada.news.R;
import com.onemena.widght.HelveRomanTextView;


/**
 */
public class CancelFollowDialog extends Dialog {

    HelveRomanTextView tvCancel;
    HelveRomanTextView tvConfirm;
    private TextView text;
    private LayoutParams layoutParams;

    public CancelFollowDialog(Context context) {
        super(context, R.style.Dialog);
        this.setContentView(R.layout.view_dialog_cancel);
        layoutParams = this.getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.dimAmount = 0.3f; // 去背景遮盖
        layoutParams.alpha = 1.0f;
        this.getWindow().setAttributes(layoutParams);
        text = (TextView) findViewById(R.id.tv_not_follow_name);
        tvCancel = (HelveRomanTextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            dismiss();
        }
    }

    public void setOnConfirmListener(View.OnClickListener listener) {
        findViewById(R.id.tv_confirm).setOnClickListener(listener);
    }

    public void setCancelListener(View.OnClickListener listener) {
        tvCancel.setOnClickListener(listener);
    }

    /**
     */
    public void setText(String content) {
        text.setText(content);
    }

    /**
     */
    public void setText(int titleResid) {
        text.setText(titleResid);
    }

    /**
     * 设置文字颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        text.setTextColor(color);
    }

    /**
     * 设置背景变暗的程度,默认透明
     * 1.0表示完全不透明，0.0表示没有变暗。
     *
     * @param dimAmount
     */
    public void setDimAmount(float dimAmount) {
        layoutParams = this.getWindow().getAttributes();
        layoutParams.dimAmount = dimAmount;
        this.getWindow().setAttributes(layoutParams);
    }

}
