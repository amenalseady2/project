package com.mysada.news.net;

import android.text.TextUtils;


/**
 * 工具类，由于图片加载尺寸过大容易内存溢出，在此做图片缩放（已废弃）
 * @author 张玉水  E-mail: 79696368@qq.com
 * @version 创建时间：2015-5-14  上午10:12:47
 */
public class RetUrlSizeUtils {

	
	/**
	 * 首页经验列表图片尺寸
	 * @author 杨生辉  E-mail: 79696368@qq.com
	 * @version 创建时间：2015-5-14  上午10:14:20
	 * @return
	 */
	public static String getResetUrl(int width , int height, String url , int multiple){
		
		String retSizeUrl = "";
		synchronized (RetUrlSizeUtils.class) {
			
		
			if(width == 0)
				return "default";
			if(TextUtils.isEmpty(url))
				return "default";
			
			
			retSizeUrl = url + "?imageView2/1/w/" + width/multiple + "/h/" + height/multiple
					+  "/interlace/1";
		}
		return retSizeUrl;
		
	}
	
	
	
	
}
