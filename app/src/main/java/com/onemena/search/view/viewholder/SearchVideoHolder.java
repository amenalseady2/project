package com.onemena.search.view.viewholder;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arabsada.news.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onemena.base.BaseViewHolder;
import com.onemena.search.model.javabean.SearchItemBean;
import com.onemena.utils.StringUtils;
import com.onemena.widght.HelveBoldTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by WHF on 2017-04-08.
 */

public class SearchVideoHolder extends BaseViewHolder<SearchItemBean.RtListBean> {

    @BindView(R.id.iv_video_image)
    SimpleDraweeView ivVideoImage;
    @BindView(R.id.tv_video_time)
    TextView tvVideoTime;
    @BindView(R.id.tv_video_title)
    HelveBoldTextView tvVideoTitle;
//    @BindView(R.id.rl_search)
//    AutoRelativeLayout rlSearch;

    public SearchVideoHolder(Context context, ViewGroup parent) {
        super(context, parent);
    }

    @Override
    public void bindHolder(SearchItemBean.RtListBean dataModel) {
        tvVideoTitle.setText(Html.fromHtml(dataModel.getTitle()));

        String video_time = dataModel.getVideo_time();
        if (StringUtils.isEmpty(video_time)) {
            tvVideoTime.setVisibility(View.GONE);
        }else {
            tvVideoTime.setVisibility(View.VISIBLE);
            tvVideoTime.setText(video_time);
        }

        String img_url = dataModel.getImg_url();
        if (img_url != null) {
            ivVideoImage.setImageURI(Uri.parse(img_url));
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_video_search;
    }


    @OnClick(R.id.rl_search)
    public void onClick(View view) {
        onViewClick(view);
    }
}
