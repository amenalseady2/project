package com.onemena.video.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.activity.MainActivity;
import com.onemena.app.config.Config;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.app.refresh.PullToRefreshLayout;
import com.onemena.app.refresh.PullableListView;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.data.eventbus.NightMode;
import com.onemena.data.eventbus.RefreshBean;
import com.onemena.base.BaseFragment;
import com.onemena.app.fragment.CommentFragment;
import com.onemena.listener.JsonObjectListener;
import com.onemena.listener.OnActionViewClickListener;
import com.onemena.service.PublicService;
import com.nineoldandroids.animation.ObjectAnimator;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.utils.TaskUtil;
import com.onemena.utils.TongJiUtil;
import com.onemena.video.view.activity.VideoNewsDetailActivity;
import com.onemena.video.view.adapter.VideoPagerRefushLVAdapter;
import com.onemena.widght.PopwinVideoListSetting;

import java.util.Map;

import de.greenrobot.event.EventBus;


/**
 * Created by Administrator on 2016/10/28.
 */
public class VideoPagerFragment extends BaseFragment implements OnActionViewClickListener, PullToRefreshLayout.OnRefreshListener {

    public static final String TITLEID = "titleId";

    private PullableListView pullable_listview;
    private VideoPagerRefushLVAdapter itemAdapter;
    private PullToRefreshLayout refreshLayout;
    private String category_id;
    private RelativeLayout video_rl_unsuccess;
    private boolean isViewPrepared;
    private boolean isPulled = false;
    private ImageView img_video_not_data;
    private TextView txt_video_not_data;
    private PopwinVideoListSetting videoListSetting;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.videofragment_item, container, false);
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        category_id = arguments.getString(TITLEID);
//        TongJiUtil.getInstance().putEntries("video_category_change",
//                MyEntry.getIns("category_id",category_id));
        //初始化下拉刷新LV
        pullable_listview = (PullableListView) view.findViewById(R.id.pullable_listview_video);
        video_rl_unsuccess = (RelativeLayout) view.findViewById(R.id.video_rl_unsuccess);
        img_video_not_data = (ImageView) view.findViewById(R.id.img_video_not_data);
        txt_video_not_data = (TextView) view.findViewById(R.id.txt_video_not_data);
        itemAdapter = new VideoPagerRefushLVAdapter(getActivity(), category_id);
        itemAdapter.setOnActionViewClickListener(this);//点击事件
        pullable_listview.setAdapter(itemAdapter);
        refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refreashLayout);
        refreshLayout.setOnRefreshListener(this);
        video_rl_unsuccess.setOnClickListener(this);

        //注册EventBus
        EventBus.getDefault().register(this);
        checkMode();
    }

    private void checkMode() {
        if (SpUtil.getBoolean(SPKey.MODE, false)) {
            video_rl_unsuccess.setBackgroundColor(getResources().getColor(R.color.nightbg_252525));
            img_video_not_data.setImageResource(R.mipmap.details_failed_to_load_img_night);
            txt_video_not_data.setTextColor(getResources().getColor(R.color.textcolor_707070));

        } else {
            video_rl_unsuccess.setBackgroundColor(getResources().getColor(R.color.activity_bg_f5));
            img_video_not_data.setImageResource(R.mipmap.details_failed_to_load_img);
            txt_video_not_data.setTextColor(getResources().getColor(R.color.textcolor_a1a6bb));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isPulled)
                pullRefresh(); // 在此请求数据
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isViewPrepared = true;
        if (!isPulled){

            pullRefresh();
        }

        if (videoListSetting != null) {
            if (videoListSetting.isShare()) {
                TaskUtil.sendTask(mContext,"share_news");
                videoListSetting.setShare(false);
            }
        }
    }


    private void pullRefresh() {
        if (getUserVisibleHint() && isViewPrepared) {
            pullable_listview.setSelectionAfterHeaderView();
            refreshLayout.refresh();
            isPulled = true;
        }
    }


    //当前fragment隐藏
    public void hidden() {
//        JCVideoPlayer3 jcVideoPlayer = itemAdapter.getJcVideoPlayer();
//        if (jcVideoPlayer !=null){
//            jcVideoPlayer.playerPause(false);
//        }
    }

    private void getDataFromNet(final int changeItemNum, int size, final boolean isLoadMore) {
        PublicService.getInstance().postJsonObjectRequest(true, ConfigUrls.VIDEO_LIST_DU + category_id + "/" + size, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
                JSONArray content = obj.getJSONArray("content");
                if (isCacheData) {
                    refreshLayout.refreshFinish(PullToRefreshLayout.FAIL, 0, isLoadMore);
                } else {
                    refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED, content.size(), isLoadMore);
                }
                if (content.size() > 0) {
                    video_rl_unsuccess.setVisibility(View.INVISIBLE);
                } else {
                    if (changeItemNum == 0 && itemAdapter.getData().size() == 0) {
                        video_rl_unsuccess.setVisibility(View.VISIBLE);
                    } else if (changeItemNum != 0) {
                        //上拉加载更多的时候，没有数据了，不做任何动作
                        refreshLayout.refreshFinish(PullToRefreshLayout.FAIL, 0, isLoadMore);
                    }
                }
                itemAdapter.notifyDataSetChanged(changeItemNum, content);
//                dismissProgressDialog();

            }

            @Override
            public void onObjError() {
                refreshLayout.refreshFinish(PullToRefreshLayout.FAIL, 0, isLoadMore);
                if (changeItemNum == 0) {
                    video_rl_unsuccess.setVisibility(View.VISIBLE);
                } else {
                    video_rl_unsuccess.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_share:
                /*
                Activity context,BaseFragment baseFragment,
                                  String shareTitle, String shareDesc , String shareMainPic,
                                  String shareUrl,String category_id ,String come,int position,
                                  VideoPagerRefushLVAdapter itemAdapter,
                                  PullableListView pullable_listview,String video_id
                 */
                ObjectAnimator.ofFloat(v, "scaleX", 1.0f, 1.1f, 1.0f).setDuration(200).start();
                ObjectAnimator.ofFloat(v, "scaleY", 1.0f, 1.1f, 1.0f).setDuration(200).start();
//                int position=Integer.parseInt(v.getTag().toString());
                String[] split = StringUtils.split(v.getTag().toString(), "@");
                int position = Integer.parseInt(split[0]);
                String id = itemAdapter.getData().getJSONObject(position).getString("id");
                String categories = itemAdapter.getData().getJSONObject(position).getString("categories");
                String video_url = itemAdapter.getData().getJSONObject(position).getString("video_url");
                String title_video = itemAdapter.getData().getJSONObject(position).getString("title");
                String share_url = itemAdapter.getData().getJSONObject(position).getString("share_url");
                videoListSetting = new PopwinVideoListSetting(mContext, this, title_video, "", "",
                        share_url, categories, Config.FIREBAEVIDEOTYPE, position, itemAdapter, pullable_listview, id);
                videoListSetting.show(v);
                break;
            case R.id.video_rl_unsuccess:
//                showProgressDialog(getContext());
                video_rl_unsuccess.setVisibility(View.GONE);
                getDataFromNet(0, 0, false);
                break;
            case R.id.video_item_img:
//                VideoNewsDetailActivity videoNewsDetailFragment=VideoNewsDetailActivity.getInstance("55118");
//                addToBackStack(videoNewsDetailFragment);

                Intent intent = new Intent(mContext, VideoNewsDetailActivity.class);
                String[] split2 = StringUtils.split(v.getTag().toString(), "@");
                intent.putExtra(VideoNewsDetailActivity.ID, split2[1]);
                intent.putExtra(VideoNewsDetailActivity.POSITION, split2[0]);
                intent.putExtra(VideoNewsDetailActivity.NEWS_TYPE, "1");
                intent.putExtra(VideoNewsDetailActivity.CATEGORY_ID, category_id);
                startActivity(intent);
                TongJiUtil.getInstance().putStatistics(
                        MyEntry.getIns(TJKey.DOCTYPE, "video"),
                        MyEntry.getIns(TJKey.ACTYPE, "click"),
                        MyEntry.getIns(TJKey.DOCID, split2[1]));
                break;
            case R.id.video_comment_img:
                String[] split1 = StringUtils.split(v.getTag().toString(), "@");
                String s = split1[2];
                if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
                    s="0";
                }
                CommentFragment commentFragment = CommentFragment.getInstance();
                Bundle bundle_s = new Bundle();
                bundle_s.putString(CommentFragment.ID, split1[0]);
                bundle_s.putInt(CommentFragment.FLAG, 1);
                bundle_s.putString(CommentFragment.CATEGORY_ID, category_id);
                bundle_s.putString(CommentFragment.TITLE, split1[1]);
                bundle_s.putInt(CommentFragment.COMMENT_COUNT, Integer.parseInt(s));
                bundle_s.putString("img_url", split1[3]);
                commentFragment.setArguments(bundle_s);
                addToBackStack(commentFragment);


                break;
        }
    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        TongJiUtil.getInstance().putEntries(TJKey.UPDATE,
                MyEntry.getIns(TJKey.CATEGORY_ID, category_id),
                MyEntry.getIns(TJKey.TYPE, Config.FIREBAEVIDEOTYPE));
        TongJiUtil.getInstance().putStatistics(
                MyEntry.getIns(TJKey.DOCTYPE, "video"),
                MyEntry.getIns(TJKey.ACTYPE, "downFresh"),
                MyEntry.getIns(TJKey.CATGID, category_id));
        getDataFromNet(0, 0, false);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        TongJiUtil.getInstance().putEntries(TJKey.LOAD_MORE,
                MyEntry.getIns(TJKey.CATEGORY_ID, category_id),
                MyEntry.getIns(TJKey.TYPE, Config.FIREBAEVIDEOTYPE));
        TongJiUtil.getInstance().putStatistics(
                MyEntry.getIns(TJKey.DOCTYPE, "video"),
                MyEntry.getIns(TJKey.ACTYPE, "upLoad"),
                MyEntry.getIns(TJKey.CATGID, category_id));
        getDataFromNet(itemAdapter.getData().size(), itemAdapter.getData().size(),true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    private void sendDataToNet(int i, String mId) {
        switch (i) {
            case 2://点赞
                setLike(mId);
                break;
            case 3://点不赞
                setDislike(mId);
                break;
        }
    }

    private void setDislike(String mId) {
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.NEWS_SET_DISLIKE + mId, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
            }

            @Override
            public void onObjError() {
            }
        });
    }

    private void setLike(String mId) {
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.NEWS_SET_LIKE + mId, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
            }

            @Override
            public void onObjError() {
            }
        });
    }

    //EventBus的方法
    public void onEventMainThread(Map<String, String> event) {
        String type = event.get("type");
        String news_ids = event.get("news_id");
        String flag = event.get("flag");
        if ("1".equals(flag)) {
            TongJiUtil.getInstance().putEntries(type,
                    MyEntry.getIns(TJKey.CATEGORY_ID, category_id),
                    MyEntry.getIns(TJKey.RESOURCE_ID, news_ids));
        }
        itemAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(NightMode mode) {
        itemAdapter.notifyDataSetChanged();
        checkMode();
    }

    public void onEventMainThread(JSONObject object) {
        String category_id = object.getString("category_id");
        if (StringUtils.isNotEmpty(category_id) && category_id.equals(this.category_id)) {
            String position = object.getString("position");
            if (StringUtils.isNotEmpty(position)) {
                itemAdapter.getData().getJSONObject(Integer.parseInt(position)).put("comment_count", object.getString("comment_count"));
                itemAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onEventMainThread(RefreshBean refreshBean) {
        if (refreshBean.getFlag() != 1) return;
        if ((refreshLayout.getState() & (PullToRefreshLayout.REFRESHING)) == 0) {
            pullRefresh();
        }
    }

}