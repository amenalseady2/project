package com.onemena.widght;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author 张玉水
 * @date 创建时间：2016-6-11 上午11:47:59
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class AutoFixImageView extends ImageView {

	private int defaultWidth;
	private int defaultHeight;
	private double scale=400.0/710.0;

	public AutoFixImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AutoFixImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoFixImageView(Context context) {
		super(context);
	}

//	@Override
//	protected void onDraw(Canvas canvas) {
//		
//		measure(0, 0);
//		defaultWidth=this.getMeasuredWidth();
//		defaultHeight=Math.round(defaultWidth * scale);
//
//		LayoutParams params = this.getLayoutParams();
//		params.width = defaultWidth;
//		params.height = defaultHeight;
//		this.setLayoutParams(params);
//		super.onDraw(canvas);
//	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		 int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) Math.round(width*scale+0.5f);
        setMeasuredDimension(width,height);
	}
}
