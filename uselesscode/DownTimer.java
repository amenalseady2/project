package com.onemena.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by zhaojie on 2016/6/11.
 */
public class DownTimer extends CountDownTimer {
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */

    public TextView mView;

    public DownTimer(TextView view, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mView = view;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mView.setClickable(false);
        mView.setText("已发送"+millisUntilFinished/1000 + "s");
    }

    @Override
    public void onFinish() {
        mView.setClickable(true);
        mView.setText("重新发送");
    }
}
