package com.onemena.widght;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 剪切图片控件
 * @author 杨生辉  E-mail: 79696368@qq.com
 * @version 创建时间：2015-5-13  上午10:11:19
 */
public class ClipImageLayout extends RelativeLayout
{

	private ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;
	private Bitmap bm;

	private Context mContext;
	/**
	 * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
	 */
//	private int mHorizontalPadding = 0;

	public ClipImageLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.mContext = context;
	}
	
	
	private void init(Context mContext){
		
		mZoomImageView = new ClipZoomImageView(mContext);
		mClipImageView = new ClipImageBorderView(mContext);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		
		/**
		 * 这里测试，直接写死了图片，真正使用过程中，可以提取为自定义属性
		 */
		mZoomImageView.setImageBitmap(bm);
		
		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);

		
		// 计算padding的px
//		mHorizontalPadding = (int) TypedValue.applyDimension(
//				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
//						.getDisplayMetrics());
//		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
//		mClipImageView.setHorizontalPadding(mHorizontalPadding);
		
	}
	
	public void setBitmap(Bitmap bt){
		this.bm = bt;
		init(mContext);
	}

	/**
	 * 对外公布设置边距的方法,单位为dp
	 * 
	 * @param mHorizontalPadding
	 */
//	public void setHorizontalPadding(int mHorizontalPadding)
//	{
//		this.mHorizontalPadding = mHorizontalPadding;
//	}

	/**
	 * 裁切图片
	 * 
	 * @return
	 */
	public String clip()
	{
		return mZoomImageView.clip();
	}


}
