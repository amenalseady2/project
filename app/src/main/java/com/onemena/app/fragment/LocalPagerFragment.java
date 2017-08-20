package com.onemena.app.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.adapter.LocalPagerRefushLVAdapter;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.base.BaseFragment;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.data.eventbus.NightMode;
import com.onemena.listener.OnActionViewClickListener;
import com.onemena.utils.FileUtils;
import com.onemena.utils.NetworkUtil;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.utils.ToastUtil;
import com.onemena.utils.TongJiUtil;
import com.onemena.video.view.activity.VideoNewsDetailActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/10/26.
 */
public class LocalPagerFragment extends BaseFragment implements OnActionViewClickListener{


    private ListView pullable_listview;
    private LocalPagerRefushLVAdapter itemAdapter;
    private JSONObject localJson;
    private JSONArray content;

    public static LocalPagerFragment getInstance() {
        return new LocalPagerFragment();
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_local_fragment, container, false);
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        pullable_listview = (ListView) view.findViewById(R.id.pullable_listview_home);
        itemAdapter = new LocalPagerRefushLVAdapter(getContext());
        itemAdapter.setOnActionViewClickListener(this);//点击事件


        content = FileUtils.getLocalJsonArray(mContext, R.raw.article);
//        content = localJson.getJSONArray("content");

        View v = LayoutInflater.from(mContext).inflate(
                R.layout.home_local_fragment_footer, null);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TongJiUtil.getInstance().putEntries(TJKey.DEP_CLICK);

                if (NetworkUtil.checkNetWork(mContext)) {
                    EventBus.getDefault().post("1");
                }else {
                    ToastUtil.showDevShortToast(getString(R.string.please_check_your_network));
                }
            }
        });
        pullable_listview.addFooterView(v);
        pullable_listview.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged(0,content);
        EventBus.getDefault().register(this);

        Boolean mode = SpUtil.getBoolean(SPKey.MODE, false);
        if (mode) {
            nightMode();
        } else {
            dayMode();
        }


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.style1:
                Bundle bundle_1 = (Bundle) v.getTag();
                LocalNewsDetailFragment instance_1 = LocalNewsDetailFragment.getInstance();
                instance_1.setArguments(bundle_1);
                addToBackStack(instance_1);
//                TongJiUtil.getInstance().putStatistics(
//                        MyEntry.getIns("acType", "click"),
//                        MyEntry.getIns("docId", bundle_1.getString("id")));
//
                TongJiUtil.getInstance().putEntries(TJKey.DEP_READ,
                        MyEntry.getIns(TJKey.RESOURCE_ID,bundle_1.getString("id")));

                break;
            case R.id.style2:
                Intent intent = new Intent(mContext, VideoNewsDetailActivity.class);
                String[] split2 = StringUtils.split(v.getTag().toString(), "@");
                intent.putExtra(VideoNewsDetailActivity.ID,split2[1]);
                intent.putExtra(VideoNewsDetailActivity.POSITION,split2[2]);
                startActivity(intent);

//                TongJiUtil.getInstance().putStatistics(
//                        MyEntry.getIns("acType", "click"),
//                        MyEntry.getIns("docId", split2[1]));
                break;
            case R.id.style3:
                Bundle bundle_3 = (Bundle) v.getTag();
                LocalNewsDetailFragment instance_3 = LocalNewsDetailFragment.getInstance();
                instance_3.setArguments(bundle_3);
                addToBackStack(instance_3);
//                TongJiUtil.getInstance().putStatistics(
//                        MyEntry.getIns("acType", "click"),
//                        MyEntry.getIns("docId", bundle_3.getString("id")));
                TongJiUtil.getInstance().putEntries(TJKey.DEP_READ,
                        MyEntry.getIns(TJKey.RESOURCE_ID,bundle_3.getString("id")));
                break;
            case R.id.style4:
                Bundle bundle = (Bundle) v.getTag();
                LocalNewsDetailFragment instance_4 = LocalNewsDetailFragment.getInstance();
                instance_4.setArguments(bundle);
                addToBackStack(instance_4);
//                TongJiUtil.getInstance().putStatistics(
//                        MyEntry.getIns("acType", "click"),
//                        MyEntry.getIns("docId", bundle.getString("id")));
                TongJiUtil.getInstance().putEntries(TJKey.DEP_READ,
                        MyEntry.getIns(TJKey.RESOURCE_ID,bundle.getString("id")));
                break;
            case R.id.simple_img:
                Bundle bundle2 = (Bundle) v.getTag();
                LocalNewsDetailFragment instance2 = LocalNewsDetailFragment.getInstance();
                instance2.setArguments(bundle2);
                addToBackStack(instance2);
                break;

        }

    }

    public void onEventMainThread(NightMode mode) {
        boolean mode1 = mode.getMode();
        if (mode1) {
            nightMode();
        } else {
            dayMode();
        }
        itemAdapter.notifyDataSetChanged();
    }

    private void nightMode() {
        pullable_listview.setDivider(new ColorDrawable(Color.parseColor("#464646")));
    }

    private void dayMode() {
        pullable_listview.setDivider(new ColorDrawable(Color.parseColor("#E4E7F0")));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
