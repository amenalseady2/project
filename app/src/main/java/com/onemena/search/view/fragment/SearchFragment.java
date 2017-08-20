package com.onemena.search.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.SPKey;
import com.onemena.app.refresh.PullToRefreshLayout;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.base.BaseFragment;
import com.onemena.app.fragment.HomeNewsDetailFragment;
import com.onemena.listener.JsonObjectListener;
import com.onemena.listener.VolleyStringListener;
import com.onemena.service.PublicService;
import com.onemena.base.BaseAdapter;
import com.onemena.search.model.javabean.SearchItemBean;
import com.onemena.search.view.adapter.SearchContentAdapter;
import com.onemena.search.view.adapter.SearchListAdapter;
import com.onemena.utils.GetPhoneInfoUtil;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.utils.ToastUtil;
import com.onemena.utils.TongJiUtil;
import com.onemena.video.view.activity.VideoNewsDetailActivity;
import com.onemena.widght.HelveRomanTextView;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/8.
 */

public class SearchFragment extends BaseFragment implements BaseAdapter.OnViewClickListener, PullToRefreshLayout.OnRefreshListener {
    @BindView(R.id.tv_cancel)
    HelveRomanTextView tvCancel;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.lv_history)
    ListView lvHistory;
    @BindView(R.id.lv_hot)
    GridView lvHot;
    @BindView(R.id.lv_search_result)
    ListView lvSearchResult;
    @BindView(R.id.ll_history)
    AutoLinearLayout llHistory;
    @BindView(R.id.ll_hot)
    AutoLinearLayout llHot;
    @BindView(R.id.ll_search_result)
    AutoLinearLayout llSearchResult;
    @BindView(R.id.tv_no_result)
    TextView tvNoResult;
    @BindView(R.id.refreashLayout)
    PullToRefreshLayout refreashLayout;
    @BindView(R.id.lay_no_result)
    AutoLinearLayout layNoResult;
    private List<String> historyArray = new ArrayList<>();
    private SearchContentAdapter historyAdapter;
    private SearchContentAdapter hotAdapter;
    private SearchListAdapter searchListAdapter;
    private String searchStr;
    private int pageNum;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_search, null);
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        historyAdapter = new SearchContentAdapter(mContext,1);
        lvHistory.setAdapter(historyAdapter);
        historyAdapter.setOnViewClickListener(this);

        hotAdapter = new SearchContentAdapter(mContext,0);
        lvHot.setAdapter(hotAdapter);
        hotAdapter.setOnViewClickListener(this);

        searchListAdapter = new SearchListAdapter(mContext);
        lvSearchResult.setAdapter(searchListAdapter);
        searchListAdapter.setOnViewClickListener(this);
        refreashLayout.setCannotPullDown();
        refreashLayout.setOnRefreshListener(this);

        editTextAddListener();
        getHotWords();
        getHistoryData();

        if (historyArray == null || historyArray.isEmpty()) {
            llHistory.setVisibility(View.GONE);
        } else {
            historyAdapter.notifyDataSetChanged(0, historyArray);
        }
    }

    private void editTextAddListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    ivDelete.setVisibility(View.VISIBLE);
                } else {
                    ivDelete.setVisibility(View.GONE);
                }
            }
        });

        //软键盘回车键的监听
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    search();
                    return true;
                }
                return false;
            }
        });

