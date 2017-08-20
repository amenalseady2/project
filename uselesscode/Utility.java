package com.onemena.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;

public class Utility {
	public static final int DURATION = Toast.LENGTH_SHORT;

	public static void showToast(Context context, int resId) {
		Toast toast = Toast.makeText(context, resId, DURATION);
		toast.show();
	}

	public static void closeKeybord(View mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}

	public static void getFocus(View view) {
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
	}

	public static void openKeybord(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
	  /**   
     * 返回当前程序版本名   
     */    
    public final static String getAppVersionName(Context context) {     
        String versionName = "";     
        try {     
            // ---get the package info---     
            PackageManager pm = context.getPackageManager();     
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);     
            versionName = pi.versionName;     
            if (versionName == null || versionName.length() <= 0) {     
                return "";     
            }     
        } catch (Exception e) {     
            
        }     
        return versionName;     
    }
    
    
    public final static int getAppVersionCode(Context context) {     
        int versionCode = 1;     
        try {     
            // ---get the package info---     
            PackageManager pm = context.getPackageManager();     
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);     
            versionCode = pi.versionCode;     
                
        } catch (Exception e) {     
            
        }     
        return versionCode;     
    }
    
    public static String getFromAssets(Context context,String fileName){  
        String result = "";  
            try {  
                InputStream in = context.getResources().getAssets().open(fileName);  
                //获取文件的字节数  
                int lenght = in.available();  
                //创建byte数组  
                byte[]  buffer = new byte[lenght];  
                //将文件中的数据读到byte数组中  
                in.read(buffer);  
                result = new String(buffer);
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
            return result;  
    }  
    
    public static void setListViewHeightBasedOnChildren(ListView listView) {  
            //获取ListView对应的Adapter  
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            // pre-condition  
            return;  
        }  
  
        int totalHeight = 0;  
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  //计算子项View 的宽高  
            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度  
        }  
  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        //listView.getDividerHeight()获取子项间分隔符占用的高度  
        //params.height最后得到整个ListView完整显示需要的高度  
        listView.setLayoutParams(params);  
    }  
      
    
    public static void makeCall(Context context, String phoneNum) {

		if (!TextUtils.isEmpty(phoneNum)) {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ phoneNum));
			context.startActivity(intent);
		}

	}

}
