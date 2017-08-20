package com.onemena.home.view.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arabsada.news.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onemena.app.config.Config;
import com.onemena.app.config.SPKey;
import com.onemena.base.BaseViewHolder;
import com.onemena.home.model.javabean.NewsItemBean;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.widght.HelveBoldTextView;
import com.onemena.widght.NotoRegularTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 三尺春光驱我寒，一生戎马为长安
 * Created by Han on 17/3/27.
 */

public class NewsThreeImageHolder extends BaseViewHolder<NewsItemBean> {

    @BindView(R.id.title3)
    HelveBoldTextView title3;
    @BindView(R.id.image3_1)
    SimpleDraweeView image31;
    @BindView(R.id.image3_2)
    SimpleDraweeView image32;
    @BindView(R.id.image3_3)
    SimpleDraweeView image33;
    @BindView(R.id.iv_top_3)
    ImageView ivTop3;
    @BindView(R.id.txt_time_3)
    TextView txtTime3;
    @BindView(R.id.txt_zan_3)
    TextView txtZan3;
    @BindView(R.id.img_like_3)
    ImageView imgLike3;
    @BindView(R.id.txt_com_3)
    NotoRegularTextView txtCom3;
    @BindView(R.id.img_com_3)
    SimpleDraweeView imgCom3;
    @BindView(R.id.show_pop_iv_3)
    ImageView showPopIv3;
    @BindView(R.id.lcoal_diver_3)
    TextView lcoalDiver3;
    @BindView(R.id.style3)
    LinearLayout style3;
    @BindView(R.id.three_img_diver)
    TextView threeImgDiver;

    public NewsThreeImageHolder(Context context, ViewGroup parent) {
        super(context, parent);
    }

    @Override
    public void bindHolder(NewsItemBean dataModel) {
        title3.setText(dataModel.getTitle());

        if (StringUtils.isEmpty(dataModel.getRecomId())) {
            txtCom3.setText(dataModel.getFirst_name());
        } else {
            txtCom3.setText(dataModel.getCategories_text());
        }
        txtZan3.setText(StringUtils.int2IndiaNum(dataModel.getComment_count()));
        txtTime3.setText(dataModel.getCreateTime());
        imgCom3.setImageURI(Uri.parse(dataModel.getProfile_photo()));

        if (StringUtils.isNotEmpty(dataModel.getTop())) {
            ivTop3.setVisibility(View.VISIBLE);
        } else {
            ivTop3.setVisibility(View.GONE);
        }
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) image31.getLayoutParams();
        int width = (Config.displayWidth - 90) / 3;
        linearParams.weight = width;
        linearParams.height = width * 72 / 100;
        image31.setLayoutParams(linearParams);
        image32.setLayoutParams(linearParams);
        image33.setLayoutParams(linearParams);

        image31.setImageURI(dataModel.getRect_thumb_meta().get(0));
        image32.setImageURI(dataModel.getRect_thumb_meta().get(1));
        image33.setImageURI(dataModel.getRect_thumb_meta().get(2));

        if (dataModel.is_read()) {
            if (SpUtil.getBoolean(SPKey.MODE,false)) {

                title3.setTextColor(mContext.getResources().getColor(R.color.txt_4a4a4a));
            }else {
                title3.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
            }
        }else {
            if (SpUtil.getBoolean(SPKey.MODE,false)) {
                title3.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            }else {
                title3.setTextColor(Color.BLACK);
            }
        }
    }

    @Override
    protected void nightMode() {
        //夜间模式
        imgCom3.getHierarchy().setPlaceholderImage(R.mipmap.news_com_night);
        style3.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
        threeImgDiver.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
        title3.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        txtCom3.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        txtTime3.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        txtZan3.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        showPopIv3.setImageResource(R.mipmap.home_delete_2_night);
        imgLike3.setImageResource(R.mipmap.home_list_comment_night);
        ivTop3.setImageResource(R.mipmap.home_list_top_night);
        image31.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);
        image32.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);
        image33.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);
    }

    @Override
    protected void dayMode() {
        imgCom3.getHierarchy().setPlaceholderImage(R.mipmap.news_com);
        style3.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        threeImgDiver.setBackgroundColor(mContext.getResources().getColor(R.color.txt_E3E3E3));
        title3.setTextColor(Color.BLACK);
        txtCom3.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
        txtTime3.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
        txtZan3.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
        showPopIv3.setImageResource(R.mipmap.home_delete);
        imgLike3.setImageResource(R.mipmap.home_list_comment);
        ivTop3.setImageResource(R.mipmap.home_list_top);
        image31.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);
        image32.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);
        image33.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_news_three_image;
    }

    @OnClick({R.id.show_pop_iv_3, R.id.style3})
    public void onClick(View view) {
        onViewClick(view);
    }
}
