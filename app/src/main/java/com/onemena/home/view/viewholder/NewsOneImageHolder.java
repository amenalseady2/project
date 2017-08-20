package com.onemena.home.view.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arabsada.news.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.SPKey;
import com.onemena.base.BaseViewHolder;
import com.onemena.home.model.javabean.NewsItemBean;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.widght.MyArabicTextview;
import com.onemena.widght.NotoRegularTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 三尺春光驱我寒，一生戎马为长安
 * Created by Han on 17/3/25.
 */

public class NewsOneImageHolder extends BaseViewHolder<NewsItemBean> {


    @BindView(R.id.title1)
    MyArabicTextview title1;
    @BindView(R.id.img_like_1)
    ImageView imgLike1;
    @BindView(R.id.lcoal_diver_1)
    TextView lcoalDiver1;
    @BindView(R.id.home_item_bottom_icons)
    LinearLayout homeItemBottomIcons;
    @BindView(R.id.image1)
    SimpleDraweeView image1;
    @BindView(R.id.img_like_1_1)
    ImageView imgLike11;
    @BindView(R.id.lcoal_diver_1_1)
    TextView lcoalDiver11;
    @BindView(R.id.home_item_bottom_icons_1)
    RelativeLayout homeItemBottomIcons1;
    @BindView(R.id.style1)
    LinearLayout style1;
    @BindView(R.id.txt_time_1)
    NotoRegularTextView txtTime1;
    @BindView(R.id.iv_top_1)
    ImageView ivTop1;
    @BindView(R.id.txt_zan_1)
    TextView txtZan1;
    @BindView(R.id.txt_com_1)
    NotoRegularTextView txtCom1;
    @BindView(R.id.img_com_1)
    SimpleDraweeView imgCom1;
    @BindView(R.id.show_pop_iv)
    ImageView showPopIv;
    @BindView(R.id.iv_top_1_1)
    ImageView ivTop11;
    @BindView(R.id.txt_time_1_1)
    TextView txtTime11;
    @BindView(R.id.txt_zan_1_1)
    TextView txtZan11;
    @BindView(R.id.txt_com_1_1)
    NotoRegularTextView txtCom11;
    @BindView(R.id.img_com_1_1)
    SimpleDraweeView imgCom11;
    @BindView(R.id.show_pop_iv_1)
    ImageView showPopIv1;
    @BindView(R.id.one_img_diver)
    TextView oneImgDiver;
    @BindView(R.id.bottom_view_style1)
    TextView bottomViewStyle1;

    public NewsOneImageHolder(Context context, ViewGroup parent) {
        super(context, parent);
    }

    @Override
    public void bindHolder(NewsItemBean dataModel) {
        title1.setText(dataModel.getTitle());
        if (StringUtils.isEmpty(dataModel.getTop())) {
            ivTop1.setVisibility(View.GONE);
            ivTop11.setVisibility(View.GONE);
        } else {
            ivTop1.setVisibility(View.VISIBLE);
            ivTop11.setVisibility(View.VISIBLE);
        }


        image1.setImageURI(dataModel.getRect_thumb_meta().get(0));
        if (StringUtils.isEmpty(dataModel.getRecomId())) {
            txtCom1.setText(dataModel.getFirst_name());
            txtCom11.setText(dataModel.getFirst_name());
        }else {
            txtCom1.setText(dataModel.getCategories_text());
            txtCom11.setText(dataModel.getCategories_text());
        }
        txtZan1.setText(StringUtils.int2IndiaNum(dataModel.getComment_count()));
        txtZan11.setText(StringUtils.int2IndiaNum(dataModel.getComment_count()));
        txtTime1.setText(dataModel.getCreateTime());
        txtTime11.setText(dataModel.getCreateTime());
        imgCom1.setImageURI(Uri.parse(ConfigUrls.HOST_DU_IMG + dataModel.getProfile_photo()));
        imgCom11.setImageURI(Uri.parse(ConfigUrls.HOST_DU_IMG + dataModel.getProfile_photo()));
        title1.post(new Runnable() {
            @Override
            public void run() {
                int lineCount = title1.getLineCount();
                if (lineCount >= 3) {
                    homeItemBottomIcons.setVisibility(View.GONE);
                    homeItemBottomIcons1.setVisibility(View.VISIBLE);
                    bottomViewStyle1.setVisibility(View.GONE);
                } else {
                    bottomViewStyle1.setVisibility(View.VISIBLE);
                    homeItemBottomIcons.setVisibility(View.VISIBLE);
                    homeItemBottomIcons1.setVisibility(View.GONE);
                }
            }
        });

        if (dataModel.is_read()) {
            if (SpUtil.getBoolean(SPKey.MODE,false)) {

                title1.setTextColor(mContext.getResources().getColor(R.color.txt_4a4a4a));
            }else {
                title1.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
            }
        }else {
            if (SpUtil.getBoolean(SPKey.MODE,false)) {
                title1.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            }else {
                title1.setTextColor(Color.BLACK);
            }
        }

    }

    @Override
    protected void nightMode() {
        //夜间模式
        imgCom1.getHierarchy().setPlaceholderImage(R.mipmap.news_com_night);
        imgCom11.getHierarchy().setPlaceholderImage(R.mipmap.news_com_night);
        style1.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
        oneImgDiver.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
        showPopIv.setImageResource(R.mipmap.home_delete_night);
        showPopIv1.setImageResource(R.mipmap.home_delete_2_night);
        title1.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        txtCom1.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        txtTime1.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        txtZan1.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        txtCom11.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        txtTime11.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        txtZan11.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        imgLike1.setImageResource(R.mipmap.home_list_comment_night);
        imgLike11.setImageResource(R.mipmap.home_list_comment_night);
        ivTop1.setImageResource(R.mipmap.home_list_top_night);
        ivTop11.setImageResource(R.mipmap.home_list_top_night);
        image1.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);
    }

    @Override
    protected void dayMode() {
        imgCom1.getHierarchy().setPlaceholderImage(R.mipmap.news_com);
        imgCom11.getHierarchy().setPlaceholderImage(R.mipmap.news_com);
        style1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        oneImgDiver.setBackgroundColor(mContext.getResources().getColor(R.color.txt_E3E3E3));
        showPopIv1.setImageResource(R.mipmap.home_delete);
        showPopIv.setImageResource(R.mipmap.home_delete);
        title1.setTextColor(Color.BLACK);
        txtCom1.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
        txtTime1.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
        txtZan1.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
        txtCom11.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
        txtTime11.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
        txtZan11.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
        imgLike1.setImageResource(R.mipmap.home_list_comment);
        imgLike11.setImageResource(R.mipmap.home_list_comment);
        ivTop1.setImageResource(R.mipmap.home_list_top);
        ivTop11.setImageResource(R.mipmap.home_list_top);
        image1.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_news_one_image;
    }

    @OnClick({R.id.show_pop_iv, R.id.show_pop_iv_1, R.id.style1})
    public void onClick(View view) {
        onViewClick(view);
    }
}
