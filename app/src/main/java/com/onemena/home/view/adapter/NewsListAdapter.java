package com.onemena.home.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.arabsada.news.R;
import com.onemena.base.BaseAdapter;
import com.onemena.base.BaseViewHolder;
import com.onemena.home.model.javabean.NewsItemBean;
import com.onemena.home.view.viewholder.NewsNoImageHolder;
import com.onemena.home.view.viewholder.NewsOneImageHolder;
import com.onemena.home.view.viewholder.NewsThreeImageHolder;
import com.onemena.home.view.viewholder.NewsVideoHolder;
import com.onemena.home.view.viewholder.SpecialOneImageHolder;
import com.onemena.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Administrator on 2017/3/24.
 */

public class NewsListAdapter extends BaseAdapter<NewsItemBean> {
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    final int TYPE_3 = 2;
    final int TYPE_4 = 3;
    final int TYPE_5 = 4;
    private long newsTime = System.currentTimeMillis() + 20 * 60 * 1000;
    private boolean isShowNumTime = true;
    private Random random;
    private List baseData = new ArrayList();
    private int pagers;
    private boolean visibility=true;

    public NewsListAdapter(Context mContext) {
        super(mContext);
    }


    @Override
    public BaseViewHolder getHolder(int position, ViewGroup parent) {
        int type = getItemViewType(position);
        BaseViewHolder baseViewHolder = null;
            //按当前所需的样式，确定new的布局
            switch (type) {
                case TYPE_1:
                    baseViewHolder = new NewsOneImageHolder(mContext, parent);
                    Log.e("convertView = ", "NULL TYPE_1");
                    break;
                case TYPE_2:
                    baseViewHolder = new NewsVideoHolder(mContext, parent);
                    Log.e("convertView = ", "NULL TYPE_2");
                    break;
                case TYPE_3:
                    baseViewHolder = new NewsThreeImageHolder(mContext, parent);
                    Log.e("convertView = ", "NULL TYPE_3");
                    break;
                case TYPE_4:
                    baseViewHolder = new NewsNoImageHolder(mContext, parent);
                    Log.e("convertView = ", "NULL TYPE_4");
                    break;
                case TYPE_5:
                    baseViewHolder = new SpecialOneImageHolder(mContext, parent);
                    Log.e("convertView = ", "NULL TYPE_4");
                    break;
            }
        return baseViewHolder;
    }

    @Override
    public void changeData(NewsItemBean data) {
        if (data.getNews_time() == 0) {
            if (isShowNumTime) {
                random = new Random();
                newsTime = newsTime - random.nextInt(5) * 1000 * 60 - 4 * 1000 * 60;
                data.setNews_time(newsTime);
            } else {
                data.setNews_time(System.currentTimeMillis());
            }
        }
        long news_time = System.currentTimeMillis() - data.getNews_time();
        String formatterTime="";
        if (visibility){
            formatterTime= getFormatterTime(news_time);
        }

        data.setCreateTime(formatterTime);
    }

    /**
     * 更新经验列表
     */
    public void notifyDataSetChanged(int skipCount, List array, List topArray) {
        if (array == null) {
            return;
        }
        if (skipCount < 0) {//刷新前边加
            isShowNumTime = false;

            baseData.addAll(0,array);
            data.clear();
            data.addAll(topArray);
            data.addAll(baseData);

        } else if (skipCount == 0) {//刷新清空原来数据,暂时还用不到
            newsTime = System.currentTimeMillis() + 20 * 60 * 1000;
            isShowNumTime = true;

            data.clear();
            data.addAll(topArray);
            data.addAll(array);
        } else {
            isShowNumTime = true;
            baseData.addAll(array);//加载更多，后叠加


            data.clear();
            data.addAll(topArray);
            data.addAll(baseData);

        }
        super.notifyDataSetChanged();
    }

    /**
     * 更新经验列表
     */
    public void notifyDataSetChanged(int skipCount, List array) {
        if (array == null) {
            return;
        }
        if (skipCount < 0) {
            isShowNumTime = false;
            this.data.addAll(0,array);
        } else if (skipCount == 0) {
            newsTime = System.currentTimeMillis() + 20 * 60 * 1000;
            isShowNumTime = true;
            this.data = array;
        } else {
            isShowNumTime = true;
            this.data.addAll(array);
        }
        super.notifyDataSetChanged();
    }


//    /**
//     * 更新经验列表
//     */
//    public int notifyDataSetChanged(int skipCount, List array, List topArray, int count) {
//        if (array == null) {
//            return 0;
//        }
//        synchronized (BaseAdapter.class) {
//            pagers++;
//            if (pagers > count) {
//                return 1;
//            }
//            if (skipCount < 0) {//刷新前边加
//                isShowNumTime = false;
//                array.addAll(baseData);
//                baseData = array;
//                topArray.addAll(baseData);
//                this.data = topArray;
//            } else if (skipCount == 0) {//刷新清空原来数据,暂时还用不到
//                newsTime = System.currentTimeMillis() + 20 * 60 * 1000;
//                isShowNumTime = true;
//                topArray.addAll(array);
//                this.baseData = array;
//                this.data = topArray;
//            } else {
//                isShowNumTime = true;
//                baseData.addAll(array);//加载更多，后叠加
//                topArray.addAll(baseData);
//                this.data = topArray;
//            }
//        }
//        super.notifyDataSetChanged();
//        return 0;
//    }


    @Override
    public int getItemViewType(int position) {
        NewsItemBean jsonObject = data.get(position);
        String posttype = jsonObject.getPosttype();
        List<String> img_arry = jsonObject.getRect_thumb_meta();
        if ("video".equals(posttype)||"v".equals(posttype)) {
            return TYPE_2;
        }  else if ("special".equals(posttype)){
            return TYPE_5;

        }else {
            if (img_arry!=null&&img_arry.size() > 0) {//有多张图片,或一张图
                if (img_arry.size() > 2) {
                    return TYPE_3;
                }else {
                    //有一个图
                    return TYPE_1;
                }
            }
            return TYPE_4;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }


    private String getFormatterTime(long news_time) {
        news_time /= 1000;
        if (news_time < 60) {
            return mContext.getResources().getString(R.string.only_just);
        } else if (news_time < 60 * 60) {
            return mContext.getResources().getString(R.string.before) + " " +
                    StringUtils.int2IndiaNum(String.valueOf(news_time / 60)) + " " +
                    mContext.getResources().getString(R.string.minute_before);
        } else if (news_time < 60 * 60 * 24) {
            return mContext.getResources().getString(R.string.before) + " " +
                    StringUtils.int2IndiaNum(String.valueOf(news_time / 60 / 60)) + " " +
                    mContext.getResources().getString(R.string.hour_before);
        } else if (news_time < 60 * 60 * 24 * 2) {
            return mContext.getResources().getString(R.string.yesterday);
        } else if (news_time < 60 * 60 * 24 * 3) {
            return mContext.getResources().getString(R.string.the_day_before_yesterday);
        } else {
            return mContext.getResources().getString(R.string.before) + " " +
                    StringUtils.int2IndiaNum(String.valueOf(news_time / 60 / 60 / 24)) + " " +
                    mContext.getResources().getString(R.string.day_before);
        }
    }

    public void setCreateTimeVisibility(boolean visibility){
        this.visibility =visibility;
    }

}
