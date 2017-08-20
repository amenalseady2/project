package com.onemena.utils;

import android.widget.Toast;

import com.onemena.app.NewsApplication;
import com.onemena.app.manager.UtilsManager;


/**
 * 吐司
 * @author yangshenghui
 *
 */
public class ToastUtil {

	public static void showDevShortToast(CharSequence content) {
		if (UtilsManager.DEBUG_MESSSAGE) {
			Toast.makeText(NewsApplication.getInstance(), content, Toast.LENGTH_SHORT).show();
		}
	}

	public static void showNormalShortToast(CharSequence content) {
		Toast.makeText(NewsApplication.getInstance(), content, Toast.LENGTH_SHORT).show();
	}

	public static void showNormalLongToast( CharSequence content) {
		Toast.makeText(NewsApplication.getInstance(), content, Toast.LENGTH_LONG).show();
	}

	public static void showNormalShortToast( int resid) {
		Toast.makeText(NewsApplication.getInstance(), resid, Toast.LENGTH_SHORT).show();
	}

	public static void showNormalLongToast(int resid) {
		Toast.makeText(NewsApplication.getInstance(), resid, Toast.LENGTH_LONG).show();
	}
	


}
