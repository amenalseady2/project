package com.onemena.listener;

import com.alibaba.fastjson.JSONObject;

/**
 * 通用级联菜单接口
 * @author yangshenghui
 */
public interface LeftMenuViewOnSelectListener {
	public void getValue(JSONObject menuItem);
}
