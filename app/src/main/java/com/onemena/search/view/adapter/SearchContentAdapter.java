package com.onemena.search.view.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.onemena.base.BaseAdapter;
import com.onemena.base.BaseViewHolder;
import com.onemena.search.view.viewholder.SearchContentHistoryHolder;
import com.onemena.search.view.viewholder.SearchContentHolder;

/**
 * Created by Administrator on 2017/4/10.
 */

public class SearchContentAdapter extends BaseAdapter<String> {
    private int Type;
    public SearchContentAdapter(Context mContext,int Type) {
        super(mContext);
        this.Type=Type;
    }

    @Override
    public BaseViewHolder getHolder(int position, ViewGroup parent) {

        BaseViewHolder baseViewHolder = null;
        switch (Type) {
            case 0:
                baseViewHolder = new SearchContentHolder(mContext,parent);
                break;
            case 1:
                baseViewHolder= new SearchContentHistoryHolder(mContext,parent);
                break;
        }
        return baseViewHolder;
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    @Override
    public void changeData(String data) {

    }
}
