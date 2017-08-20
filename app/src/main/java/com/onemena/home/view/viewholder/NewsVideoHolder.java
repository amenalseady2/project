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

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.arabsada.news.R.id.img_like_2;
import static com.arabsada.news.R.id.iv_video_top_2;
import static com.arabsada.news.R.id.show_pop_iv_2;
import static com.arabsada.news.R.id.txt_com_2;
import static com.arabsada.news.R.id.txt_time_2;
import static com.arabsada.news.R.id.txt_zan_2;

/**
 * 三尺春光驱我寒，一生戎马为长安
 * Created by Han on 17/3/27.
 */

public class NewsVideoHolder extends BaseViewHolder<NewsItemBean> {
    @BindView(R.id.title2)
    HelveBoldTextView title2;
    @BindView(R.id.video_play_item)
    ImageView videoPlayItem;
    @BindView(iv_video_top_2)
    ImageView ivVideoTop2;
    @BindView(txt_time_2)
    TextView txtTime2;
    @BindView(txt_zan_2)
    TextView txtZan2;
    @BindView(img_like_2)
    ImageView imgLike2;
    @BindView(txt_com_2)
    NotoRegularTextView txtCom2;
    @BindView(show_pop_iv_2)
    ImageView showPopIv2;
    @BindView(R.id.style2)
    LinearLayout style2;
    @BindView(R.id.video_item_img_2)
    SimpleDraweeView videoItemImg2;
    @BindView(R.id.video_list_time_2)
    TextView videoListTime2;
    @BindView(R.id.img_com_2)
    SimpleDraweeView imgCom2;
    @BindView(R.id.lcoal_diver_video)
    TextView lcoalDiverVideo;

    public NewsVideoHolder(Context context, ViewGroup parent) {
        super(context, parent);
    }

    @Override
    public void bindHolder(NewsItemBean dataModel) {
        title2.setText(dataModel.getTitle());
        if (StringUtils.isEmpty(dataModel.getRecomId())) {
            txtCom2.setText(dataModel.getFirst_name());
        } else {
            txtCom2.setText(dataModel.getCategories_text());
        }
        txtTime2.setText(dataModel.getCreateTime());
        txtZan2.setText(StringUtils.int2IndiaNum(dataModel.getComment_count()));
        String video_time = dataModel.getVideo_time();
        if (StringUtils.isNotEmpty(video_time)) {
            videoListTime2.setVisibility(View.VISIBLE);
            videoListTime2.setText(video_time);
        } else {
            videoListTime2.setVisibility(View.GONE);
        }
        if (StringUtils.isNotEmpty(dataModel.getTop())) {
            ivVideoTop2.setVisibility(View.VISIBLE);
        } else {
            ivVideoTop2.setVisibility(View.GONE);
        }

        String profile_photo = dataModel.getProfile_photo();
        if (profile_photo != null) {
            imgCom2.setImageURI(Uri.parse(profile_photo));
        }
        List<String> rect_thumb_meta = dataModel.getRect_thumb_meta();
        if (rect_thumb_meta != null && !rect_thumb_meta.isEmpty()) {
            String imgUrl = rect_thumb_meta.get(0);
            if (imgUrl != null) {
                videoItemImg2.setImageURI(Uri.parse(imgUrl));
            }
        }

        if (dataModel.is_read()) {
            if (SpUtil.getBoolean(SPKey.MODE,false)) {
                title2.setTextColor(mContext.getResources().getColor(R.color.txt_4a4a4a));
            }else {
                title2.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
            }
        }else {
            if (SpUtil.getBoolean(SPKey.MODE,false)) {
                title2.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
            }else {
                title2.setTextColor(Color.BLACK);
            }
        }

    }


    @Override
    protected void nightMode() {
        //夜间模式
        imgCom2.getHierarchy().setPlaceholderImage(R.mipmap.news_com_night);
        style2.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
        showPopIv2.setImageResource(R.mipmap.home_delete_night);
        title2.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        txtCom2.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        txtTime2.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        txtZan2.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        txtCom2.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        imgLike2.setImageResource(R.mipmap.home_list_comment_night);
        ivVideoTop2.setImageResource(R.mipmap.home_list_top_night);
        lcoalDiverVideo.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
    }

    @Override
    protected void dayMode() {
        imgCom2.getHierarchy().setPlaceholderImage(R.mipmap.news_com);
        style2.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        showPopIv2.setImageResource(R.mipmap.home_delete);
        title2.setTextColor(Color.BLACK);
        txtCom2.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
        txtTime2.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
        txtZan2.setTextColor(mContext.getResources().getColor(R.color.txt_999999));
        imgLike2.setImageResource(R.mipmap.home_list_comment);
        ivVideoTop2.setImageResource(R.mipmap.home_list_top);
        lcoalDiverVideo.setBackgroundColor(mContext.getResources().getColor(R.color.txt_E3E3E3));
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_news_video;
    }

    @OnClick({show_pop_iv_2, R.id.style2})
    public void onClick(View view) {
        onViewClick(view);
    }
}
