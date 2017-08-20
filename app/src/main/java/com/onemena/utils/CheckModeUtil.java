package com.onemena.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.onemena.app.config.SPKey;

/**
 * 吐司
 * @author yangshenghui
 *
 */
public class CheckModeUtil {

	public static void changeModeTextColor(Context context, int dayId, int nightId, TextView... textViews){
		if (SpUtil.getBoolean(SPKey.MODE,false)) {
			for (TextView textView : textViews) {
				textView.setTextColor(context.getResources().getColor(nightId));
			}
		}else {
			for (TextView textView : textViews) {
				textView.setTextColor(context.getResources().getColor(dayId));
			}
		}
	}

	public static void changeModeTextColor(Context context, int dayId, int nightId, TextView textView1,TextView textView2){
		if (SpUtil.getBoolean(SPKey.MODE,false)) {
				textView1.setTextColor(context.getResources().getColor(nightId));
				textView2.setBackgroundColor(context.getResources().getColor(nightId));
		}else {
				textView1.setTextColor(context.getResources().getColor(dayId));
				textView2.setBackgroundColor(context.getResources().getColor(dayId));
		}
	}

	public static void changeModeText(Context context,int daytxt, int nighttxt, TextView... textViews){
		if (SpUtil.getBoolean(SPKey.MODE,false)) {
			for (TextView textView : textViews) {
				textView.setText(context.getResources().getString(nighttxt));
			}
		}else {
			for (TextView textView : textViews) {
				textView.setText(context.getResources().getString(daytxt));
			}
		}
	}
	public static void changeModeImage(int dayId, int nightId, ImageView imageView){
		if (SpUtil.getBoolean(SPKey.MODE,false)) {
			imageView.setImageResource(nightId);

		}else {
			imageView.setImageResource(dayId);
		}
	}
	public static void changeModeBackgroud(Context context,int dayId, int nightId, View... views){
		if (SpUtil.getBoolean(SPKey.MODE,false)) {
			for (View view : views) {
				view.setBackgroundColor(context.getResources().getColor(nightId));
			}
		}else {
			for (View view : views) {
				view.setBackgroundColor(context.getResources().getColor(dayId));
			}
		}
	}

}
