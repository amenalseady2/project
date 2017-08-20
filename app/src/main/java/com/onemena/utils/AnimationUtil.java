package com.onemena.utils;


import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;

public class AnimationUtil {

	private static final int TRANSLATE_DURATION = 200;
	private static final int ALPHA_DURATION = 300;
	
	public static Animation createTranslationInAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				1, type, 0);
		an.setDuration(TRANSLATE_DURATION);
		return an;
	}

	public static Animation createAlphaInAnimation() {
		AlphaAnimation an = new AlphaAnimation(0, 1);
		an.setDuration(ALPHA_DURATION);
		return an;
	}

	public static Animation createTranslationOutAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				0, type, 1);
		an.setDuration(TRANSLATE_DURATION);
		an.setFillAfter(true);
		return an;
	}

	public static Animation createAlphaOutAnimation() {
		AlphaAnimation an = new AlphaAnimation(1, 0);
		an.setDuration(ALPHA_DURATION);
		an.setFillAfter(true);
		return an;
	}
	
	public static Animation applyRotation(int position, float start, float end,float centerX,float centerY,float deep,boolean reverse,boolean direction) {  
        // 计算中心点  
        // Create a new 3D rotation with the supplied parameter  
        // The animation listener is used to trigger the next animation  
        final Animation rotation =  
                new Rotate3dAnimation(start, end, centerX, centerY, deep, reverse,direction);  
        rotation.setDuration(400);  
        rotation.setFillAfter(true);  
        //加速动画插入器
        rotation.setInterpolator(new AccelerateInterpolator());  
        
        return rotation;
    }
	
	public static void zoomContainer(View view){
		float centerX = view.getWidth() / 2.0f;
		float centerY = (float) Math.sqrt(Math.sqrt(view.getHeight()
				* view.getHeight() - view.getHeight()
				* view.getHeight() / 4.0d));
		Animation animation = (Rotate3dAnimation) AnimationUtil
				.applyRotation(0, 0, 0, centerX, centerY, 160.0f, true,
						true);
		view.startAnimation(animation);
	}
	
	
	
	public static void ShowUp(float fromYDelta,View view){
		TranslateAnimation anim = new TranslateAnimation(0,
				0, fromYDelta,
				0);
		anim.setFillAfter(true);
		anim.setDuration(250);
		view.startAnimation(anim);
	}
	
	public static void showDown(float toYDelta,View view){
		TranslateAnimation anim = new TranslateAnimation(0,
				0, 0,
				toYDelta);
		anim.setFillAfter(true);
		anim.setDuration(600);
		anim.setAnimationListener(new AnimationListener() {

			// @Override
			public void onAnimationStart(Animation animation) {
				
			}

			// @Override
			public void onAnimationRepeat(Animation animation) {
			}

			// @Override
			public void onAnimationEnd(Animation animation) {
				
			}
		});
		view.startAnimation(anim);
	}
	
	/**
	 * PopupWindow关闭时动画以及关闭操作
	 */
	public static void popupCloseAnimation(View view,int height,final PopupWindow popup){		
		TranslateAnimation tran = new TranslateAnimation(0,0,0,height);
		tran.setFillAfter(true);
		tran.setDuration(600);
		view.startAnimation(tran);
		tran.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {						
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}	
			@Override
			public void onAnimationEnd(Animation animation) {
				if(popup != null){
					popup.dismiss();
				}
			}
		});	
	}
	
	public static void popupCloseAnimation(View view,int height,final View parentView){		
		TranslateAnimation tran = new TranslateAnimation(0,0,0,height);
		tran.setFillAfter(true);
		tran.setDuration(600);
		view.startAnimation(tran);
		tran.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {						
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}	
			@Override
			public void onAnimationEnd(Animation animation) {
				if(parentView != null){
					parentView.setVisibility(View.GONE);
				}
			}
		});	
	}
	
	public static void doAlphaAnimation(float begin,float end,int duration,View view){
		Animation alphaAnimation = new AlphaAnimation(begin, end);
		alphaAnimation.setFillAfter(true);
		alphaAnimation.setDuration(duration);
		view.startAnimation(alphaAnimation);
	}
	
}
