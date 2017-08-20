package com.mysada.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mysada.news.R;
import com.mysada.news.app.config.Config;
import com.mysada.news.app.config.ConfigUrls;
import com.mysada.news.app.config.SPKey;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.widght.ContentBondTextView;
import com.onemena.widght.ContentTextView;
import com.onemena.widght.MyArabicTextview;

import java.util.Random;

/**
 * Created by Administrator on 2016/10/27.
 */

public class HomePagerRefushLVAdapter extends BaseAdapter {
    private Context mContext;
    private JSONArray img_arry;
    private JSONArray tags;
    private String createTime;
    private String like;
    private String user_name;
    private TextView txt_zan_3;
    private TextView txt_com_3;
    private TextView txt_time_3;
    private String profile_photo;
    private long newsTime = System.currentTimeMillis() + 20 * 60 * 1000;
    private boolean isShowNumTime = true;
    private Random random;
    private String posttype;
    private String comment_count_list;
    private boolean isTop;
    private JSONArray baseData = new JSONArray();


    public HomePagerRefushLVAdapter(Context context) {

        mContext = context;
    }


    /**
     * 更新经验列表
     */
    public void notifyDataSetChanged(int skipCount, JSONArray array, JSONArray topArray) {
        if (array == null) {
            return;
        }
        if (skipCount < 0) {//刷新前边加
            isShowNumTime = false;
            array.addAll(baseData);
            baseData = array;
            topArray.addAll(baseData);
            this.data = topArray;
        } else if (skipCount == 0) {//刷新清空原来数据,暂时还用不到
            newsTime = System.currentTimeMillis() + 20 * 60 * 1000;
            isShowNumTime = true;
            topArray.addAll(array);
            this.data = topArray;
        } else {
            isShowNumTime = true;
            baseData.addAll(array);//加载更多，后叠加
            topArray.addAll(baseData);
            this.data = topArray;
        }
        super.notifyDataSetChanged();
    }

    /**
     * 更新经验列表
     */
    public void notifyDataSetChanged(int skipCount, JSONArray array) {
        if (array == null) {
            return;
        }
        if (skipCount < 0) {
            isShowNumTime = false;
            array.addAll(this.data);
            this.data = array;
        } else if (skipCount == 0) {
            newsTime = System.currentTimeMillis() + 20 * 60 * 1000;
            isShowNumTime = true;
            this.data = array;
        } else {
            isShowNumTime = true;
            this.data.addAll(array);
        }
        super.notifyDataSetChanged();
    }


    /**
     * 更新经验列表
     */
    public int notifyDataSetChanged(int skipCount, JSONArray array, JSONArray topArray, int count) {
        if (array == null) {
            return 0;
        }
        synchronized (BaseAdapter.class) {
            pagers++;
            if (pagers > count) {
                return 1;
            }
            if (skipCount < 0) {//刷新前边加
                isShowNumTime = false;
                array.addAll(baseData);
                baseData = array;
                topArray.addAll(baseData);
                this.data = topArray;
            } else if (skipCount == 0) {//刷新清空原来数据,暂时还用不到
                newsTime = System.currentTimeMillis() + 20 * 60 * 1000;
                isShowNumTime = true;
                topArray.addAll(array);
                this.baseData = array;
                this.data = topArray;
            } else {
                isShowNumTime = true;
                baseData.addAll(array);//加载更多，后叠加
                topArray.addAll(baseData);
                this.data = topArray;
            }
        }
        super.notifyDataSetChanged();
        return 0;
    }


    @Override
    protected View getMyView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = ViewHolder.getViewHolder(mContext, convertView, parent,
                R.layout.item_home_fragment, position);

        LinearLayout ll_style1 = holder.getView(R.id.style1);
        LinearLayout ll_style2 = holder.getView(R.id.style2);
        LinearLayout ll_style3 = holder.getView(R.id.style3);
        LinearLayout ll_style4 = holder.getView(R.id.style4);

