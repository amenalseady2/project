package com.onemena.app.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.config.Config;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.SPKey;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.widght.HelveBoldTextView;
import com.onemena.widght.NotoRegularTextView;
import com.onemena.widght.MyArabicTextview;

/**
 * Created by Administrator on 2016/10/27.
 */

public class CollectionAdapter extends BaseAdapter {
    private Context mContext;
    private JSONArray img_arry;
    private String user_name;
    private TextView txt_com_3;
    private String profile_photo;
    private boolean isEdit = false;
    private boolean mode;
    private boolean isAn;


    public CollectionAdapter(Context context) {

        mContext = context;
    }

    public void notifyDataSetChanged(boolean isEdit) {
        this.isEdit = isEdit;
        this.isAn = true;
        super.notifyDataSetChanged();
        mode = SpUtil.getBoolean(SPKey.MODE, false);
    }

    /**
     * 更新经验列表
     */
    public void notifyDataSetChanged(int skipCount, boolean isAn, JSONArray array) {
        this.isAn = isAn;
        if (array == null) {
            return;
        }
        synchronized (BaseAdapter.class) {
            if (skipCount <= 0) {
                this.data = array;
            } else {
                this.data.addAll(array);
            }
        }
        super.notifyDataSetChanged();
    }

    @Override
    protected View getMyView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = ViewHolder.getViewHolder(mContext, convertView, parent,
                R.layout.item_collection_fragment, position);

        LinearLayout ll_style1 = holder.getView(R.id.style1);
        LinearLayout ll_style2 = holder.getView(R.id.style2);
        LinearLayout ll_style3 = holder.getView(R.id.style3);
        LinearLayout ll_style4 = holder.getView(R.id.style4);
        RelativeLayout rl_check = holder.getView(R.id.rl_check);

