package com.onemena.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mysada.news.R;


/**得到listview的高，设置给外层的viewpager。解决scrollview与listview的冲突问题
 * 作者:  张玉水
 * 日期: 14-9-30
 * 时间: 14:28
 * <strong>软件中所有与布局相关的工具类</strong>
 * <p></p>
 */
public class ViewUtil {
  public static void setListViewHeightBasedOnChildren(ListView listView) {
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
      return;
    }

    int totalHeight = 0;
    for (int i = 0; i < listAdapter.getCount(); i++) {
      View listItem = listAdapter.getView(i, null, listView);
      listItem.measure(0, 0);
      totalHeight += listItem.getMeasuredHeight();
    }

    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    listView.setLayoutParams(params);
  }

  /**
   * 获取Listview的高度，然后设置ViewPager的高度
   * @param listView
   * @return
   */
  public static int setListViewHeightBasedOnChildren1(ListView listView) {
    //获取ListView对应的Adapter
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
      // pre-condition
      return 0;
    }

    int totalHeight = 0;
    for (int i = 0, len = listAdapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
      View listItem = listAdapter.getView(i, null, listView);
      listItem.measure(0, 0); //计算子项View 的宽高
      totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
    }

    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    //listView.getDividerHeight()获取子项间分隔符占用的高度
    //params.height最后得到整个ListView完整显示需要的高度
    listView.setLayoutParams(params);
    return params.height;
  }

  /**
   * Tab 底部下划线切换
   * @param textView 所用控件
   * @param context 窗体
   * @param layout_tab 布局文件
     */
  public static void setTabSelected(TextView textView, Context context, ViewGroup layout_tab) {
    Drawable selectedDrawable = ResourceReader.readDrawable(context, R.drawable.shape_nav_indicator);
    int screenWidth = DensityUtils.getScreenSize(context)[0];
    int right = screenWidth / 10;
    selectedDrawable.setBounds(0, 0, right, DensityUtils.dipTopx(context, 3));
    textView.setSelected(true);
    textView.setCompoundDrawables(null, null, null, selectedDrawable);
    int size = layout_tab.getChildCount();
    for (int i = 0; i < size; i++) {
      if (textView.getId() != layout_tab.getChildAt(i).getId()) {
        layout_tab.getChildAt(i).setSelected(false);
        ((TextView) layout_tab.getChildAt(i)).setCompoundDrawables(null, null, null, null);
      }
    }
  }

  public static int dip2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

}