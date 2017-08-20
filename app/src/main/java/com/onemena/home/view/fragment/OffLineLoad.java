package com.onemena.home.view.fragment;

import android.net.Uri;
import android.os.Message;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.onemena.app.NewsApplication;
import com.onemena.app.config.ConfigUrls;
import com.onemena.listener.FileDownloadListener;
import com.onemena.listener.JsonObjectListener;
import com.onemena.service.PublicService;
import com.onemena.data.eventbus.DownloadBean;
import com.onemena.utils.GreenDaoUtils;
import com.onemena.utils.LogManager;
import com.onemena.utils.StringUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/4/20.
 */

public enum OffLineLoad {
    INSTANCE;

    OffLineFragment offLineFragment;
    List<Integer> keyList = new ArrayList<>();
    List<String> mList = new ArrayList<>();
    int downloadNum;
    int categorieNum = 0;
    public int progress;
    private boolean isFinish=true;

    public void setFragment(OffLineFragment fragment) {
        offLineFragment = fragment;
    }

    public void init(OffLineFragment offLineFragment, List<String> mList) {
        this.offLineFragment = offLineFragment;
        keyList.clear();
        this.mList=mList;
        categorieNum = 0;
        if (mList.size() > 0) {
            isFinish=false;
            EventBus.getDefault().post(new DownloadBean(isFinish));
           downloadNewsList(mList.get(0));
        }
    }

    /*
  下载频道方法
   */
    public void downloadNewsList(final String ids) {

        LogManager.i("xiazai_id______________________________",ids);

        PublicService.getInstance().getJsonObjectRequest(ConfigUrls.NEWS_OFFLINE_LIST + ids, null, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
                downloadNews(obj.getJSONArray("content"));
            }

            @Override
            public void onObjError() {

                LogManager.i("error______________________________",ids);
                downFailMethod();

            }
        });
    }




    /*
   下载图片
    */
    private void downloadNews(JSONArray cateArticleList) {
        if (cateArticleList == null||cateArticleList.size()<1) {
            downFailMethod();
            return;
        }
        downloadNum = cateArticleList.size();
        for (int j = 0; j < cateArticleList.size(); j++) {
            JSONObject jsonObject = cateArticleList.getJSONObject(j);
            JSONObject articleDetail = jsonObject.getJSONObject("articleDetail");
            String body = articleDetail.getString("content");
            if (StringUtils.isEmpty(body)){
                downLoading(j);
                continue;
            }
            JSONArray rect_thumb_meta = new JSONArray();
            Document doc = Jsoup.parse(body);
            Elements pngs = doc.select("img[src]");
            if (pngs == null || pngs.size() == 0) {
                downLoading(j);//
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
                        downLoading(j);
                    }

                    @Override
                    public void fail(int j) {
                        downLoading(j);
                    }
                });
            }
            body = doc.toString();
            jsonObject.put("content", body);//存
            jsonObject.put("rect_thumb_meta", rect_thumb_meta);//存

            GreenDaoUtils.getInstance().addNote(jsonObject);
        }
    }


    /*
       地址写入
        */
    private String getLocationImagePath(String imgUrl) {
        String[] split = imgUrl.split("/");
        String filePath = "";
        if (split != null) {
            filePath = split[split.length - 1];
        } else {
            return "";
        }
        String dirPath = NewsApplication.getInstance().getCacheDir().getAbsolutePath() + File.separator + "news_image";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                LogManager.i("Unable to create cache dirPath image_news");
            }
        }
        return dirPath + File.separator + filePath;
//        return Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + filePath;
    }

    public void downLoading(int j) {
        if (isFinish) return;
        if (!keyList.contains(j)) {
            keyList.add(j);//更新ui
            LogManager.i("%%%", keyList.size() * 100 / downloadNum + "-id:" + j);
            progress = keyList.size() * 100 / downloadNum / mList.size() + 100 * categorieNum / mList.size();
            Message message = Message.obtain();
            message.arg1 = progress;
            message.what = 0;
            sendMessage(message);
            if (keyList.size() == downloadNum) {
                keyList.clear();
                categorieNum++;
                if (categorieNum < mList.size()) {
                    LogManager.i("xiazai_handler______________________________",categorieNum+"");
                    OffLineLoad.INSTANCE.downloadNewsList(mList.get(categorieNum));
                } else if (categorieNum == mList.size()) {
                    isFinish=true;
                    EventBus.getDefault().post(new DownloadBean(isFinish));
                    Message msg = Message.obtain();
                    msg.what = 1;
                    sendMessage(msg);
                }
            }
        }
    }

    private void sendMessage(Message message) {
        if (offLineFragment != null) {
            offLineFragment.handler.sendMessage(message);
        }
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }
    private void downFailMethod() {
        categorieNum++;
        if (categorieNum >= mList.size()){
            isFinish=true;
            EventBus.getDefault().post(new DownloadBean(isFinish));
            Message msg = Message.obtain();
            msg.what = 1;
            sendMessage(msg);
        }else {
            OffLineLoad.INSTANCE.downloadNewsList(mList.get(categorieNum));
        }
    }
}