        JSONObject jsonObject = data.getJSONObject(position);
        img_arry = jsonObject.getJSONArray("rect_thumb_meta");
        tags = jsonObject.getJSONArray("tags");
//        createTime = jsonObject.getString("create_time");
        like = jsonObject.getString("like");
        comment_count_list = jsonObject.getString("comment_count");
        user_name = jsonObject.getString("first_name");
        profile_photo = jsonObject.getString("profile_photo");
        posttype = jsonObject.getString("posttype");

        if (jsonObject.containsKey("top")) {
            isTop = true;
        } else {
            isTop = false;
        }
        if (!jsonObject.containsKey("news_time")) {
            if (isShowNumTime) {
                random = new Random();
                newsTime = newsTime - random.nextInt(5) * 1000 * 60 - 4 * 1000 * 60;
                jsonObject.put("news_time", newsTime);
            } else {
                jsonObject.put("news_time", System.currentTimeMillis());
            }
        }
        long news_time = System.currentTimeMillis() - jsonObject.getLongValue("news_time");
        createTime = getFormatterTime(news_time);

        String type = "1";
        if ("video".equals(posttype)) {
            type = "2";

        } else {
            if (img_arry.size() > 0) {//有多张图片,或一张图
                if (img_arry.size() > 2) {
                    type = "3";
                }
                if (img_arry.size() == 1) {
                    String imgUrl = img_arry.getString(0);
                    if (TextUtils.isEmpty(imgUrl)) {
                        //一个图都没有
                        type = "4";
                    } else {
                        //有一个图
                        type = "1";
                    }
                }

            } else {
                type = "4";
            }
        }


        //夜间模式
        nightMode(ll_style1, ll_style2, ll_style3, ll_style4, SpUtil.getBoolean(SPKey.MODE, false), holder);

