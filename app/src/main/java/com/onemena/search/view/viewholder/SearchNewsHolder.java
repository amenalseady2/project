package com.onemena.search.view.viewholder;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;

import com.arabsada.news.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onemena.base.BaseViewHolder;
import com.onemena.search.model.javabean.SearchItemBean;
import com.onemena.widght.HelveBoldTextView;
import com.onemena.widght.HelveRomanTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * SearchNewsHolder Created by voler on 2017/5/23.
 * 说明：
 */

public class SearchNewsHolder extends BaseViewHolder<SearchItemBean.RtListBean> {

    @BindView(R.id.iv_news_image)
    SimpleDraweeView ivNewsImage;
    @BindView(R.id.tv_news_title)
    HelveBoldTextView tvNewsTitle;
    @BindView(R.id.iv_from)
    SimpleDraweeView ivFrom;
//    @BindView(R.id.rl_search)
//    AutoRelativeLayout rlSearch;
    @BindView(R.id.tv_from)
    HelveRomanTextView tvFrom;

    public SearchNewsHolder(Context context, ViewGroup parent) {
        super(context, parent);
    }

    @Override
    public void bindHolder(SearchItemBean.RtListBean dataModel) {
        tvNewsTitle.setText(Html.fromHtml(dataModel.getTitle()));
        tvFrom.setText(dataModel.getFirstName());
        String img_url = dataModel.getImg_url();
        if (img_url != null) {
            ivNewsImage.setImageURI(Uri.parse(img_url));
        }
        String imgFrom = dataModel.getUserPhoto();
        if (imgFrom != null) {
            ivFrom.setImageURI(Uri.parse(imgFrom));
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_news_search;
    }


    @OnClick(R.id.rl_search_news)
    public void onClick(View view) {
        onViewClick(view);
    }
}
