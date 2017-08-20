package com.onemena.home.view.fragment;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.base.BaseFragment;
import com.onemena.home.view.adapter.OffLineTitleAdapter;
import com.onemena.app.config.ConfigUrls;
import com.onemena.listener.FileDownloadListener;
import com.onemena.listener.JsonObjectListener;
import com.onemena.service.PublicService;
import com.onemena.utils.GreenDaoUtils;
import com.onemena.utils.LogManager;
import com.onemena.widght.MyDialog;
import com.onemena.widght.HelveRomanTextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by WHF on 2017-01-07.
 */

public class OffLineListFragment extends BaseFragment {

    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.download_title_list)
    ListView downloadTitleList;
    @BindView(R.id.btn_down)
    HelveRomanTextView btnDown;
    @BindView(R.id.title_offline)
    HelveRomanTextView titleOffline;
    private int[] man_id = {17, 13, 5,
            1, 2, 3,
            4, 6, 10,
            12, 11, 16,
            14, 15, 7,
            8, 9};
    private String[] manString = {"السعودية", "الإمارات", "مصر", "عربي", "دولي", "إقتصاد", "كرة قدم",
            "تكنولوجيا", "سيارات", "ألعاب", "علوم", "صحة",
            "سياحة", "طبخ", "ترفيه", "مشاهير", "حواء"
    };
    int categorieNum = 0;
    int newsNum;
    int downloadNum;
    List<String> mList = new ArrayList<>();
    List<String> mTitleList = new ArrayList<>();
    List<Integer> keyList = new ArrayList<>();
    private JSONObject content;

    private TextView tv_categorie_title;
    private ProgressBar my_progress;
    private JSONArray mArray;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!keyList.contains(msg.arg2)) {
                keyList.add(msg.arg2);//更新ui
                LogManager.i("%%%", keyList.size() * 100 / downloadNum + "-id:" + msg.arg2);
                my_progress.setProgress(100 - (keyList.size() * 100 / downloadNum));
                if (keyList.size() == downloadNum) {
                    keyList.clear();
                    categorieNum++;
                    if (categorieNum < mList.size()) {
                        downloadNews(content.getJSONArray(mList.get(categorieNum)));
                        tv_categorie_title.setText(mTitleList.get(categorieNum));
                    } else if (categorieNum == mList.size()) {
                        if (builder.isShowing()) {
                            builder.dismiss();
                            showFinishDialog();
                        }
                    }
                }
            }
        }
    };
    private Dialog builder;


    public static OffLineListFragment getIntence() {
        return new OffLineListFragment();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_offlinelist, null);
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {


        OffLineTitleAdapter offLineTitleAdapter = new OffLineTitleAdapter(mContext);
        downloadTitleList.setAdapter(offLineTitleAdapter);
        mArray = new JSONArray();
        for (int i = 0; i < manString.length; i++) {
            JSONObject object = new JSONObject();
            object.put("name", manString[i]);
            object.put("isCheck", false);
            object.put("id", man_id[i]);
            mArray.add(object);
        }
        offLineTitleAdapter.notifyDataSetChanged(0, mArray);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.btn_down, R.id.back_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_down:
                showDialog();
                categorieNum=0;
                mList.clear();
                mTitleList.clear();
                for (int i = 0; i < mArray.size(); i++) {
                    JSONObject jsonObject = mArray.getJSONObject(i);
                    if (jsonObject.getBoolean("isCheck")) {
                        mList.add(jsonObject.getString("id"));
                        mTitleList.add(jsonObject.getString("name"));
                    }
                }
                if (mList.size() > 0) {
                    downloadNewsList();
                }
                break;
            case R.id.back_iv:
                popBackStack();
                break;
        }
    }

    //显示对话框
    protected void showDialog() {

        View view = mContext.getLayoutInflater().inflate(R.layout.dialog_offline, null);
        builder = new MyDialog(mContext, view, R.style.dialog, Gravity.BOTTOM);
        builder.setCanceledOnTouchOutside(false);

        final TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_categorie_title = (TextView) view.findViewById(R.id.tv_categorie_title);
        my_progress = (ProgressBar) view.findViewById(R.id.my_progress);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });


        builder.show();

    }

    //显示对话框
    protected void showFinishDialog() {

        View view = mContext.getLayoutInflater().inflate(R.layout.dialog_download_finish, null);
        final MyDialog    myDialog = new MyDialog(mContext, view, R.style.dialog, Gravity.CENTER);
        myDialog.setCanceledOnTouchOutside(false);

        final TextView tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }


    public void downloadNewsList() {
        String categoryIds = "";

        for (int i = 0; i < mList.size(); i++) {
            if (i != mList.size() - 1)
                categoryIds += mList.get(i) + ",";
            else
                categoryIds += mList.get(i);
        }
        tv_categorie_title.setText(mTitleList.get(0));
        PublicService.getInstance().postJsonObjectRequest(true, ConfigUrls.NEWS_OFFLINE_LIST + categoryIds, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
                content = obj.getJSONObject("content");

                GreenDaoUtils.getInstance().deleteAll("-1");
                downloadNews(content.getJSONArray(mList.get(0)));
//                    <img src=\"http:\/\/www.mysada.com\/uploads\/2016\/12\/29\/5864fd16193d1.jpg\"

            }

            @Override
            public void onObjError() {

            }
        });
    }


    private void downloadNews(JSONArray cateArticleList) {
        if (cateArticleList == null) {
            return;
        }
        downloadNum = cateArticleList.size();
        for (int j = 0; j < cateArticleList.size(); j++) {
            JSONObject jsonObject = cateArticleList.getJSONObject(j);
            String body = jsonObject.getString("content");
            JSONArray rect_thumb_meta = new JSONArray();
            Document doc = Jsoup.parse(body);
            Elements pngs = doc.select("img[src]");
            if (pngs == null || pngs.size() == 0) {
                keyList.add(j);
            }
            for (Element element : pngs) {
                String imgUrl = element.attr("src");
                String filePath = getLocationImagePath(imgUrl);
                String uriPath = Uri.fromFile(new File(filePath)).toString();
                element.attr("src", uriPath);
                rect_thumb_meta.add(uriPath);
                PublicService.getInstance().downLoadFile(j, imgUrl, "", filePath, new FileDownloadListener() {
                    @Override
                    public void success(int j) {
                        Message message = Message.obtain();
                        message.arg2 = j;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void fail(int j) {
                        Message message = Message.obtain();
                        message.arg2 = j;
                        handler.sendMessage(message);
                    }
                });
            }
            body = doc.toString();
            jsonObject.put("content", body);//存
            jsonObject.put("rect_thumb_meta", rect_thumb_meta);//存

            GreenDaoUtils.getInstance().addNote(jsonObject);
        }
    }


    private String getLocationImagePath(String imgUrl) {
        String[] split = imgUrl.split("/");
        String filePath = "";
        if (split != null) {
            filePath = split[split.length - 1];
        } else {
            return "";
        }
        String dirPath = mContext.getCacheDir().getAbsolutePath() + File.separator + "news_image";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                LogManager.i("Unable to create cache dirPath image_news");
            }
        }
        return dirPath + File.separator + filePath;
//        return Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + filePath;
    }

}
