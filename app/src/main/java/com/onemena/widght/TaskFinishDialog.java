package com.onemena.widght;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.arabsada.news.R;


/**
 * 加载等待动画
 * @ClassName:LoadingDialog.java
 * @Description:TODO 类说明
 * @author:wang
 * @email:super0086@qq.com
 * @version:V1.0  
 * @Date:2014年12月2日 下午1:51:32
 */
public class TaskFinishDialog extends Dialog {

	private TextView loadtext;
	private LayoutParams layoutParams;
	public TaskFinishDialog(Context context) {
		super(context, R.style.Dialog);
		this.setContentView(R.layout.view_dialog_task_finish);
		layoutParams = this.getWindow().getAttributes();
		layoutParams.gravity = Gravity.CENTER;
		layoutParams.dimAmount = 0; // 去背景遮盖
		layoutParams.alpha = 1.0f;
		this.getWindow().setAttributes(layoutParams);
		loadtext = (TextView) findViewById(R.id.tv_msg);
	}
	
	@Override  
    public void onWindowFocusChanged(boolean hasFocus) {  
        if (!hasFocus) {
            dismiss();  
        }  
    }

	@Override
	public void show() {
		super.show();
		loadtext.postDelayed(new Runnable() {
			@Override
			public void run() {
				dismiss();
			}
		},2500);
	}

	/**
	 * @author: wang
	 * @email:super0086@qq.com
	 * @date： 2014年12月2日 下午1:46:16
	 * @Description: 描述作用
	 * @return 返回值
	 */
	public void setLoadText(String content){
		loadtext.setText(content);
	}

	/**
	 * @author: wang
	 * @email:super0086@qq.com
	 * @date： 2014年12月2日 下午1:46:36
	 * @Description: 描述作用
	 * @return 返回值
	 */
	public void setLoadText(int titleResid) {
		loadtext.setText(titleResid);
	}
	
	/**
	 * 设置文字颜色
	 * @param color
	 */
	public void setLoadTextColor(int color) {
		loadtext.setTextColor(color);
	}
	
	/**
	 * 设置背景变暗的程度,默认透明
	 * 1.0表示完全不透明，0.0表示没有变暗。
	 * @param dimAmount
	 */
	public void setDimAmount(float dimAmount) {
		layoutParams = this.getWindow().getAttributes();
		layoutParams.dimAmount = dimAmount;
		this.getWindow().setAttributes(layoutParams);
	}
	
}