        final JSONObject jsonObject = data.getJSONObject(position);
        img_arry = jsonObject.getJSONArray("rect_thumb_meta");
        user_name = jsonObject.getString("first_name");
        profile_photo = jsonObject.getString("profile_photo");
        if (!jsonObject.containsKey("isCheck")) {
            jsonObject.put("isCheck", false);
        }
        final ImageView iv_check = holder.getView(R.id.iv_check);
        iv_check.setImageResource(jsonObject.getBoolean("isCheck") ? mode ? R.mipmap.collection_checkbox_s_night : R.mipmap.collection_checkbox_s : mode ? R.mipmap.collection_checkbox_night : R.mipmap.collection_checkbox);
        iv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonObject.put("isCheck", !jsonObject.getBoolean("isCheck"));
                iv_check.setImageResource(jsonObject.getBoolean("isCheck") ? mode ? R.mipmap.collection_checkbox_s_night : R.mipmap.collection_checkbox_s : mode ? R.mipmap.collection_checkbox_night : R.mipmap.collection_checkbox);
                mListener.onClick(v);
            }
        });
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
        nightMode(ll_style1, ll_style2, ll_style3, ll_style4, rl_check, SpUtil.getBoolean(SPKey.MODE, false), holder);

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
        View view = holder.getView(R.id.ll_news_item);
        if (isAn) {
            if (isEdit) {
                ObjectAnimator.ofFloat(view, "translationX", 0.0f, -Config.widthPixels/8).setDuration(50).start();
            } else {
                jsonObject.put("isCheck", false);
                ObjectAnimator.ofFloat(view, "translationX", -Config.widthPixels/8, 0f).setDuration(50).start();
            }
        }
        return holder.getConverView();
    }


    private void nightMode(LinearLayout ll_style1, LinearLayout ll_style2, LinearLayout ll_style3, LinearLayout ll_style4, RelativeLayout rl_check, Boolean isNight, ViewHolder holder) {

        final MyArabicTextview tv_title_1 = (MyArabicTextview) ll_style1.findViewById(R.id.title1);
        NotoRegularTextView txt_com_1 = (NotoRegularTextView) ll_style1.findViewById(R.id.txt_com_1);
        NotoRegularTextView txt_com_1_1 = (NotoRegularTextView) ll_style1.findViewById(R.id.txt_com_1_1);
        SimpleDraweeView img_image1 = (SimpleDraweeView) ll_style1.findViewById(R.id.image1);


        HelveBoldTextView tv_title_3 = (HelveBoldTextView) ll_style3.findViewById(R.id.title3);
        NotoRegularTextView txt_com_3 = (NotoRegularTextView) ll_style3.findViewById(R.id.txt_com_3);
        SimpleDraweeView image3_1 = (SimpleDraweeView) ll_style3.findViewById(R.id.image3_1);
        SimpleDraweeView image3_2 = (SimpleDraweeView) ll_style3.findViewById(R.id.image3_2);
        SimpleDraweeView image3_3 = (SimpleDraweeView) ll_style3.findViewById(R.id.image3_3);


        HelveBoldTextView tv_title_4 = (HelveBoldTextView) ll_style4.findViewById(R.id.title4);
        NotoRegularTextView txt_com_4 = (NotoRegularTextView) ll_style4.findViewById(R.id.txt_com_4);


        if (isNight) {
            ll_style1.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
            ll_style2.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
            ll_style3.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
            ll_style4.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
            rl_check.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));

            tv_title_1.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_com_1.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_com_1_1.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            // holder.getView(R.id.view_d_1).setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
            img_image1.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);


            tv_title_3.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_com_3.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            //  holder.getView(R.id.view_d_3).setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
            image3_1.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);
            image3_2.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);
            image3_3.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);

            tv_title_4.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            txt_com_4.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            holder.getView(R.id.view_d_4).setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));


        } else {

            ll_style1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            ll_style2.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            ll_style3.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            ll_style4.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            rl_check.setBackgroundColor(mContext.getResources().getColor(R.color.white));

            tv_title_1.setTextColor(Color.BLACK);
            txt_com_1.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            txt_com_1_1.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            //  holder.getView(R.id.view_d_1).setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));
            img_image1.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);

            tv_title_3.setTextColor(Color.BLACK);
            txt_com_3.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
            //  holder.getView(R.id.view_d_3).setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));
            image3_1.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);
            image3_2.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);
            image3_3.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);


            tv_title_4.setTextColor(Color.BLACK);
            txt_com_4.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
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
                ll_style1.setTag(jsonObject.getString("unique_id") + "@" + jsonObject.getString("id") + "@" + position);
                final MyArabicTextview tv_title_1 = holder.getView(R.id.title1);
                final View home_item_bottom_icons_1 = holder.getView(R.id.home_item_bottom_icons_1);
                final View home_item_bottom_icons = holder.getView(R.id.home_item_bottom_icons);
                String s = jsonObject.getString("img");

                TextView txt_com_1 = (TextView) home_item_bottom_icons.findViewById(R.id.txt_com_1);
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
                TextView txt_com_1_1 = (TextView) home_item_bottom_icons_1.findViewById(R.id.txt_com_1_1);
                txt_com_1_1.setText(user_name);

                img_com_1.setImageURI(Uri.parse(ConfigUrls.HOST_DU_IMG + profile_photo));
                img_com_1_1.setImageURI(Uri.parse(ConfigUrls.HOST_DU_IMG + profile_photo));

                final View bottom_view_style1 = holder.getView(R.id.bottom_view_style1);

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
                            home_item_bottom_icons_1.setVisibility(View.VISIBLE);
                            bottom_view_style1.setVisibility(View.GONE);
                        } else {
                            home_item_bottom_icons.setVisibility(View.VISIBLE);
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
                ll_style2.setTag(jsonObject.getString("unique_id") + "@" + jsonObject.getString("id") + "@" + position);
                TextView tv2 = holder.getView(R.id.title2);
                SimpleDraweeView image2 = holder.getView(R.id.image2);
                tv2.setText(jsonObject.getString("title"));
                image2.setImageURI(Uri.parse(img_arry.get(0).toString()));
                break;
            case 3:
                ll_style1.setVisibility(View.GONE);
                ll_style2.setVisibility(View.GONE);
                ll_style3.setVisibility(View.VISIBLE);
                ll_style4.setVisibility(View.GONE);
                ll_style3.setOnClickListener(mListener);
                ll_style3.setTag(jsonObject.getString("unique_id") + "@" + jsonObject.getString("id") + "@" + position);
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

                txt_com_3 = holder.getView(R.id.txt_com_3);
                txt_com_3.setText(user_name);
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) image3_1.getLayoutParams();
                int width = (Config.displayWidth - 90) / 3;
                linearParams.weight = width;
                linearParams.height = width * 72 / 100;
                image3_1.setLayoutParams(linearParams);
                image3_2.setLayoutParams(linearParams);
                image3_3.setLayoutParams(linearParams);


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
                ll_style4.setTag(jsonObject.getString("unique_id") + "@" + jsonObject.getString("id") + "@" + position);
                TextView tv4 = holder.getView(R.id.title4);
                TextView txt_com_4 = holder.getView(R.id.txt_com_4);
                SimpleDraweeView img_com_4 = holder.getView(R.id.img_com_4);

                GenericDraweeHierarchy hierarchy4 = img_com_4.getHierarchy();
                if (SpUtil.getBoolean(SPKey.MODE, false)) {
                    //夜间模式
                    hierarchy4.setPlaceholderImage(R.mipmap.news_com_night);
                } else {
                    hierarchy4.setPlaceholderImage(R.mipmap.news_com);
                }

                txt_com_4.setText(user_name);
                tv4.setText(jsonObject.getString("title"));
                img_com_4.setImageURI(Uri.parse(ConfigUrls.HOST_DU_IMG + profile_photo));
                break;
            default:
                break;

        }

    }
}

