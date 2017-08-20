package com.onemena.search.view.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.onemena.base.BaseAdapter;
import com.onemena.base.BaseViewHolder;
import com.onemena.search.model.javabean.SearchItemBean;
import com.onemena.search.view.viewholder.SearchNewsHolder;
import com.onemena.search.view.viewholder.SearchNewsTextHolder;
import com.onemena.search.view.viewholder.SearchVideoHolder;
import com.onemena.utils.StringUtils;

/**
 * Created by WHF on 2017-04-08.
 */

public class SearchListAdapter extends BaseAdapter<SearchItemBean.RtListBean> {

    final int VIDEO_TYPE = 0;
    final int NEWS_IMAGE_TYPE = 1;
    final int NEWS_TEXT_TYPE = 2;

    private String searchId;

    public SearchListAdapter(Context mContext) {
        super(mContext);
    }


    @Override
    public int getItemViewType(int position) {
        SearchItemBean.RtListBean rtListBean = data.get(position);
        String dType = rtListBean.getDType();
        if ("v".equals(dType))
            return VIDEO_TYPE;
        else {
            String img_url = rtListBean.getImg_url();
            if (StringUtils.isEmpty(img_url))
                return NEWS_TEXT_TYPE;
            else
                return NEWS_IMAGE_TYPE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public SearchItemBean.RtListBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public BaseViewHolder getHolder(int position, ViewGroup parent) {
        int type = getItemViewType(position);
        BaseViewHolder baseViewHolder=null;
        switch (type) {
            case VIDEO_TYPE:
                baseViewHolder=new SearchVideoHolder(mContext,parent);
                break;
            case NEWS_TEXT_TYPE:
                baseViewHolder=new SearchNewsTextHolder(mContext,parent);
                break;
            case NEWS_IMAGE_TYPE:
                baseViewHolder=new SearchNewsHolder(mContext,parent);
                break;
        }

        return baseViewHolder;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    @Override
    public void changeData(SearchItemBean.RtListBean bean) {
        if (StringUtils.isEmpty(bean.getSearchId())) {
            bean.setSearchId(searchId);
        }
    }
}
