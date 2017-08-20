package com.onemena.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;

/**
 * Created by Administrator on 2016/11/17.
 */

public class InterestGridViewAdapter extends BaseAdapter {

    private Context mContext;

    public InterestGridViewAdapter(Context context) {

        mContext = context;
    }

    @Override
    protected View getMyView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = ViewHolder.getViewHolder(mContext, convertView, parent,
                R.layout.item_interest_gridview, position);
        ImageView iv = holder.getView(R.id.iv);
        TextView tv = holder.getView(R.id.tv);

        JSONObject object = data.getJSONObject(position);

        tv.setText(object.getString("name"));
        String select = object.getString("select");
        if ("0".equals(select)) {
            //没有选中
            iv.setImageResource(Integer.parseInt(object.getString("imgdef")));
            tv.setTextColor(Color.parseColor("#8E9AAA"));
        } else {
            //选中了
            iv.setImageResource(Integer.parseInt(object.getString("imgsel")));
            tv.setTextColor(Color.parseColor("#3E84E0"));
        }
        iv.setTag(position);
        iv.setOnClickListener(mListener);

        return holder.getConverView();
    }
}
