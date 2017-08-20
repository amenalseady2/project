package com.onemena.video.view.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.adapter.BaseAdapter;
import com.onemena.app.adapter.ViewHolder;
import com.onemena.app.config.SPKey;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.widght.HelveBoldTextView;
import com.onemena.widght.NotoRegularTextView;

/**
 * Created by Administrator on 2016/10/28.
 */
public class VideoPagerRefushLVAdapter extends BaseAdapter {
    private Activity mContext;
    private String category_id;
    private String mId;
    private TextView video_list_playcount;
    private NotoRegularTextView video_from_tv;
    private View video_view_bottom;
    private LinearLayout ll_video_bottom;
    private SimpleDraweeView video_item_img;
    private ImageView video_share;
    private HelveBoldTextView video_list_title;
    private TextView video_list_time;
    private TextView video_list_comment;
    private ImageView video_play_item;
    private ImageView video_comment_img;

    public VideoPagerRefushLVAdapter(Activity context, String category_id) {
        mContext = context;
        this.category_id = category_id;
    }


    @Override
    protected View getMyView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getViewHolder(mContext, convertView, parent,
                R.layout.video_item_list, position);
        video_item_img = holder.getView(R.id.video_item_img);
        video_play_item = holder.getView(R.id.video_play_item);
        video_list_comment = holder.getView(R.id.video_list_comment);
        video_comment_img = holder.getView(R.id.video_comment_img);
        ll_video_bottom = holder.getView(R.id.ll_video_bottom);
        video_list_playcount = holder.getView(R.id.video_list_playcount);
        video_from_tv = holder.getView(R.id.video_from_tv);
        video_view_bottom = holder.getView(R.id.video_view_bottom);
        video_share =  holder.getView(R.id.video_share);
        video_list_title = holder.getView(R.id.video_list_title);
        video_list_time = holder.getView(R.id.video_list_time);

        final JSONObject object = data.getJSONObject(position);
        String title = object.getString("title");
        String img_url = object.getString("img_url");
        String total_view = object.getString("total_view");
        String comment_count = object.getString("comment_count");
        String video_time = object.getString("video_time");
        String id = object.getString("id");
        if (StringUtils.isEmpty(video_time)){
            video_list_time.setVisibility(View.GONE);
        }else {
            video_list_time.setVisibility(View.VISIBLE);
        }

        if (StringUtils.isEmpty(comment_count)){
            comment_count="0";
        }
        if (StringUtils.isEmpty(total_view)){
            total_view="0";
        }

        String[] split_comm = StringUtils.split(getCountNum(Integer.parseInt(comment_count)), "@");
        String[] split_total = StringUtils.split(getCountNum(Integer.parseInt(total_view)), "@");


        video_list_title.setText(title);
        video_list_time.setText(video_time);
        video_item_img.setImageURI(img_url);
        video_list_comment.setText(StringUtils.int2IndiaNum(split_comm[1])+split_comm[0]);
        video_list_playcount.setText(StringUtils.int2IndiaNum(split_total[1])+" "+split_total[0]+" "+mContext.getResources().getString(R.string.video_play_count));


        video_share.setOnClickListener(mListener);
        video_item_img.setOnClickListener(mListener);
        video_comment_img.setOnClickListener(mListener);

        video_comment_img.setTag(id+"@"+title+"@"+comment_count+"@"+img_url);
        video_item_img.setTag(position+"@"+id);//
        //Activity context, String shareTitle, String shareDesc , String shareMainPic, String shareUrl,String category_id,String news_id,String come
        video_share.setTag(position+"@"+id);
        changeLikeView();
        return holder.getConverView();
    }

    private String getCountNum(int videoTimes) {
        String unit = "";
        if (videoTimes > 1000) {
            videoTimes = videoTimes / 1000;
            unit = mContext.getResources().getString(R.string.thousand);
            if (videoTimes > 1000) {
                videoTimes = videoTimes / 1000;
                unit = mContext.getResources().getString(R.string.million);
            }
        }
        return unit+"@"+videoTimes;
    }


    private void changeLikeView() {
        if (SpUtil.getBoolean(SPKey.MODE, false)) {
            //夜间模式
            ll_video_bottom.setBackgroundColor(Color.parseColor("#252525"));
            video_view_bottom.setBackgroundColor(Color.parseColor("#1B1B1B"));
            video_list_playcount.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
            video_list_comment.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
            video_list_title.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
            video_from_tv.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
            video_play_item.setImageResource(R.mipmap.video_play_night);
            video_comment_img.setImageResource(R.mipmap.video_comment_night);
        } else {
            //白天模式
            ll_video_bottom.setBackgroundColor(Color.parseColor("#ffffff"));
            video_view_bottom.setBackgroundColor(Color.parseColor("#E7E7E7"));
            video_list_playcount.setTextColor(mContext.getResources().getColor(R.color.textcolor_989DB1));
            video_list_comment.setTextColor(mContext.getResources().getColor(R.color.textcolor_989DB1));
            video_from_tv.setTextColor(mContext.getResources().getColor(R.color.textcolor_989DB1));
            video_list_title.setTextColor(mContext.getResources().getColor(R.color.white));
            video_play_item.setImageResource(R.mipmap.video_play);
            video_comment_img.setImageResource(R.mipmap.video_comment);
        }
    }

}
