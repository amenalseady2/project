package com.onemena.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.config.SPKey;
import com.onemena.utils.SpUtil;
import com.onemena.widght.HelveRomanTextView;

/**
 * Created by WHF on 2016-12-10.
 */

public class FeedbackAdapter extends BaseAdapter {

    public FeedbackAdapter(Context context){
        mContext=context;
    }


    @Override
    protected View getMyView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder =ViewHolder.getViewHolder(mContext, convertView, parent, R.layout.item_feedback_adapter, position);
        TextView txt_feedback=viewHolder.getView(R.id.txt_feedback);
        HelveRomanTextView txt_div_feed=viewHolder.getView(R.id.txt_div_feed);
        ImageView img_arrow=viewHolder.getView(R.id.img_arrow);
        if (SpUtil.getBoolean(SPKey.MODE,false)){
            txt_feedback.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
            img_arrow.setImageResource(R.mipmap.pc_arrow_night);
            txt_div_feed.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
        }else {
            txt_feedback.setTextColor(mContext.getResources().getColor(R.color.textcolor_222328));
            img_arrow.setImageResource(R.mipmap.arrow);
            txt_div_feed.setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));
        }

        JSONObject jsonObject = data.getJSONObject(position);
        txt_feedback.setText(jsonObject.getString("title"));
        txt_feedback.setTag(jsonObject.getString("url"));
        txt_feedback.setOnClickListener(mListener);


        return viewHolder.getConverView();
    }
}
