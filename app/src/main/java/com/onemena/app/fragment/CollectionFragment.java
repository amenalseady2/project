package com.onemena.app.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.adapter.CollectionAdapter;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.SPKey;
import com.onemena.app.refresh.PullToRefreshLayout;
import com.onemena.app.refresh.PullableListView;
import com.onemena.base.BaseFragment;
import com.onemena.listener.JsonObjectListener;
import com.onemena.listener.OnActionViewClickListener;
import com.onemena.service.PublicService;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.widght.HelveRomanTextView;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/10/26.
 */
public class CollectionFragment extends BaseFragment implements OnActionViewClickListener, PullToRefreshLayout.OnRefreshListener, HomeNewsDetailFragment.OnGetLikeListener {


    private PullableListView pullable_listview;
    private CollectionAdapter itemAdapter;
    private PullToRefreshLayout refreshLayout;
    private RelativeLayout rl_unsuccess;
    private LinearLayout ll_no_collection;
    private LinearLayout ll_delete;
    private String titleId = "666";
    private HelveRomanTextView tv_edit;
    private TextView tv_delete_count;
    private ImageView back_iv;
    private ImageView iv_null;
    private boolean isEdit = false;
    private ArrayList<String> mList;
    private RelativeLayout rl_content;
    private HelveRomanTextView tv_title;
    private HelveRomanTextView tv_null_des;
    private RelativeLayout head_detail_view;
    private ImageView line;

    public static CollectionFragment getInstance() {
        return new CollectionFragment();
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collection, container, false);
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        //初始化下拉刷新LV
        pullable_listview = (PullableListView) view.findViewById(R.id.pullable_listview_home);
        itemAdapter = new CollectionAdapter(mContext);
        itemAdapter.setOnActionViewClickListener(this);//点击事件

        pullable_listview.setAdapter(itemAdapter);

        refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refreashLayout);


        rl_unsuccess = (RelativeLayout) view.findViewById(R.id.rl_unsuccess);
        rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
        head_detail_view = (RelativeLayout) view.findViewById(R.id.head_detail_view);
        ll_no_collection = (LinearLayout) view.findViewById(R.id.ll_no_collection);
        ll_delete = (LinearLayout) view.findViewById(R.id.ll_delete);

        rl_unsuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(mContext);
                rl_unsuccess.setVisibility(View.GONE);
            }
        });
        tv_edit = (HelveRomanTextView) view.findViewById(R.id.tv_edit);
        tv_title = (HelveRomanTextView) view.findViewById(R.id.tv_title);
        tv_delete_count = (TextView) view.findViewById(R.id.tv_delete_count);
        back_iv = (ImageView) view.findViewById(R.id.back_iv);
        iv_null = (ImageView) view.findViewById(R.id.iv_null);
        line = (ImageView) view.findViewById(R.id.line);
        tv_null_des = (HelveRomanTextView) view.findViewById(R.id.tv_null_des);

        refreshLayout.setCannotPullDown();
        refreshLayout.setOnRefreshListener(this);
        tv_edit.setOnClickListener(this);
        back_iv.setOnClickListener(this);
        tv_delete_count.setOnClickListener(this);

        showProgressDialog(mContext);
        getListDataFromNet(0);


        changeMode();

    }


    private void getListDataFromNet(final int size) {
        PublicService.getInstance().postJsonObjectRequest(true, ConfigUrls.FAVOURITE_LIST + size, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
                dismissProgressDialog();
                try {
                    JSONArray data = obj.getJSONArray("content");
                    itemAdapter.notifyDataSetChanged(size, false, data);

                    if (data.size() > 0) {
                        if (!SpUtil.getBoolean(SPKey.FIRST_UPDATE, false)) {
//                            TongJiUtil.getInstance().putEntries("first_update");
                            SpUtil.saveValue(SPKey.FIRST_UPDATE, true);
                        }
                        rl_unsuccess.setVisibility(View.INVISIBLE);
                        ll_no_collection.setVisibility(View.INVISIBLE);
                        refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

                    } else {
                        if (itemAdapter.getData().size() == 0) {
                            ll_no_collection.setVisibility(View.VISIBLE);
                        }
                        refreshLayout.loadmoreFinish(PullToRefreshLayout.NOTMORE);
                    }

                } catch (Exception e) {
                    refreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
            }

            @Override
            public void onObjError() {
                SpUtil.saveValue(SPKey.FIRST_UPDATE, true);
                dismissProgressDialog();
                refreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                if (itemAdapter.getData().size() == 0) {
                    //下拉刷新的时候
                    rl_unsuccess.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void refresh() {
        getListDataFromNet(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_edit:
                if (itemAdapter.getData().size() != 0) {
                    editChange();
                }
                break;
            case R.id.tv_delete_count:
                showProgressDialog(mContext, R.string.delloading_dialog);
                delSaveNews();
                editChange();
                break;
            case R.id.iv_check:
                getDelCount();
                break;
            case R.id.back_iv:
                popBackStack();
                break;
            case R.id.style1:
                String id_1 = (String) v.getTag();
                String[] sp_1 = StringUtils.split(id_1, "@");
//                bundle_1.putString("unique_id", sp_1[0]);
                HomeNewsDetailFragment instance_1 = HomeNewsDetailFragment.newInstance(sp_1[1],
                        titleId, sp_1[2],"5", "");
                instance_1.setOnGetLikeListener(this);
                addToBackStack(instance_1);
                break;
            case R.id.style2:
                String id_2 = (String) v.getTag();
                String[] sp_2 = StringUtils.split(id_2, "@");
                HomeNewsDetailFragment instance_2 = HomeNewsDetailFragment.newInstance(sp_2[1],
                        titleId, sp_2[2], "5", "");
                instance_2.setOnGetLikeListener(this);
                addToBackStack(instance_2);
                break;
            case R.id.style3:
                String id_3 = (String) v.getTag();
                String[] sp_3 = StringUtils.split(id_3, "@");
                HomeNewsDetailFragment instance_3 = HomeNewsDetailFragment.newInstance(
                        sp_3[1], titleId,sp_3[2] ,"5" , "");
                instance_3.setOnGetLikeListener(this);
                addToBackStack(instance_3);
                break;
            case R.id.style4:
                String id_4 = (String) v.getTag();
                String[] sp_4 = StringUtils.split(id_4, "@");
                HomeNewsDetailFragment instance_4 = HomeNewsDetailFragment.newInstance(
                        sp_4[1], titleId, sp_4[2],"5" , "");
                instance_4.setOnGetLikeListener(this);

                addToBackStack(instance_4);
                break;
        }

    }

    private void editChange() {
        isEdit = !isEdit;
        if (isEdit) {
            tv_edit.setText(getResources().getString(R.string.cancel));
            ll_delete.setVisibility(View.VISIBLE);
            tv_delete_count.setText(getResources().getString(R.string.delete));
            tv_delete_count.setTextColor(getResources().getColor(R.color.textcolor_64697b));
            changeModeTextColor(R.color.textcolor_64697b, R.color.nighttxt_707070, tv_delete_count);
        } else {
            tv_edit.setText(getResources().getString(R.string.edit));
            ll_delete.setVisibility(View.GONE);
        }
        itemAdapter.notifyDataSetChanged(isEdit);
    }

    //收藏或者取消收藏
    private void delSaveNews() {
        HashMap<String, String> params = new HashMap<>();
        addId(params);
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.NEWS_DEL_FAV, params, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
                getListDataFromNet(0);
            }

            @Override
            public void onObjError() {
                dismissProgressDialog();
            }
        });
    }

    private void addId(HashMap<String, String> params) {
        int i = 0;
        for (String id : mList) {
            params.put("id[" + i + "]", id);
            i++;
        }
    }

    private void getDelCount() {
        JSONArray data = itemAdapter.getData();
        if (mList != null) {
            mList.clear();
        } else {
            mList = new ArrayList<>();
        }
        for (int i = 0; i < data.size(); i++) {
            JSONObject jsonObject = data.getJSONObject(i);
            Boolean isCheck = jsonObject.getBoolean("isCheck");
            if (isCheck != null && isCheck) {
                mList.add(jsonObject.getString("id"));
            }
        }
        if (mList.size() > 0) {
            tv_delete_count.setText(getResources().getString(R.string.delete) + "(" + StringUtils.int2IndiaNum("" + mList.size()) + ")");
            tv_delete_count.setTextColor(getResources().getColor(R.color.txt_EB4E35));
        } else {
            tv_delete_count.setText(getResources().getString(R.string.delete));
            tv_delete_count.setTextColor(mNightMode ? getResources().getColor(R.color.nighttxt_707070) : getResources().getColor(R.color.textcolor_64697b));
        }
    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        getListDataFromNet(itemAdapter.getCount());
    }

    private void changeMode() {
        if (mNightMode) {
            pullable_listview.setDivider(new ColorDrawable(Color.parseColor("#464646")));
        } else {
            pullable_listview.setDivider(new ColorDrawable(Color.parseColor("#E4E7F0")));
        }
        changeModeTextColor(R.color.white, R.color.nighttxt_707070, tv_edit, tv_title);
        changeModeTextColor(R.color.textcolor_64697b, R.color.nighttxt_707070, tv_delete_count);
        changeModeBackgroud(R.color.textcolor_a9a9a9, R.color.nighttxt_707070, line);
        changeModeTextColor(R.color.textcolor_a1a6bb, R.color.txt_464646, tv_null_des);
        changeModeBackgroud(R.color.collection_del_day, R.color.collection_del_night, tv_delete_count);
        changeModeImage(R.mipmap.details_navbar_back, R.mipmap.details_navbar_back_night, back_iv);
        changeModeImage(R.mipmap.img, R.mipmap.img_night, iv_null);
        changeModeBackgroud(R.color.activity_bg_f5, R.color.nightbg_1b1b1b, rl_content);
        changeModeBackgroud(R.color.main_bule, R.color.txt_234A7D, head_detail_view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setLikeNumCallback(String numCallback, String position) {
        if (StringUtils.isNotEmpty(position)) {
            itemAdapter.getData().getJSONObject(Integer.parseInt(position)).put("like", numCallback);
            itemAdapter.notifyDataSetChanged();
        }

    }
}
