package com.onemena.home.view.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.cache.DataManager;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.base.BaseFragment;
import com.onemena.listener.OnActionViewClickListener;
import com.onemena.home.view.adapter.OffLineTitleAdapter;
import com.onemena.utils.GreenDaoUtils;
import com.onemena.utils.NetworkUtil;
import com.onemena.utils.SpUtil;
import com.onemena.utils.ToastUtil;
import com.onemena.utils.TongJiUtil;
import com.onemena.widght.HelveRomanTextView;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WHF on 2017-01-07.
 */

public class NewOffLineFragment extends BaseFragment implements OnActionViewClickListener {

    @BindView(R.id.title_offline)
    HelveRomanTextView titleOffline;
    @BindView(R.id.offline_back_iv)
    ImageView offlineBackIv;
    @BindView(R.id.ll_offline_title)
    AutoLinearLayout llOfflineTitle;
    @BindView(R.id.list_download)
    ListView listDownload;

    ArrayList<String> mList = new ArrayList<>();
    List<String> mTitleList = new ArrayList<>();


    @BindView(R.id.down_txt_btn)
    HelveRomanTextView downTxtBtn;
    @BindView(R.id.txt_count_down)
    HelveRomanTextView txtCountDown;
    private JSONArray mArray;
    private OffLineTitleAdapter offLineTitleAdapter;

    private  StringBuffer stringBuffer;

    private int[] man_id =
            {0,1, 2, 3,
                    4, 6, 7,
                    8, 9, 5,
                    17, 10, 11,
                    12, 14, 15,
                    16, 13,
            };
    private String[] manString =
            {"اخترنا لك","عربي", "دولي", "إقتصاد",
                    "كرة قدم", "تكنولوجيا", "ترفيه",
                    "مشاهير", "حواء", "مصر",

                    "السعودية", "سيارات", "علوم",

                    "ألعاب", "سياحة", "طبخ", "صحة",

                    "الإمارات",
            };

    private int[] img_id = {R.drawable.column_recommend_s,
            R.drawable.column_middle_east_s, R.drawable.column_international_s, R.drawable.column_finance_s,
            R.drawable.column_football_s, R.drawable.column_it_s, R.drawable.column_entertainment_s,
            R.drawable.column_superstar_s, R.drawable.column_female_s, R.drawable.column_egypt_s,
            R.drawable.column_saudi_s, R.drawable.column_car_s, R.drawable.column_technology_s,
            R.drawable.column_game_s, R.drawable.column_travel_s, R.drawable.column_food_s,
            R.drawable.column_health_s, R.drawable.column_uae_s,

    };


    public static NewOffLineFragment getInstance() {
        return new NewOffLineFragment();
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_newoffline, null);
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        offLineTitleAdapter = new OffLineTitleAdapter(mContext);
        offLineTitleAdapter.setOnActionViewClickListener(this);

        mArray = new JSONArray();
        for (int i = 0; i < manString.length; i++) {
            JSONObject object = new JSONObject();
            object.put("name", manString[i]);
            object.put("isCheck", false);
            object.put("id", man_id[i]);
            object.put("img", img_id[i]);
            mArray.add(object);
        }
        offLineTitleAdapter.notifyDataSetChanged(0, mArray);
        listDownload.setAdapter(offLineTitleAdapter);
        changeModeBackgroud(R.color.txt_E3E3E3,R.color.txt_505050,downTxtBtn);
        txtCountDown.setText(getString(R.string.离线选中条数) + "(" + 0 + "/" + mArray.size() + ")");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.offline_back_iv, R.id.down_txt_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.offline_back_iv:
                popBackStack();
                break;
            case R.id.down_txt_btn:
                if (NetworkUtil.checkNetWork(mContext)) {

                    OffLineFragment instance = OffLineFragment.getInstance();
                    Bundle bundle = new Bundle();
                    putData();
                    if (mList.size() > 0) {
                        GreenDaoUtils.getInstance().deleteAll("-1");
                        new myAsyncTask().execute();
                        stringBuffer=new StringBuffer();
                        for (String s : mList) {
                            stringBuffer.append(s+",");
                        }
                        TongJiUtil.getInstance().putEntries(TJKey.OL_DOWNLOAD_START, MyEntry.getIns(TJKey.CATEGORY,stringBuffer.toString()));
                        popBackStack();
                        SpUtil.saveValue(SPKey.IS_DOWNLOAD,true);
                        bundle.putStringArrayList("ids", mList);
                        instance.setArguments(bundle);
//                        instance.getDownLoadData();
                        addToBackStack(instance);

                    }
                }else {
                    ToastUtil.showNormalShortToast(R.string.please_check_your_network);
                }

                break;
            case R.id.rl_title:
                Integer position = (Integer) view.getTag();
                mArray.getJSONObject(position).put("isCheck", !mArray.getJSONObject(position).getBoolean("isCheck"));
                offLineTitleAdapter.notifyDataSetChanged();
                putData();
                if (mList.size() > 0) {
                    changeModeBackgroud(R.color.textcolor_09bb07,R.color.textcolor_045D03,downTxtBtn);
                    txtCountDown.setText(getString(R.string.离线选中条数) + "(" + mList.size() + "/" + mArray.size() + ")");
                } else {
                    changeModeBackgroud(R.color.txt_E3E3E3,R.color.txt_505050,downTxtBtn);
                    txtCountDown.setText(getString(R.string.离线选中条数) + "(" + 0 + "/" + mArray.size() + ")");
                }
                break;
        }
    }

    private void putData() {
        mList.clear();
        mTitleList.clear();
        for (int i = 0; i < mArray.size(); i++) {
            JSONObject jsonObject = mArray.getJSONObject(i);
            if (jsonObject.getBoolean("isCheck")) {
                mList.add(jsonObject.getString("id"));
                mTitleList.add(jsonObject.getString("name"));
            }
        }
    }
    private class myAsyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            DataManager.cleanDownLoadInternalCache(mContext);
            return null;
        }
    }
}
