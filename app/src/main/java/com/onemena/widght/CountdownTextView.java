package com.onemena.widght;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * CountdownTextView Created by voler on 2017/5/17.
 * 说明：
 */

public class CountdownTextView extends TextView {

    // 画圆所在的距形区域
    private RectF mRectF;

    private Paint mPaint;

    private Context mContext;

    private float strokeWidth;
    private float animatedValue;
    private float cx;
    private float cy;
    private float radius;


    public CountdownTextView(Context context) {
        this(context, null);
    }

    public CountdownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }


    private void init() {
        mRectF = new RectF();
        mPaint = new Paint();
        strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.5f, mContext.getResources().getDisplayMetrics());
        // 设置画笔相关属性
        mPaint.setAntiAlias(true);

        AssetManager mgr=mContext.getAssets();
        Typeface tf= Typeface.createFromAsset(mgr, "fonts/NotoNaskhArabic-Regular.ttf");
        setTypeface(tf);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getWidth();
        int height = getHeight();
        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }
        cx = cy = width / 2;
        radius = width / 2 - strokeWidth;

        // 位置
        mRectF.left = strokeWidth / 2; // 左上角x
        mRectF.top = strokeWidth / 2; // 左上角y
        mRectF.right = width - strokeWidth / 2; // 左下角x
        mRectF.bottom = height - strokeWidth / 2; // 右下角y
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setARGB((int) (0xff * 0.7), 43, 43, 43);
        canvas.drawCircle(cx, cy, radius, mPaint);


        mPaint.setColor(0xfff80809);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        // 绘制圆圈，进度
        canvas.drawArc(mRectF, -90 - animatedValue, animatedValue, false, mPaint);

        super.onDraw(canvas);
    }

    public void start() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(360, 0);
        valueAnimator.addUpdateListener((animator) -> {
            animatedValue = (float) animator.getAnimatedValue();
            invalidate();
        });
        valueAnimator.setDuration(3000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }
}
