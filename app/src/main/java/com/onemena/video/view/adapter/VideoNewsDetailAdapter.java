package com.onemena.video.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.adapter.BaseAdapter;
import com.onemena.app.adapter.ViewHolder;
import com.onemena.app.config.SPKey;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.widght.MyArabicTextview;

/**
 * Created by Administrator on 2016/11/3.
 */
public class VideoNewsDetailAdapter extends BaseAdapter {
    private Context mContext;

    public VideoNewsDetailAdapter(Context context) {

        mContext = context;
    }

    @Override
    protected View getMyView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getViewHolder(mContext, convertView, parent, R.layout.item_video_news_detail, position);


        final MyArabicTextview text_title = holder.getView(R.id.title);
        text_title.setMaxLines(2);
        SimpleDraweeView image = holder.getView(R.id.image);


        TextView videoTimes = holder.getView(R.id.txt_relation_com);
        TextView tv_video_time = holder.getView(R.id.tv_video_time);
        TextView txt_diver = holder.getView(R.id.txt_diver);
        TextView tv_video_source = holder.getView(R.id.tv_video_source);
        View root_view = holder.getView(R.id.root_view);
        if (SpUtil.getBoolean(SPKey.MODE, false)) {
            text_title.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
            videoTimes.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
            root_view.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
            txt_diver.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
            tv_video_source.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));

            image.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);

        } else {
            text_title.setTextColor(mContext.getResources().getColor(R.color.textcolor_222328));
            videoTimes.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            root_view.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            txt_diver.setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));
            tv_video_source.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));

            image.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);
        }

        final JSONObject jsonObject = data.getJSONObject(position);
        text_title.setText(jsonObject.getString("title"));


        image.setImageURI(Uri.parse(jsonObject.getString("img_url")));

        String total_view = jsonObject.getString("total_view");
        if (org.apache.commons.lang3.StringUtils.isBlank(total_view)) {
            total_view="0";
        }
        long videoLongTimes = Long.parseLong(total_view);
        String unit = "";
        if (videoLongTimes > 1000) {
            videoLongTimes = videoLongTimes / 1000;
            unit = mContext.getResources().getString(R.string.thousand);
            if (videoLongTimes > 1000) {
                videoLongTimes = videoLongTimes / 1000;
                unit = mContext.getString(R.string.million);
            }
        }
        videoTimes.setText(StringUtils.int2IndiaNum("" + videoLongTimes)+" " + unit+ " "+ mContext.getString(R.string.video_play_count));
        String video_time = jsonObject.getString("video_time");
        if (StringUtils.isEmpty(video_time)) {
            tv_video_time.setVisibility(View.GONE);
        } else {
            tv_video_time.setVisibility(View.VISIBLE);
            tv_video_time.setText(video_time);
        }
        root_view.setTag(data.getJSONObject(position).getString("id"));
        root_view.setOnClickListener(mListener);
        return holder.getConverView();
    }


}
