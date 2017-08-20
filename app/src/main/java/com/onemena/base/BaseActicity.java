package com.onemena.base;


import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import com.trello.rxlifecycle.components.support.RxFragmentActivity;


/**
 * 基本父类
 */
public abstract class BaseActicity extends RxFragmentActivity {


	//不受系统字体影响的代码。
	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		Configuration config=new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config,res.getDisplayMetrics() );
		return res;
	}


	/**
	 * Activity被回收时，不记录fragment的状态
	 * @param outState
	 */
	protected void onSaveInstanceState(Bundle outState) {

	}

}
