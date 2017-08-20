package com.onemena.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.adapter.AttentionListAdapter;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.base.BaseFragment;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.data.eventbus.NightMode;
import com.onemena.listener.JsonObjectListener;
import com.onemena.listener.OnActionViewClickListener;
import com.onemena.service.PublicService;
import com.onemena.utils.SpUtil;
import com.onemena.utils.TongJiUtil;
import com.onemena.widght.HelveRomanTextView;

import de.greenrobot.event.EventBus;

/**
 * Created by WHF on 2016-12-27.
 */

public class AttentionFragment extends BaseFragment implements View.OnClickListener, OnActionViewClickListener {


    private ListView pullableListView;
    private View footer_view;
    private AttentionListAdapter adapter;
    private TextView txt_dive_bottom;
    private ImageView img_attention_add;
    private TextView txt_attention_context;
    private LinearLayout attention_title;
    private HelveRomanTextView attention_logo;
    private ImageView img_add_attention_above;
    private View inflate;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_attention, container, false);
        return inflate;
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        adapter=new AttentionListAdapter(mContext);
        adapter.setOnActionViewClickListener(this);
        pullableListView=(ListView) view.findViewById(R.id.pullable_listview_attention);
        attention_title = (LinearLayout) view.findViewById(R.id.attention_title);
        attention_logo = (HelveRomanTextView) view.findViewById(R.id.attention_logo);
        img_add_attention_above = (ImageView) view.findViewById(R.id.img_add_attention_above);
        img_add_attention_above.setOnClickListener(this);

        footer_view = View.inflate(getActivity(), R.layout.auto_head_view_attention, null);
        txt_dive_bottom = (TextView) footer_view.findViewById(R.id.txt_dive_bottom);
        txt_attention_context = (TextView) footer_view.findViewById(R.id.txt_attention_context);
        img_attention_add = (ImageView) footer_view.findViewById(R.id.img_attention_add);
        img_attention_add.setOnClickListener(this);
        pullableListView.addFooterView(footer_view);
        pullableListView.setAdapter(adapter);
        EventBus.getDefault().register(this);
        checkMode();

        getMyAttentionList();
    }

    public void getMyAttentionList() {
        PublicService.getInstance().postJsonObjectRequest(true, ConfigUrls.ATTENTION_MYLIST, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
                JSONArray content = obj.getJSONArray("content");
                adapter.notifyDataSetChanged(0,content);
            }

            @Override
            public void onObjError() {
            }
        });
    }

    private void checkMode() {
        if (SpUtil.getBoolean(SPKey.MODE,false)){
            nightMode();
        }else {
            dayMode();
        }
    }

    public void onEventMainThread(NightMode mode){
        boolean mode1 = mode.getMode();
        if (mode1){
            nightMode();
        }else {
            dayMode();
        }
        adapter.notifyDataSetChanged();

    }

    private void dayMode() {
        pullableListView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        txt_dive_bottom.setBackgroundColor(mContext.getResources().getColor(R.color.txt_e4e7f0));
        img_attention_add.setImageResource(R.mipmap.add_more);
        txt_attention_context.setTextColor(mContext.getResources().getColor(R.color.textcolor_a1a6bb));
        footer_view.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        attention_title.setBackgroundColor(mContext.getResources().getColor(R.color.main_bule));
        attention_logo.setTextColor(mContext.getResources().getColor(R.color.white));
        img_add_attention_above.setImageResource(R.mipmap.navbar_addx);
    }

    private void nightMode() {
        pullableListView.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_1b1b1b));
        txt_dive_bottom.setBackgroundColor(mContext.getResources().getColor(R.color.txt_464646));
        img_attention_add.setImageResource(R.mipmap.add_more_night);
        txt_attention_context.setTextColor(mContext.getResources().getColor(R.color.textcolor_707070));
        footer_view.setBackgroundColor(mContext.getResources().getColor(R.color.nightbg_1b1b1b));
        attention_title.setBackgroundColor(mContext.getResources().getColor(R.color.txt_234A7D));
        attention_logo.setTextColor(mContext.getResources().getColor(R.color.nighttxt_707070));
        img_add_attention_above.setImageResource(R.mipmap.navbar_add_night);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackStackChanged() {
        super.onBackStackChanged();
        getMyAttentionList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_add_attention_above:
            case R.id.img_attention_add:
                AddAttentionFragment fragment=AddAttentionFragment.newInstance();
                addToBackStack(fragment);
                break;
            case R.id.item_btn_attention:
                Integer position=(Integer) v.getTag();
                JSONObject jsonObject = adapter.getData().getJSONObject(position);
                String id = jsonObject.getString("id");
                Boolean attentionState = jsonObject.getBoolean("attentionState");
                if (attentionState){
                    attentionState=false;
                    TongJiUtil.getInstance().putEntries(TJKey.FOLLOW_HEADLINES, MyEntry.getIns(TJKey.TYPE,"1"),MyEntry.getIns(TJKey.HEADLINES_ID,id));
                }else {
                    attentionState=true;
                    TongJiUtil.getInstance().putEntries(TJKey.FOLLOW_HEADLINES, MyEntry.getIns(TJKey.TYPE,"0"),MyEntry.getIns(TJKey.HEADLINES_ID,id));
                }
                adapter.getData().getJSONObject(position).put("attentionState",attentionState);
                adapter.notifyDataSetChanged();
                addAttention(id);
                break;
        }
    }

    /*
    添加关注方法
     */
    private void addAttention(String mId) {
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.ADD_ATTENTION+mId, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
            }

            @Override
            public void onObjError() {

            }
        });
    }

}
