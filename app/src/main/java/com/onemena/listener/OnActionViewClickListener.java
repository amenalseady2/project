package com.onemena.listener;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * ActionView视图可响应事件, ActionView Click事件监听
 * @author wcy
 *
 */
public interface OnActionViewClickListener extends OnClickListener {

	@Override
	public void onClick(View v);

}