//        refreashLayout.setTouchListener(new PullToRefreshLayout.TouchEventListener() {
//            @Override
//            public void onTouchEvent(MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    boolean isOpen = imm.isActive();
//                    if (isOpen) {
//                        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
//                    }
//                }
//            }
//        });
    }

    private void getHotWords() {
        PublicService.getInstance().getStringRequest(ConfigUrls.VIDEO_HOT_SEARCH, null, new VolleyStringListener() {
            @Override
            public void onStringSuccess(String json, Boolean isCacheData) {
                if (StringUtils.isEmpty(json)) return;

                List<String> hots = JSONArray.parseArray(json, String.class);
                hotAdapter.notifyDataSetChanged(0, hots);
            }

            @Override
            public void onStringError() {

            }
        });
    }

    private void requestSearchData() {
        showProgressDialog(mContext);
        llHistory.setVisibility(View.GONE);
        llHot.setVisibility(View.GONE);
        llSearchResult.setVisibility(View.VISIBLE);
        String mode = "day";
        if (SpUtil.getBoolean(SPKey.MODE, false)) {
            mode = "night";
        }
        pageNum = 1;
        PublicService.getInstance().getJsonObjectRequest(ConfigUrls.ARABIC_SEARCH + StringUtils.encode(searchStr) + "&pageNum=" + pageNum
                + "&model=" + mode + "&devid=" + GetPhoneInfoUtil.INSTANCE.getAndroidId(), null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) throws Exception {
                dismissProgressDialog();
                SearchItemBean searchItemBean = obj.toJavaObject(SearchItemBean.class);
                List<SearchItemBean.RtListBean> rtList = searchItemBean.getRtList();
                if (rtList == null || rtList.isEmpty()) {
                    refreashLayout.setVisibility(View.GONE);
                    tvNoResult.setVisibility(View.VISIBLE);
                    layNoResult.setVisibility(View.VISIBLE);
                } else {
                    pageNum++;
                    if (rtList.size() < 10)
                        refreashLayout.loadmoreFinish(PullToRefreshLayout.NOTMORE);
                    refreashLayout.setVisibility(View.VISIBLE);
                    tvNoResult.setVisibility(View.GONE);
                    layNoResult.setVisibility(View.GONE);
                    searchListAdapter.setSearchId(searchItemBean.getSearchId());
                    searchListAdapter.notifyDataSetChanged(0, rtList);
                }
            }

            @Override
            public void onObjError() {
                dismissProgressDialog();
                refreashLayout.setVisibility(View.GONE);
                tvNoResult.setVisibility(View.VISIBLE);
                layNoResult.setVisibility(View.VISIBLE);
                ToastUtil.showNormalShortToast(getString(R.string.网络异常));
            }
        });
        TongJiUtil.getInstance().putStatistics(
                MyEntry.getIns("acType", "search"),
                MyEntry.getIns("kword", searchStr));
        TongJiUtil.getInstance().putEntries("search");
    }


    private void loadSearchData() {
        String mode = "day";
        if (SpUtil.getBoolean(SPKey.MODE, false)) {
            mode = "night";
        }
        PublicService.getInstance().getJsonObjectRequest(ConfigUrls.ARABIC_SEARCH + StringUtils.encode(searchStr) + "&pageNum=" + pageNum
                + "&model=" + mode + "&devid=" + GetPhoneInfoUtil.INSTANCE.getAndroidId(), null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) throws Exception {
                SearchItemBean searchItemBean = obj.toJavaObject(SearchItemBean.class);
                List<SearchItemBean.RtListBean> rtList = searchItemBean.getRtList();
                if (rtList == null || rtList.isEmpty()) {
                    refreashLayout.loadmoreFinish(PullToRefreshLayout.NOTMORE);
                } else {
                    pageNum++;
                    if (rtList.size() < 10)
                        refreashLayout.loadmoreFinish(PullToRefreshLayout.NOTMORE);
                    else
                        refreashLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    searchListAdapter.setSearchId(searchItemBean.getSearchId());
                    searchListAdapter.notifyDataSetChanged(1, rtList);
                }
            }

            @Override
            public void onObjError() {
                refreashLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
            }
        });
    }

    /**
     * 获取搜索历史 数据
     */
    private void getHistoryData() {
        historyArray.clear();
        String history = SpUtil.getString(SPKey.SEARCH_HISTORY);
        String[] split = history.split(";");
        for (int i = 0; i < split.length; i++) {
            String str = split[i];
            if (StringUtils.isNotEmpty(str)) {
                historyArray.add(str);
            }
        }
    }

    /**
     * 添加搜索历史记录
     *
     * @param string
     */
    public void setSerachHistorySp(String string) {
        String history = SpUtil.getString(SPKey.SEARCH_HISTORY);

        //去重
        String text1 = "";
        String[] str1 = history.split(";");
        for (int i = 0; i < str1.length; i++) {
            if (!str1[i].equals(string)) {
                text1 += str1[i];
                if (i != str1.length - 1) {
                    text1 += ";";
                }
            }
        }

        String[] str2 = text1.split(";");
        String text2 = "";

        //将新的字符串拼接到前面
        if (str2.length < 5) {
            text2 += string;
            for (int i = 0; i < str2.length; i++) {
                text2 += ";" + str2[i];
            }
        } else {
            for (int i = 0; i < 5; i++) {
                if (i == 0) {
                    text2 = string;//新值
                    text2 += ";";
                } else {
                    text2 += str2[i - 1];
                    if (i != 4) {
                        text2 += ";";
                    }
                }
            }
        }
        SpUtil.saveValue(SPKey.SEARCH_HISTORY, text2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.tv_cancel, R.id.iv_delete, R.id.iv_search, R.id.iv_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                popBackStack();
                break;
            case R.id.iv_delete:
                etSearch.setText("");
//                etSearch.performClick();
//                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(etSearch,InputMethodManager.SHOW_FORCED);
                break;
            case R.id.iv_search:
                search();
                break;
            case R.id.iv_clear:
                SpUtil.saveValue(SPKey.SEARCH_HISTORY, "");
                llHistory.setVisibility(View.GONE);
                break;
        }
    }

    private void search() {
        searchStr = etSearch.getText().toString().trim();
        if (StringUtils.isEmpty(searchStr)) return;
        requestSearchData();
        setSerachHistorySp(searchStr);
    }

    @Override
    public void onViewClick(View view, int position) {
        switch (view.getId()) {
            case R.id.rl_search:
                SearchItemBean.RtListBean item = searchListAdapter.getItem(position);
                Intent intent = new Intent(mContext, VideoNewsDetailActivity.class);
                String id = item.getId();
                intent.putExtra(VideoNewsDetailActivity.ID, id);
                intent.putExtra(VideoNewsDetailActivity.NEWS_TYPE, "6");
                startActivity(intent);
                TongJiUtil.getInstance().putStatistics(
                        MyEntry.getIns("acType", "click"),
                        MyEntry.getIns("docType", "video"),
                        MyEntry.getIns("searchId", item.getSearchId()),
                        MyEntry.getIns("docId", id));
                break;

            case R.id.rl_search_news:
                SearchItemBean.RtListBean newsBean = searchListAdapter.getItem(position);
                String newsId = newsBean.getId();
                addToBackStack(HomeNewsDetailFragment.newInstance(newsId, "", "", "6", ""));
                TongJiUtil.getInstance().putStatistics(
                        MyEntry.getIns("acType", "click"),
                        MyEntry.getIns("docType", "news"),
                        MyEntry.getIns("searchId", newsBean.getSearchId()),
                        MyEntry.getIns("docId", newsId));
                break;

            case R.id.tv_search_content:
                searchStr = ((TextView) view).getText().toString().trim();
                etSearch.setText(searchStr);
                requestSearchData();
                setSerachHistorySp(searchStr);
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        loadSearchData();
    }
}
