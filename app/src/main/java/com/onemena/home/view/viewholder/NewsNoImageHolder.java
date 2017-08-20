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

public class NewsNoImageHolder extends BaseViewHolder<NewsItemBean> {

    @BindView(R.id.title4)
    HelveBoldTextView title4;
    @BindView(R.id.iv_top_4)
    ImageView ivTop4;
    @BindView(R.id.txt_time_4)
    TextView txtTime4;
    @BindView(R.id.txt_zan_4)
    TextView txtZan4;
    @BindView(R.id.img_like_4)
    ImageView imgLike4;
    @BindView(R.id.txt_com_4)
    NotoRegularTextView txtCom4;
    @BindView(R.id.img_com_4)
    SimpleDraweeView imgCom4;
    @BindView(R.id.show_pop_iv_4)
    ImageView showPopIv4;
    @BindView(R.id.style4)
    LinearLayout style4;
    @BindView(R.id.lcoal_diver_4)
    TextView lcoalDiver4;

    public NewsNoImageHolder(Context context, ViewGroup parent) {
        super(context, parent);
    }

    @Override
    public void bindHolder(NewsItemBean dataModel) {
        title4.setText(dataModel.getTitle());

        if (org.apache.commons.lang3.StringUtils.isEmpty(dataModel.getRecomId())) {
            txtCom4.setText(dataModel.getFirst_name());
        }else {
            txtCom4.setText(dataModel.getCategories_text());
        }


        txtZan4.setText(StringUtils.int2IndiaNum(dataModel.getComment_count()));
        txtTime4.setText(dataModel.getCreateTime());
        String profile_photo = dataModel.getProfile_photo();
        if (profile_photo != null) {
            imgCom4.setImageURI(Uri.parse(profile_photo));
        }


        if (StringUtils.isNotEmpty(dataModel.getTop())) {
            ivTop4.setVisibility(View.VISIBLE);
        } else {
            ivTop4.setVisibility(View.GONE);
        }
        if (dataModel.is_read()) {
            if (SpUtil.getBoolean(SPKey.MODE, false)) {
                title4.setTextColor(mContext.getResources().getColor(R.color.txt_4a4a4a));
            } else {
                title4.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
            }
        } else {
            if (SpUtil.getBoolean(SPKey.MODE, false)) {
                title4.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            } else {
                title4.setTextColor(Color.BLACK);
            }
        }

    }

    @Override
    protected void nightMode() {
        //夜间模式
        imgCom4.getHierarchy().setPlaceholderImage(R.mipmap.news_com_night);
        style4.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
        lcoalDiver4.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
        title4.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        txtCom4.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        txtTime4.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        txtZan4.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        showPopIv4.setImageResource(R.mipmap.home_delete_2_night);
        lcoalDiver4.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
        imgLike4.setImageResource(R.mipmap.home_list_comment_night);
        ivTop4.setImageResource(R.mipmap.home_list_top_night);
    }

    @Override
    protected void dayMode() {
        imgCom4.getHierarchy().setPlaceholderImage(R.mipmap.news_com);
        style4.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        lcoalDiver4.setBackgroundColor(mContext.getResources().getColor(R.color.txt_E3E3E3));
        title4.setTextColor(Color.BLACK);
        txtCom4.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
        txtTime4.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
        txtZan4.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
        showPopIv4.setImageResource(R.mipmap.home_delete);
        lcoalDiver4.setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));
        imgLike4.setImageResource(R.mipmap.home_list_comment);
        ivTop4.setImageResource(R.mipmap.home_list_top);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_news_notimage;
    }

    @OnClick({R.id.show_pop_iv_4, R.id.style4})
    public void onClick(View view) {
        onViewClick(view);
    }
}
