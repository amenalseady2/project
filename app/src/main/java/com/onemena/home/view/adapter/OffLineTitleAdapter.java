package com.onemena.home.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.adapter.BaseAdapter;
import com.onemena.app.adapter.ViewHolder;
import com.onemena.app.config.SPKey;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onemena.utils.SpUtil;

/**
 * Created by WHF on 2017-01-07.
 */

public class OffLineTitleAdapter extends BaseAdapter {


    public OffLineTitleAdapter(Context mContext){
        this.mContext =mContext;
    }

    @Override
    protected View getMyView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder =ViewHolder.getViewHolder(mContext,convertView,parent, R.layout.item_offlinetitle,position);
        final ImageView checkbox_title = holder.getView(R.id.checkbox_title);
        RelativeLayout rl_title = holder.getView(R.id.rl_title);
        TextView item_title_offline = holder.getView(R.id.item_title_offline);
        SimpleDraweeView item_img_offline = holder.getView(R.id.item_img_offline);
        GenericDraweeHierarchy hierarchy = item_img_offline.getHierarchy();
        hierarchy.setPlaceholderImage(R.mipmap.ic_launcher);

        final JSONObject jsonObject = data.getJSONObject(position);
        item_img_offline.setImageURI(Uri.parse("res://" + mContext.getPackageName() + "/" + jsonObject.getInteger("img")));
        item_title_offline.setText(jsonObject.getString("name"));
        if (!jsonObject.containsKey("isCheck")){
            jsonObject.put("isCheck",false);
        }

        rl_title.setOnClickListener(mListener);
        rl_title.setTag(position);
        if (SpUtil.getBoolean(SPKey.MODE,false)){
            checkbox_title.setImageResource(jsonObject.getBoolean("isCheck")?R.mipmap.download_checkbox_night:R.mipmap.checkbox_normal_nigth);
            item_img_offline.setAlpha(1.0f);
        }else {
            checkbox_title.setImageResource(jsonObject.getBoolean("isCheck")?R.mipmap.download_checkbox:R.mipmap.checkbox_normal);
        }


        return holder.getConverView();
    }
}
