package com.onemena.home.view.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arabsada.news.R;
import com.onemena.base.BaseViewHolder;
import com.onemena.home.model.javabean.NewsItemBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 三尺春光驱我寒，一生戎马为长安
 * Created by Han on 17/3/25.
 */

public class SpecialOneImageHolder extends BaseViewHolder<NewsItemBean> {


    @BindView(R.id.special_img)
    ImageView specialImg;
    @BindView(R.id.one_img_diver)
    TextView oneImgDiver;
    @BindView(R.id.styles)
    LinearLayout styles;

    public SpecialOneImageHolder(Context context, ViewGroup parent) {
        super(context, parent);
    }

    @Override
    public void bindHolder(NewsItemBean dataModel) {

    }

    @Override
    protected void nightMode() {
        styles.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
        oneImgDiver.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
        specialImg.setImageResource(R.mipmap.imgentrance_night);
    }

    @Override
    protected void dayMode() {
        styles.setBackgroundColor(mContext.getResources().getColor(R.color.activity_bg_F4F5F6));
        oneImgDiver.setBackgroundColor(mContext.getResources().getColor(R.color.txt_E3E3E3));
        specialImg.setImageResource(R.mipmap.imgentrance);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_special_one_image;
    }

    @OnClick(R.id.styles)
    public void onClick(View view) {
        onViewClick(view);
    }
}
