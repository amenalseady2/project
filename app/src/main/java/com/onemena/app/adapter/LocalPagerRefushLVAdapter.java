package com.onemena.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.config.Config;
import com.onemena.app.config.SPKey;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onemena.utils.SpUtil;
import com.onemena.widght.HelveBoldTextView;
import com.onemena.widght.MyArabicTextview;

/**
 * Created by Administrator on 2016/10/27.
 */

public class LocalPagerRefushLVAdapter extends BaseAdapter {
    private Context mContext;
    private JSONArray img_arry;
    private String dirPath= "asset:///localpicture/";

    public LocalPagerRefushLVAdapter(Context context) {
        mContext = context;
    }

    @Override
    protected View getMyView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = ViewHolder.getViewHolder(mContext, convertView, parent,
                R.layout.item_local_fragment, position);

        LinearLayout ll_style1 = holder.getView(R.id.style1);
        LinearLayout ll_style2 = holder.getView(R.id.style2);
        LinearLayout ll_style3 = holder.getView(R.id.style3);
        LinearLayout ll_style4 = holder.getView(R.id.style4);

        JSONObject jsonObject = data.getJSONObject(position);
        img_arry = jsonObject.getJSONArray("rect_thumb_meta");
        String type = "1";
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

    private void nightMode(LinearLayout ll_style1, LinearLayout ll_style2, LinearLayout ll_style3, LinearLayout ll_style4, Boolean isNight, ViewHolder holder) {

        final MyArabicTextview tv_title_1 = (MyArabicTextview) ll_style1.findViewById(R.id.title1);
        SimpleDraweeView img_image1 = (SimpleDraweeView) ll_style1.findViewById(R.id.image1);



        HelveBoldTextView tv_title_2 = (HelveBoldTextView) ll_style2.findViewById(R.id.title2);


        HelveBoldTextView tv_title_3 = (HelveBoldTextView) ll_style3.findViewById(R.id.title3);
        SimpleDraweeView image3_1 = (SimpleDraweeView) ll_style3.findViewById(R.id.image3_1);
        SimpleDraweeView image3_2 = (SimpleDraweeView) ll_style3.findViewById(R.id.image3_2);
        SimpleDraweeView image3_3 = (SimpleDraweeView) ll_style3.findViewById(R.id.image3_3);


        HelveBoldTextView tv_title_4 = (HelveBoldTextView) ll_style4.findViewById(R.id.title4);


        if (isNight) {
            ll_style1.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
            ll_style2.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
            ll_style3.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
            ll_style4.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));

            tv_title_2.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            tv_title_1.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            img_image1.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);


            tv_title_3.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            image3_1.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);
            image3_2.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);
            image3_3.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);

            tv_title_4.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            holder.getView(R.id.view_d_4).setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));


        } else {

            ll_style1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            ll_style2.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            ll_style3.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            ll_style4.setBackgroundColor(mContext.getResources().getColor(R.color.white));

            tv_title_1.setTextColor(Color.BLACK);
            tv_title_2.setTextColor(Color.BLACK);
            img_image1.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);

            tv_title_3.setTextColor(Color.BLACK);
            image3_1.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);
            image3_2.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);
            image3_3.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);


            tv_title_4.setTextColor(Color.BLACK);
            holder.getView(R.id.view_d_4).setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));

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
        switch (type) {
            case 1:
                ll_style1.setVisibility(View.VISIBLE);
                ll_style2.setVisibility(View.GONE);
                ll_style3.setVisibility(View.GONE);
                ll_style4.setVisibility(View.GONE);
                ll_style1.setOnClickListener(mListener);

                Bundle b_s1=new Bundle();
                b_s1.putString("articleDetail",jsonObject.getJSONObject("articleDetail").toString());
                b_s1.putString("id",jsonObject.getString("id"));
                ll_style1.setTag(b_s1);
                final MyArabicTextview tv_title_1 = holder.getView(R.id.title1);
                final View bottom_view_style1 = holder.getView(R.id.bottom_view_style1);
                SimpleDraweeView image1 = holder.getView(R.id.image1);
                tv_title_1.setText(jsonObject.getString("title"));
                if (img_arry.size() > 0) {
                    image1.setImageURI(dirPath+img_arry.get(0).toString());
                }
                break;
            case 2:
                break;
            case 3:
                ll_style1.setVisibility(View.GONE);
                ll_style2.setVisibility(View.GONE);
                ll_style3.setVisibility(View.VISIBLE);
                ll_style4.setVisibility(View.GONE);
                ll_style3.setOnClickListener(mListener);

                Bundle b_s3=new Bundle();
                b_s3.putString("articleDetail",jsonObject.getJSONObject("articleDetail").toString());
                b_s3.putString("id",jsonObject.getString("id"));
                ll_style3.setTag(b_s3);
                TextView tv3 = (TextView) ll_style3.findViewById(R.id.title3);
                SimpleDraweeView image3_1 = holder.getView(R.id.image3_1);
                SimpleDraweeView image3_2 = holder.getView(R.id.image3_2);
                SimpleDraweeView image3_3 = holder.getView(R.id.image3_3);

                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) image3_1.getLayoutParams();
                int width = (Config.displayWidth - 90) / 3;
                linearParams.weight = width;
                linearParams.height = width * 72 / 100;
                image3_1.setLayoutParams(linearParams);
                image3_2.setLayoutParams(linearParams);
                image3_3.setLayoutParams(linearParams);
                tv3.setText(jsonObject.getString("title"));
                image3_1.setImageURI(dirPath+img_arry.get(0).toString());
                image3_2.setImageURI(dirPath+img_arry.get(1).toString());
                image3_3.setImageURI(dirPath+img_arry.get(2).toString());
                break;
            case 4:
                ll_style1.setVisibility(View.GONE);
                ll_style2.setVisibility(View.GONE);
                ll_style3.setVisibility(View.GONE);
                ll_style4.setVisibility(View.VISIBLE);
                ll_style4.setOnClickListener(mListener);

                Bundle b_s4=new Bundle();
                b_s4.putString("articleDetail",jsonObject.getJSONObject("articleDetail").toString());
                b_s4.putString("id",jsonObject.getString("id"));
                ll_style4.setTag(b_s4);
                TextView tv4 = holder.getView(R.id.title4);
                tv4.setText(jsonObject.getString("title"));
                break;
            default:
                break;

        }

    }
}

