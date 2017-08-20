package com.onemena.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.arabsada.news.R;
import com.onemena.app.config.SPKey;
import com.onemena.utils.SpUtil;
import com.onemena.widght.HelveRomanTextView;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/11/17.
 */

public class HomePopwinGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private AddTagListener listener;
    private HashMap<Integer,String> map=new HashMap<Integer, String>();

    public HomePopwinGridViewAdapter(Context context) {

        mContext = context;
    }

    @Override
    protected View getMyView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = ViewHolder.getViewHolder(mContext, convertView, parent,
                R.layout.item_home_pop_gridview, position);
        final HelveRomanTextView gridview_tv = holder.getView(R.id.gridview_tv);
        final String name = data.getString(position);

        if (TextUtils.isEmpty(name)) {
            gridview_tv.setVisibility(View.INVISIBLE);
        } else {
            gridview_tv.setText(name);
            gridview_tv.setVisibility(View.VISIBLE);
        }

        final Boolean mode = SpUtil.getBoolean(SPKey.MODE, false);
        if (mode) {
            //夜间模式
            gridview_tv.setTextColor(mContext.getResources().getColor(R.color.gridview_item_color_night_selector));
            gridview_tv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.gridview_item_night_selector));
        } else {
            gridview_tv.setTextColor(mContext.getResources().getColor(R.color.gridview_item_color_selector));
            gridview_tv.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.gridview_item_selector));

        }

        gridview_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mode){
                    gridview_tv.setSelected(!gridview_tv.isSelected());
                    if (gridview_tv.isSelected()) {
                        map.put(position,name);
                        gridview_tv.setTextColor(Color.parseColor("#935656"));
                    } else {
                        map.remove(position);
                        gridview_tv.setTextColor(Color.parseColor("#707070"));

                    }
                }else {
                    gridview_tv.setSelected(!gridview_tv.isSelected());
                    if (gridview_tv.isSelected()) {
                        map.put(position,name);
                        gridview_tv.setTextColor(Color.parseColor("#EB4E35"));
                    } else {
                        map.remove(position);
                        gridview_tv.setTextColor(Color.parseColor("#64697B"));

                    }
                }

                listener.setTags(map);
            }
        });

        return holder.getConverView();

    }

    public void SetAddTagListener(AddTagListener listener){
        this.listener=listener;
    }

    public interface AddTagListener{
        void setTags(HashMap<Integer,String> map);
    }
}
