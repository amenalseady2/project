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
 * SearchNewsTextHolder Created by voler on 2017/5/23.
 * 说明：
 */

public class SearchNewsTextHolder extends BaseViewHolder<SearchItemBean.RtListBean> {
    @BindView(R.id.tv_news_title)
    HelveBoldTextView tvNewsTitle;
    @BindView(R.id.iv_from)
    SimpleDraweeView ivFrom;
    @BindView(R.id.tv_from)
    HelveRomanTextView tvFrom;
//    @BindView(R.id.rl_search)
//    AutoRelativeLayout rlSearch;

    public SearchNewsTextHolder(Context context, ViewGroup parent) {
        super(context, parent);
    }

    @Override
    public void bindHolder(SearchItemBean.RtListBean dataModel) {
        tvNewsTitle.setText(Html.fromHtml(dataModel.getTitle()));
        tvFrom.setText(dataModel.getFirstName());
        String imgFrom = dataModel.getUserPhoto();
        if (imgFrom != null) {
            ivFrom.setImageURI(Uri.parse(imgFrom));
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_news_text_search;
    }

    @OnClick(R.id.rl_search_news)
    public void onClick(View view) {
        onViewClick(view);
    }
}
