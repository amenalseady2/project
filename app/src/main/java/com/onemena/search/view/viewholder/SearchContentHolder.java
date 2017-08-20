package com.onemena.search.view.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.arabsada.news.R;
import com.onemena.app.config.SPKey;
import com.onemena.base.BaseViewHolder;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.widght.HelveRomanTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/10.
 */

public class SearchContentHolder extends BaseViewHolder<String> {
    @BindView(R.id.tv_search_content)
    HelveRomanTextView tvSearchContent;
    @BindView(R.id.tv_search_content_id)
    HelveRomanTextView tvSearchContentId;

    public SearchContentHolder(Context context, ViewGroup parent) {
        super(context, parent);
    }

    @Override
    public void bindHolder(String dataModel) {
        tvSearchContent.setText(dataModel);
        tvSearchContentId.setText(StringUtils.int2IndiaNum(String.valueOf(position+1)));

        if (SpUtil.getBoolean(SPKey.MODE,false)) {
            switch (position) {
                case 0:
                    tvSearchContentId.setBackgroundColor(mContext.getResources().getColor(R.color.color_8E433C));
                    break;
                case 1://color_E17100
                    tvSearchContentId.setBackgroundColor(mContext.getResources().getColor(R.color.color_995F33));
                    break;
                case 2:
                    tvSearchContentId.setBackgroundColor(mContext.getResources().getColor(R.color.color_997C33));
                    break;
                default:
                    tvSearchContentId.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
                    break;
            }
        }else {
            switch (position) {
                case 0:
                    tvSearchContentId.setBackgroundColor(mContext.getResources().getColor(R.color.color_E42B18));
                    break;
                case 1://color_E17100
                    tvSearchContentId.setBackgroundColor(mContext.getResources().getColor(R.color.color_E17100));
                    break;
                case 2:
                    tvSearchContentId.setBackgroundColor(mContext.getResources().getColor(R.color.color_E1B900));
                    break;
                default:
                    tvSearchContentId.setBackgroundColor(mContext.getResources().getColor(R.color.color_CCCCCC));
                    break;
            }
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_search_hot;
    }

    @Override
    protected void nightMode() {

    }

    @Override
    protected void dayMode() {

    }

    @OnClick(R.id.tv_search_content)
    public void onClick(View view) {
        onViewClick(view);
    }
}
