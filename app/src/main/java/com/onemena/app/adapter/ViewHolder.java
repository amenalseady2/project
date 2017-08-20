package com.onemena.app.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 万能ViewHolder
 * @author yangshenghui
 */
public class ViewHolder {

	private SparseArray<View> mViews;
 
	private int mPosition;
	private View mConverView;

	private ViewHolder(Context context, ViewGroup parent, int layoutId,
					   int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConverView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mConverView.setTag(this);
	}

	public static ViewHolder getViewHolder(Context context, View convertView,
										   ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}
	}

	public View getConverView() {
		return mConverView;
	}

	/**
	 * 通过viewid获取view
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConverView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}
}
