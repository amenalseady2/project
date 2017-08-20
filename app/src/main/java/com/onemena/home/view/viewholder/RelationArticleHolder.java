package com.onemena.home.view.viewholder;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arabsada.news.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onemena.base.BaseViewHolder;
import com.onemena.home.model.javabean.ArticleRelation;
import com.onemena.widght.HelveBoldTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * RelationArticleHolder Created by voler on 2017/6/7.
 * 说明：
 */

public class RelationArticleHolder extends BaseViewHolder<ArticleRelation> {
    @BindView(R.id.title)
    HelveBoldTextView title;
    @BindView(R.id.txt_relation_com)
    TextView txtRelationCom;
    @BindView(R.id.txt_relation_img)
    SimpleDraweeView txtRelationImg;
    @BindView(R.id.image)
    SimpleDraweeView image;
    @BindView(R.id.root_view)
    LinearLayout rootView;
    @BindView(R.id.txt_diver)
    TextView txtDiver;

    public RelationArticleHolder(Context context, ViewGroup parent) {
        super(context, parent);
    }

    @Override
    public void bindHolder(ArticleRelation dataModel) {

        title.setText(dataModel.getTitle());
        String uriString = dataModel.getImgUrl();
        if (uriString != null) {
            image.setImageURI(Uri.parse(uriString));
        }
        String profilePhoto = dataModel.getUserPhoto();
        if (profilePhoto != null) {
            txtRelationImg.setImageURI(Uri.parse(profilePhoto));
        }
        txtRelationCom.setText(dataModel.getFirstName());
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_homenews_detail_fragment;
    }

    @OnClick(R.id.root_view)
    public void onClick(View view) {
        onViewClick(view);
    }

    @Override
    protected void nightMode() {
        title.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
        txtRelationCom.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
        rootView.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_252525));
        txtDiver.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));

        image.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img_night);
    }

    @Override
    protected void dayMode() {
        title.setTextColor(mContext.getResources().getColor(R.color.black));
        txtRelationCom.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
        rootView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        txtDiver.setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));

        image.getHierarchy().setPlaceholderImage(R.mipmap.default_home_list_img);
    }
}
