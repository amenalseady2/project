package com.onemena.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.config.SPKey;
import com.onemena.utils.SpUtil;

import java.util.HashMap;

/**
 * Created by WHF on 2016-12-29.
 */

public class AttentionTitleAdapter extends BaseAdapter {

    HashMap<String,Boolean> states=new HashMap<String,Boolean>();

    private TextView item_annention_title;
    private RelativeLayout item_title_lay;
    private TextView item_annention_bar;
    public AttentionTitleAdapter(Context context, HashMap<String,Boolean> states){
        this.states=states;
        mContext=context;
    }


    @Override
    protected View getMyView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=ViewHolder.getViewHolder(mContext, convertView, parent, R.layout.item_attention_title, position);;
        item_annention_title= holder.getView(R.id.item_annention_title);
        item_title_lay = holder.getView(R.id.item_title_lay);
        item_annention_bar=holder.getView(R.id.item_annention_bar);
        JSONObject jsonObject = data.getJSONObject(position);
        String id = jsonObject.getString("id");
        String name = jsonObject.getString("name");

        item_annention_title.setText(name);
        item_annention_title.setTag(position);
        item_annention_title.setOnClickListener(mListener);

        boolean res=false;
        if(states.get(String.valueOf(position)) == null || states.get(String.valueOf(position))== false){
            res=false;
            states.put(String.valueOf(position), false);
        }
        else{
            res = true;
        }
        checkMode(res);

        return holder.getConverView();
    }

    private void checkMode(boolean res) {

        if (SpUtil.getBoolean(SPKey.MODE,false)){
            item_annention_bar.setBackgroundColor(mContext.getResources().getColor(R.color.txt_234A7D));
            if (res){
                item_title_lay.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
                item_annention_title.setTextColor(mContext.getResources().getColor(R.color.txt_234A7D));
                item_annention_bar.setVisibility(View.VISIBLE);
            }else {
                item_annention_title.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
                item_annention_bar.setVisibility(View.GONE);
                item_title_lay.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_1b1b1b));
            }


        }else {
            item_annention_bar.setBackgroundColor(mContext.getResources().getColor(R.color.main_bule));
            if (res){
                item_title_lay.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                item_annention_title.setTextColor(mContext.getResources().getColor(R.color.main_bule));
                item_annention_bar.setVisibility(View.VISIBLE);
            }else {
                item_annention_title.setTextColor(mContext.getResources().getColor(R.color.textcolor_222328));
                item_annention_bar.setVisibility(View.GONE);
                item_title_lay.setBackgroundColor(mContext.getResources().getColor(R.color.color_f8f8f8));
            }
        }

    }
}