        switch (type) {
            case "1":
                style(ll_style1, ll_style2, ll_style3, ll_style4, jsonObject, Integer.parseInt(type), holder, position);
                break;
            case "2":
                style(ll_style1, ll_style2, ll_style3, ll_style4, jsonObject, Integer.parseInt(type), holder, position);
                break;
            case "3":
                style(ll_style1, ll_style2, ll_style3, ll_style4, jsonObject, Integer.parseInt(type), holder, position);
                break;
            case "4":
                style(ll_style1, ll_style2, ll_style3, ll_style4, jsonObject, Integer.parseInt(type), holder, position);
                break;
        }
        return holder.getConverView();
    }

    private String getFormatterTime(long news_time) {
        news_time /= 1000;
        if (news_time < 60) {
            return mContext.getResources().getString(R.string.only_just);
        } else if (news_time < 60 * 60) {
            return mContext.getResources().getString(R.string.before) + " " +
                    StringUtils.int2IndiaNum(String.valueOf(news_time / 60)) + " " +
                    mContext.getResources().getString(R.string.minute_before);
        } else if (news_time < 60 * 60 * 24) {
            return mContext.getResources().getString(R.string.before) + " " +
                    StringUtils.int2IndiaNum(String.valueOf(news_time / 60 / 60)) + " " +
                    mContext.getResources().getString(R.string.hour_before);
        } else if (news_time < 60 * 60 * 24 * 2) {
            return mContext.getResources().getString(R.string.yesterday);
        } else if (news_time < 60 * 60 * 24 * 3) {
            return mContext.getResources().getString(R.string.the_day_before_yesterday);
        } else {
            return mContext.getResources().getString(R.string.before) + " " +
                    StringUtils.int2IndiaNum(String.valueOf(news_time / 60 / 60 / 24)) + " " +
                    mContext.getResources().getString(R.string.day_before);
        }
    }


    private void nightMode(LinearLayout ll_style1, LinearLayout ll_style2, LinearLayout ll_style3, LinearLayout ll_style4, Boolean isNight, ViewHolder holder) {

        final MyArabicTextview tv_title_1 = (MyArabicTextview) ll_style1.findViewById(R.id.title1);
        ImageView show_pop_iv = (ImageView) ll_style1.findViewById(R.id.show_pop_iv);
        ImageView show_pop_iv1 = (ImageView) ll_style1.findViewById(R.id.show_pop_iv_1);
        ContentTextView txt_com_1 = (ContentTextView) ll_style1.findViewById(R.id.txt_com_1);
        ContentTextView txt_time_1 = (ContentTextView) ll_style1.findViewById(R.id.txt_time_1);
        TextView txt_zan_1 = (TextView) ll_style1.findViewById(R.id.txt_zan_1);
        ContentTextView txt_com_1_1 = (ContentTextView) ll_style1.findViewById(R.id.txt_com_1_1);
        TextView txt_time_1_1 = (TextView) ll_style1.findViewById(R.id.txt_time_1_1);
        TextView txt_zan_1_1 = (TextView) ll_style1.findViewById(R.id.txt_zan_1_1);
        ImageView img_like_1 = (ImageView) ll_style1.findViewById(R.id.img_like_1);
        ImageView img_like_1_1 = (ImageView) ll_style1.findViewById(R.id.img_like_1_1);
        ImageView iv_top_1 = (ImageView) ll_style1.findViewById(R.id.iv_top_1);
        ImageView iv_top_1_1 = (ImageView) ll_style1.findViewById(R.id.iv_top_1_1);
        SimpleDraweeView img_image1 = (SimpleDraweeView) ll_style1.findViewById(R.id.image1);

        ContentBondTextView tv_title_2 = (ContentBondTextView) ll_style2.findViewById(R.id.title2);
        ContentTextView txt_com_2 = (ContentTextView) ll_style2.findViewById(R.id.txt_com_2);
        TextView txt_time_2 = (TextView) ll_style2.findViewById(R.id.txt_time_2);
        TextView txt_zan_2 = (TextView) ll_style2.findViewById(R.id.txt_zan_2);
        ImageView show_pop_iv_2 = (ImageView) ll_style2.findViewById(R.id.show_pop_iv_2);
        ImageView img_like_2 = (ImageView) ll_style2.findViewById(R.id.img_like_2);
        ImageView iv_video_top_2 = (ImageView) ll_style2.findViewById(R.id.iv_video_top_2);


        ContentBondTextView tv_title_3 = (ContentBondTextView) ll_style3.findViewById(R.id.title3);
        ContentTextView txt_com_3 = (ContentTextView) ll_style3.findViewById(R.id.txt_com_3);
        TextView txt_time_3 = (TextView) ll_style3.findViewById(R.id.txt_time_3);
        TextView txt_zan_3 = (TextView) ll_style3.findViewById(R.id.txt_zan_3);
        ImageView show_pop_iv_3 = (ImageView) ll_style3.findViewById(R.id.show_pop_iv_3);
        ImageView img_like_3 = (ImageView) ll_style3.findViewById(R.id.img_like_3);
        ImageView iv_top_3 = (ImageView) ll_style3.findViewById(R.id.iv_top_3);
        SimpleDraweeView image3_1 = (SimpleDraweeView) ll_style3.findViewById(R.id.image3_1);
        SimpleDraweeView image3_2 = (SimpleDraweeView) ll_style3.findViewById(R.id.image3_2);
        SimpleDraweeView image3_3 = (SimpleDraweeView) ll_style3.findViewById(R.id.image3_3);


        ContentBondTextView tv_title_4 = (ContentBondTextView) ll_style4.findViewById(R.id.title4);
        ContentTextView txt_com_4 = (ContentTextView) ll_style4.findViewById(R.id.txt_com_4);
        TextView txt_time_4 = (TextView) ll_style4.findViewById(R.id.txt_time_4);
        TextView txt_zan_4 = (TextView) ll_style4.findViewById(R.id.txt_zan_4);
        ImageView show_pop_iv_4 = (ImageView) ll_style4.findViewById(R.id.show_pop_iv_4);
        ImageView img_like_4 = (ImageView) ll_style4.findViewById(R.id.img_like_4);
        ImageView iv_top_4 = (ImageView) ll_style4.findViewById(R.id.iv_top_4);

        if (isTop) {
            iv_top_1.setVisibility(View.VISIBLE);
            iv_top_1_1.setVisibility(View.VISIBLE);
            iv_video_top_2.setVisibility(View.VISIBLE);
            iv_top_3.setVisibility(View.VISIBLE);
            iv_top_4.setVisibility(View.VISIBLE);
        } else {
            iv_top_1.setVisibility(View.GONE);
            iv_top_1_1.setVisibility(View.GONE);
            iv_video_top_2.setVisibility(View.GONE);
            iv_top_3.setVisibility(View.GONE);
            iv_top_4.setVisibility(View.GONE);
        }

        if (isNight) {
            ll_style1.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
            ll_style2.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
            ll_style3.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
            ll_style4.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));

            show_pop_iv.setImageResource(R.mipmap.home_delete_night);
            show_pop_iv_2.setImageResource(R.mipmap.home_delete_night);
            show_pop_iv1.setImageResource(R.mipmap.home_delete_2_night);
            tv_title_2.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            tv_title_1.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_com_1.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_com_2.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_time_1.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_time_2.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_zan_1.setTextColor(mContext.getResources().getColor(R.color.color_945555));
            txt_zan_2.setTextColor(mContext.getResources().getColor(R.color.color_945555));
            txt_com_1_1.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_com_2.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_time_1_1.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_zan_1_1.setTextColor(mContext.getResources().getColor(R.color.color_945555));
            // holder.getView(R.id.view_d_1).setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
            img_like_1.setImageResource(R.mipmap.home_list_comment_night);
            img_like_2.setImageResource(R.mipmap.home_list_comment_night);
            img_like_1_1.setImageResource(R.mipmap.home_list_comment_night);
            iv_top_1.setImageResource(R.mipmap.home_list_top_night);
            iv_top_1_1.setImageResource(R.mipmap.home_list_top_night);
            iv_video_top_2.setImageResource(R.mipmap.home_list_top_night);
            img_image1.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);


            tv_title_3.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_com_3.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_time_3.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_zan_3.setTextColor(mContext.getResources().getColor(R.color.color_945555));
            show_pop_iv_3.setImageResource(R.mipmap.home_delete_2_night);
            //  holder.getView(R.id.view_d_3).setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
            img_like_3.setImageResource(R.mipmap.home_list_comment_night);
            iv_top_3.setImageResource(R.mipmap.home_list_top_night);
            image3_1.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);
            image3_2.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);
            image3_3.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);

            tv_title_4.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_com_4.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_time_4.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_zan_4.setTextColor(mContext.getResources().getColor(R.color.color_945555));
            show_pop_iv_4.setImageResource(R.mipmap.home_delete_2_night);
            holder.getView(R.id.view_d_4).setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
            img_like_4.setImageResource(R.mipmap.home_list_comment_night);
            iv_top_4.setImageResource(R.mipmap.home_list_top_night);


        } else {

            ll_style1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            ll_style2.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            ll_style3.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            ll_style4.setBackgroundColor(mContext.getResources().getColor(R.color.white));

            show_pop_iv.setImageResource(R.mipmap.home_delete);
            show_pop_iv1.setImageResource(R.mipmap.home_delete);
            show_pop_iv_2.setImageResource(R.mipmap.home_delete);
            tv_title_1.setTextColor(Color.BLACK);
            tv_title_2.setTextColor(Color.BLACK);
            txt_com_1.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            txt_com_2.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            txt_time_1.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            txt_time_2.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            txt_zan_1.setTextColor(mContext.getResources().getColor(R.color.color_ee4d));
            txt_zan_2.setTextColor(mContext.getResources().getColor(R.color.color_ee4d));
            txt_com_1_1.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            txt_time_1_1.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            txt_zan_1_1.setTextColor(mContext.getResources().getColor(R.color.color_ee4d));
            //  holder.getView(R.id.view_d_1).setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));
            img_like_1.setImageResource(R.mipmap.home_list_comment);
            img_like_2.setImageResource(R.mipmap.home_list_comment);
            img_like_1_1.setImageResource(R.mipmap.home_list_comment);
            iv_top_1.setImageResource(R.mipmap.home_list_top);
            iv_top_1_1.setImageResource(R.mipmap.home_list_top);
            iv_video_top_2.setImageResource(R.mipmap.home_list_top);
            img_image1.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);

            tv_title_3.setTextColor(Color.BLACK);
            txt_com_3.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            txt_time_3.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            txt_zan_3.setTextColor(mContext.getResources().getColor(R.color.color_ee4d));
            show_pop_iv_3.setImageResource(R.mipmap.home_delete);
            //  holder.getView(R.id.view_d_3).setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));
            img_like_3.setImageResource(R.mipmap.home_list_comment);
            iv_top_3.setImageResource(R.mipmap.home_list_top);
            image3_1.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);
            image3_2.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);
            image3_3.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);


            tv_title_4.setTextColor(Color.BLACK);
            txt_com_4.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            txt_time_4.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            txt_zan_4.setTextColor(mContext.getResources().getColor(R.color.color_ee4d));
            show_pop_iv_4.setImageResource(R.mipmap.home_delete);
            holder.getView(R.id.view_d_4).setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));
            img_like_4.setImageResource(R.mipmap.home_list_comment);
            iv_top_4.setImageResource(R.mipmap.home_list_top);

        }


    }

    /**
     * 设置显示样式
     *
     * @param ll_style1  样式1------一张图片（小）
     * @param ll_style2  样式2------一张图片（大）
     * @param ll_style3  样式3------三张图片
     * @param ll_style4  样式4------纯文字
     * @param jsonObject 数据源
     * @param type       类型
     */
    private void style(final LinearLayout ll_style1, LinearLayout ll_style2, LinearLayout ll_style3, LinearLayout ll_style4, JSONObject jsonObject, int type, final ViewHolder holder, int position) {
        String first_name = jsonObject.getString("first_name");
        if (StringUtils.isEmpty(first_name)) {
            first_name = "_";
        }
        switch (type) {
            case 1:
                ll_style1.setVisibility(View.VISIBLE);
                ll_style2.setVisibility(View.GONE);
                ll_style3.setVisibility(View.GONE);
                ll_style4.setVisibility(View.GONE);
                ll_style1.setOnClickListener(mListener);

                Bundle b_s1=new Bundle();
                b_s1.putString("unique_id",jsonObject.getString("unique_id").toString());
                b_s1.putString("id",jsonObject.getString("id").toString());
                b_s1.putString("position",position+"");
                ll_style1.setTag(b_s1);
                final MyArabicTextview tv_title_1 = holder.getView(R.id.title1);
                final View home_item_bottom_icons_1 = holder.getView(R.id.home_item_bottom_icons_1);
                final View home_item_bottom_icons = holder.getView(R.id.home_item_bottom_icons);
                ImageView show_pop_iv = holder.getView(R.id.show_pop_iv);
                ImageView show_pop_iv_1 = holder.getView(R.id.show_pop_iv_1);

                TextView txt_com_1 = (TextView) home_item_bottom_icons.findViewById(R.id.txt_com_1);
                TextView txt_zan_1 = (TextView) home_item_bottom_icons.findViewById(R.id.txt_zan_1);
                TextView txt_time_1 = (TextView) home_item_bottom_icons.findViewById(R.id.txt_time_1);
                SimpleDraweeView img_com_1 = (SimpleDraweeView) home_item_bottom_icons.findViewById(R.id.img_com_1);
                SimpleDraweeView img_com_1_1 = (SimpleDraweeView) home_item_bottom_icons_1.findViewById(R.id.img_com_1_1);
                GenericDraweeHierarchy hierarchy1 = img_com_1.getHierarchy();
                GenericDraweeHierarchy hierarchy1_1 = img_com_1_1.getHierarchy();
                if (SpUtil.getBoolean(SPKey.MODE, false)) {
                    //夜间模式
                    hierarchy1.setPlaceholderImage(R.mipmap.news_com_night);
                    hierarchy1_1.setPlaceholderImage(R.mipmap.news_com_night);
                } else {
                    hierarchy1.setPlaceholderImage(R.mipmap.news_com);
                    hierarchy1_1.setPlaceholderImage(R.mipmap.news_com);
                }
                txt_com_1.setText(user_name);
                txt_zan_1.setText(StringUtils.int2IndiaNum(comment_count_list));
                txt_time_1.setText(createTime);
                TextView txt_com_1_1 = (TextView) home_item_bottom_icons_1.findViewById(R.id.txt_com_1_1);
                TextView txt_zan_1_1 = (TextView) home_item_bottom_icons_1.findViewById(R.id.txt_zan_1_1);
                TextView txt_time_1_1 = (TextView) home_item_bottom_icons_1.findViewById(R.id.txt_time_1_1);
                txt_com_1_1.setText(user_name);
                txt_zan_1_1.setText(StringUtils.int2IndiaNum(comment_count_list));
                txt_time_1_1.setText(createTime);

                img_com_1.setImageURI(Uri.parse(ConfigUrls.HOST_DU_IMG + profile_photo));
                img_com_1_1.setImageURI(Uri.parse(ConfigUrls.HOST_DU_IMG + profile_photo));

                final View bottom_view_style1 = holder.getView(R.id.bottom_view_style1);

                final ImageView iv_dislike_1_1 = holder.getView(R.id.iv_dislike_1_1);
                ImageView iv_dislike_1_2 = holder.getView(R.id.iv_dislike_1_2);
                show_pop_iv.setOnClickListener(mListener);
                show_pop_iv_1.setOnClickListener(mListener);
                iv_dislike_1_1.setOnClickListener(mListener);
                iv_dislike_1_2.setOnClickListener(mListener);

                show_pop_iv.setTag(tags + "@" + jsonObject.getString("id") + "@" + position + "@" + first_name);
                iv_dislike_1_1.setTag(tags + "@" + jsonObject.getString("id") + "@" + position + "@" + first_name);
                show_pop_iv_1.setTag(tags + "@" + jsonObject.getString("id") + "@" + position + "@" + first_name);
                iv_dislike_1_2.setTag(tags + "@" + jsonObject.getString("id") + "@" + position + "@" + first_name);

                SimpleDraweeView image1 = holder.getView(R.id.image1);
                tv_title_1.setText(jsonObject.getString("title"));
                if (img_arry.size() > 0) {
                    image1.setImageURI(Uri.parse(img_arry.get(0).toString()));
                }
                //tv_title_1加载完成后调用的方法

                tv_title_1.post(new Runnable() {
                    @Override
                    public void run() {
                        int lineCount = tv_title_1.getLineCount();
                        if (lineCount >= 3) {
                            home_item_bottom_icons.setVisibility(View.GONE);
                            iv_dislike_1_1.setVisibility(View.GONE);
                            home_item_bottom_icons_1.setVisibility(View.VISIBLE);
                            bottom_view_style1.setVisibility(View.GONE);
                        } else {
                            home_item_bottom_icons.setVisibility(View.VISIBLE);
                            iv_dislike_1_1.setVisibility(View.VISIBLE);
                            home_item_bottom_icons_1.setVisibility(View.GONE);
                            bottom_view_style1.setVisibility(View.VISIBLE);
                        }

                    }
                });

                break;
            case 2:
                ll_style1.setVisibility(View.GONE);
                ll_style2.setVisibility(View.VISIBLE);
                ll_style3.setVisibility(View.GONE);
                ll_style4.setVisibility(View.GONE);
                ll_style2.setOnClickListener(mListener);
                ll_style2.setTag(jsonObject.getString("unique_id")+"@"+jsonObject.getString("id")+"@"+position);
                TextView tv2 = (TextView) ll_style2.findViewById(R.id.title2);
                SimpleDraweeView video_item_img_2 = holder.getView(R.id.video_item_img_2);
                ImageView show_pop_iv_2 = holder.getView(R.id.show_pop_iv_2);
                SimpleDraweeView img_com_2 = holder.getView(R.id.img_com_2);
                TextView txt_com_2 = holder.getView(R.id.txt_com_2);
                TextView txt_zan_2 = holder.getView(R.id.txt_zan_2);
                TextView txt_time_2 = holder.getView(R.id.txt_time_2);
                TextView video_list_time_2 = holder.getView(R.id.video_list_time_2);

                show_pop_iv_2.setOnClickListener(mListener);
                show_pop_iv_2.setTag(tags + "@" + jsonObject.getString("id") + "@" + position + "@" + first_name);
                img_com_2.setImageURI(Uri.parse(ConfigUrls.HOST_DU_IMG + profile_photo));
                video_item_img_2.setImageURI(Uri.parse(jsonObject.getString("img_url")));
                txt_com_2.setText(user_name);
                txt_zan_2.setText(StringUtils.int2IndiaNum(comment_count_list));
                txt_time_2.setText(createTime);
                String video_time = jsonObject.getString("video_time");
                if (StringUtils.isNotEmpty(video_time)) {
                    video_list_time_2.setVisibility(View.VISIBLE);
                    video_list_time_2.setText(video_time);
                } else {
                    video_list_time_2.setVisibility(View.GONE);
                }

                tv2.setText(jsonObject.getString("title"));
                GenericDraweeHierarchy hierarchy2 = img_com_2.getHierarchy();
//                image2.setImageURI(Uri.parse(img_arry.get(0).toString()));
                if (SpUtil.getBoolean(SPKey.MODE, false)) {
                    //夜间模式
                    hierarchy2.setPlaceholderImage(R.mipmap.news_com_night);
                } else {
                    hierarchy2.setPlaceholderImage(R.mipmap.news_com);
                }

                break;
            case 3:
                ll_style1.setVisibility(View.GONE);
                ll_style2.setVisibility(View.GONE);
                ll_style3.setVisibility(View.VISIBLE);
                ll_style4.setVisibility(View.GONE);
                ll_style3.setOnClickListener(mListener);

                Bundle b_s3=new Bundle();
                b_s3.putString("unique_id",jsonObject.getString("unique_id").toString());
                b_s3.putString("id",jsonObject.getString("id").toString());
                b_s3.putString("position",position+"");
                ll_style3.setTag(b_s3);
                TextView tv3 = (TextView) ll_style3.findViewById(R.id.title3);
                SimpleDraweeView image3_1 = holder.getView(R.id.image3_1);
                SimpleDraweeView image3_2 = holder.getView(R.id.image3_2);
                SimpleDraweeView image3_3 = holder.getView(R.id.image3_3);
                SimpleDraweeView img_com_3 = holder.getView(R.id.img_com_3);

                GenericDraweeHierarchy hierarchy = img_com_3.getHierarchy();
                if (SpUtil.getBoolean(SPKey.MODE, false)) {
                    //夜间模式
                    hierarchy.setPlaceholderImage(R.mipmap.news_com_night);
                } else {
                    hierarchy.setPlaceholderImage(R.mipmap.news_com);
                }

                txt_zan_3 = holder.getView(R.id.txt_zan_3);
                txt_com_3 = holder.getView(R.id.txt_com_3);
                txt_time_3 = holder.getView(R.id.txt_time_3);
                txt_com_3.setText(user_name);
                txt_time_3.setText(createTime);
                txt_zan_3.setText(StringUtils.int2IndiaNum(comment_count_list));
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) image3_1.getLayoutParams();
                int width = (Config.displayWidth - 90) / 3;
                linearParams.weight = width;
                linearParams.height = width * 72 / 100;
                image3_1.setLayoutParams(linearParams);
                image3_2.setLayoutParams(linearParams);
                image3_3.setLayoutParams(linearParams);
                ImageView show_pop_iv_3 = holder.getView(R.id.show_pop_iv_3);
                show_pop_iv_3.setOnClickListener(mListener);
                show_pop_iv_3.setTag(tags + "@" + jsonObject.getString("id") + "@" + position + "@" + first_name);

                RelativeLayout rl_dislike_3 = holder.getView(R.id.rl_dislike_3);
                rl_dislike_3.setOnClickListener(mListener);
                rl_dislike_3.setTag(tags + "@" + jsonObject.getString("id") + "@" + position + "@" + first_name);

                tv3.setText(jsonObject.getString("title"));
                image3_1.setImageURI(Uri.parse(img_arry.get(0).toString()));
                image3_2.setImageURI(Uri.parse(img_arry.get(1).toString()));
                image3_3.setImageURI(Uri.parse(img_arry.get(2).toString()));
                img_com_3.setImageURI(Uri.parse(ConfigUrls.HOST_DU_IMG + profile_photo));
                break;
            case 4:
                ll_style1.setVisibility(View.GONE);
                ll_style2.setVisibility(View.GONE);
                ll_style3.setVisibility(View.GONE);
                ll_style4.setVisibility(View.VISIBLE);
                ll_style4.setOnClickListener(mListener);

                Bundle b_s4=new Bundle();
                b_s4.putString("unique_id",jsonObject.getString("unique_id").toString());
                b_s4.putString("id",jsonObject.getString("id").toString());
                b_s4.putString("position",position+"");
                ll_style4.setTag(b_s4);
                TextView tv4 = holder.getView(R.id.title4);
                TextView txt_com_4 = holder.getView(R.id.txt_com_4);
                TextView txt_time_4 = holder.getView(R.id.txt_time_4);
                TextView txt_zan_4 = holder.getView(R.id.txt_zan_4);
                ImageView show_pop_iv_4 = holder.getView(R.id.show_pop_iv_4);
                SimpleDraweeView img_com_4 = holder.getView(R.id.img_com_4);

                GenericDraweeHierarchy hierarchy4 = img_com_4.getHierarchy();
                if (SpUtil.getBoolean(SPKey.MODE, false)) {
                    //夜间模式
                    hierarchy4.setPlaceholderImage(R.mipmap.news_com_night);
                } else {
                    hierarchy4.setPlaceholderImage(R.mipmap.news_com);
                }

                txt_time_4.setText(createTime);
                txt_com_4.setText(user_name);
                txt_zan_4.setText(StringUtils.int2IndiaNum(comment_count_list));
                tv4.setText(jsonObject.getString("title"));
                img_com_4.setImageURI(Uri.parse(ConfigUrls.HOST_DU_IMG + profile_photo));
                show_pop_iv_4.setOnClickListener(mListener);
                show_pop_iv_4.setTag(tags + "@" + jsonObject.getString("id") + "@" + position + "@" + first_name);
                RelativeLayout rl_dislike_4 = holder.getView(R.id.rl_dislike_4);
                rl_dislike_4.setOnClickListener(mListener);
                rl_dislike_4.setTag(tags + "@" + jsonObject.getString("id") + "@" + position + "@" + first_name);
                break;
            default:
                break;

        }

    }
}

