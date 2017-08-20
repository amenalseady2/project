package com.onemena.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.SPKey;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onemena.utils.SpUtil;
import com.onemena.widght.NotoRegularTextView;
import com.onemena.widght.HelveRomanTextView;

/**
 * Created by WHF on 2016-12-27.
 */

public class AttentionListAdapter extends BaseAdapter {


    private HelveRomanTextView item_btn_attention,txt_atten_com;

    private NotoRegularTextView txt_atten_num;
    private TextView attention_diver;
    private SimpleDraweeView imageView;
    private Boolean attentionState=false;
    private LinearLayout item_attention_lay;

    public AttentionListAdapter (Context context){
        mContext=context;
    }


    @Override
    protected View getMyView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getViewHolder(mContext, convertView, parent, R.layout.item_attention_fragment, position);
        item_attention_lay = (LinearLayout) holder.getView(R.id.item_attention_lay);
        item_btn_attention = holder.getView(R.id.item_btn_attention);
        txt_atten_com = holder.getView(R.id.txt_atten_com);
        txt_atten_num = holder.getView(R.id.txt_atten_num);
        attention_diver = holder.getView(R.id.attention_diver);
        imageView = holder.getView(R.id.imageView);

        final JSONObject object = data.getJSONObject(position);
        final String id = object.getString("id");
        String user_name = object.getString("first_name");
        String follow_count = object.getString("follow_count");
        String profile_photo = object.getString("profile_photo");

        txt_atten_com.setText(user_name);
        txt_atten_num.setText( "يتابعه" +" "+follow_count +" "+"شخص");
        imageView.setImageURI(ConfigUrls.HOST_DU_IMG + profile_photo);
        if (!object.containsKey("attentionState")) {
            object.put("attentionState", true);
        }
        attentionState = object.getBoolean("attentionState");
        removeAttention(attentionState);
        item_btn_attention.setTag(position);
        item_btn_attention.setOnClickListener(mListener);

        return holder.getConverView();
    }

    private void removeAttention(boolean b){

        if (SpUtil.getBoolean(SPKey.MODE,false)){
            //夜间

            if (b) {
                item_btn_attention.setTextColor(Color.parseColor("#707070"));
                item_btn_attention.setText(R.string.attention);
                //
                item_btn_attention.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.attention_item_selector_night_select));
            } else {
                item_btn_attention.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.attention_item_selector_night));
                item_btn_attention.setTextColor(Color.parseColor("#234A7D"));
                item_btn_attention.setText(R.string.notattention);
            }

            txt_atten_com.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
            txt_atten_num.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
            attention_diver.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
            item_attention_lay.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
        }else {

            //白天
            if (b) {
                //未关注（蓝）
                item_btn_attention.setTextColor(Color.parseColor("#a1a6bb"));
                item_btn_attention.setText(R.string.attention);
                item_btn_attention.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.attention_item_selector_select));
            } else {
                //已关注（灰）
                item_btn_attention.setTextColor(Color.parseColor("#3E84E0"));
                item_btn_attention.setText(R.string.notattention);
                item_btn_attention.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.attention_item_selector));
            }

            txt_atten_com.setTextColor(mContext.getResources().getColor(R.color.textcolor_222328));
            txt_atten_num.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            attention_diver.setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));
            item_attention_lay.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }


    }

}
