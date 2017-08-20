package com.onemena.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.adapter.AttentionListAddAdapter;
import com.onemena.app.adapter.AttentionTitleAdapter;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.base.BaseFragment;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.listener.JsonObjectListener;
import com.onemena.listener.OnActionViewClickListener;
import com.onemena.service.PublicService;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.utils.TongJiUtil;

import org.apache.commons.lang3.BooleanUtils;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/28.
 */
public class AddAttentionFragment extends BaseFragment implements OnActionViewClickListener {

    private ListView listview_title;
    private AttentionTitleAdapter titleAdapter;
    private ListView pullable_listview_addattention;
    private AttentionListAddAdapter listAddAdapter;
    private HashMap<String, Boolean> states = new HashMap<String, Boolean>();
    private ImageView img_back_attention;

    public static AddAttentionFragment newInstance() {
        return new AddAttentionFragment();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = null;
        if (SpUtil.getBoolean(SPKey.MODE, false)) {
            inflate = View.inflate(getContext(), R.layout.fragment_addattention_night, null);
        } else {
            inflate = View.inflate(getContext(), R.layout.fragment_addattention, null);
        }

        return inflate;
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {

        listview_title = (ListView) view.findViewById(R.id.listview_title);
        pullable_listview_addattention = (ListView) view.findViewById(R.id.pullable_listview_addattention);
        img_back_attention = (ImageView) view.findViewById(R.id.img_back_attention);
        img_back_attention.setOnClickListener(this);
        listAddAdapter = new AttentionListAddAdapter(mContext);
        listAddAdapter.setOnActionViewClickListener(this);
        pullable_listview_addattention.setAdapter(listAddAdapter);
        states.put("0", true);
        titleAdapter = new AttentionTitleAdapter(mContext, states);
        titleAdapter.setOnActionViewClickListener(this);
        listview_title.setAdapter(titleAdapter);
        getAttentionTitle();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.item_annention_title:
                Integer position = (Integer) v.getTag();

                for (String key : states.keySet()) {
                    states.put(key, false);
                }
                states.put(String.valueOf(position), true);

                JSONObject jsonObject = titleAdapter.getData().getJSONObject(position);
                titleAdapter.notifyDataSetChanged();
                String id = jsonObject.getString("id");
                getAttentionList(id);
                break;
            case R.id.item_btn_attention:
                String[] split = StringUtils.split(v.getTag().toString(), "@");
                int position_a = Integer.parseInt(split[0].toString());
                addAttention(split[1]);
                String exits = listAddAdapter.getData().getJSONObject(position_a).getString("exits");
                if (BooleanUtils.toBoolean(exits)) {
                    listAddAdapter.getData().getJSONObject(position_a).put("exits", false);
                    TongJiUtil.getInstance().putEntries(TJKey.FOLLOW_HEADLINES, MyEntry.getIns(TJKey.TYPE,"1"),MyEntry.getIns(TJKey.HEADLINES_ID,split[1]));
                } else {
                    listAddAdapter.getData().getJSONObject(position_a).put("exits", true);
                    TongJiUtil.getInstance().putEntries(TJKey.FOLLOW_HEADLINES, MyEntry.getIns(TJKey.TYPE,"0"),MyEntry.getIns(TJKey.HEADLINES_ID,split[1]));
                }
                listAddAdapter.notifyDataSetChanged();
                break;
            case R.id.img_back_attention:
                popBackStack();
                break;
        }

    }

    /*
    添加关注方法
     */
    private void addAttention(String mId) {
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.ADD_ATTENTION + mId, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {

            }

            @Override
            public void onObjError() {

            }
        });
    }

    /*
    获取相关频道内容列表
     */
    private void getAttentionList(String id) {
        PublicService.getInstance().postJsonObjectRequest(true, ConfigUrls.ADD_ATTENTION_LIST + id, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
                JSONArray content = obj.getJSONArray("content");
                listAddAdapter.notifyDataSetChanged(0, content);
            }

            @Override
            public void onObjError() {

            }
        });
    }

    /*
    获取频道列表
     */
    private void getAttentionTitle() {
        PublicService.getInstance().postJsonObjectRequest(true, ConfigUrls.ATTENTION_TITLE_LIST, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
                JSONArray content = obj.getJSONArray("content");

                titleAdapter.notifyDataSetChanged(0, content);
                String id = content.getJSONObject(0).getString("id");
                getAttentionList(id);

            }

            @Override
            public void onObjError() {

            }
        });
    }

}
