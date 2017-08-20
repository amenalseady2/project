package com.onemena.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.listener.OnActionViewClickListener;
import com.onemena.widght.HelveRomanTextView;
import com.facebook.drawee.view.SimpleDraweeView;


/**
 * 作者：张玉水 on 16/6/20 12:08
 */
public class AutoViewPagerAdapter extends PagerAdapter {

    private JSONArray array = new JSONArray();
    protected OnActionViewClickListener mListener;

    private Context mContext;


    private int mType;

    public void setType(int type) {
        this.mType = type;
    }

    public AutoViewPagerAdapter(Context context) {
        this.mContext = context;
    }


    /**
     * 设置ActionView点击事件
     *
     * @param listener
     */
    public void setOnActionViewClickListener(OnActionViewClickListener listener) {
        if (listener == null) {
            return;
        }
        this.mListener = listener;
    }

    public void updateNotifyDataSetChanged(JSONArray mArray) {
        this.array = mArray;
        notifyDataSetChanged();
    }


    private int mChildCount = 0;

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        if (array.size() == 0) {
            return 0;
        }
        return array.size() * 500;
    }

    public JSONArray getArray() {
        return array;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }


    /**
     * 释放图片资源的方法
     *
     * @param imageView
     */
    public void releaseImageViewResouce(SimpleDraweeView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        }
        System.gc();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View view = View.inflate(mContext, R.layout.auto_viewpager_item, null);

        JSONObject jsonObject = array.getJSONObject(position % array.size());

        SimpleDraweeView img = (SimpleDraweeView) view.findViewById(R.id.simple_img);


        HelveRomanTextView title_tv = (HelveRomanTextView) view.findViewById(R.id.title_tv);
//        GenericDraweeHierarchy hierarchy = img.getHierarchy();
//        hierarchy.setPlaceholderImage(R.mipmap.ic_default);
        //img.setDefaultImageResId(R.mipmap.ic_default);
//        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        img.setImageURI( Uri.parse(ImageSizeUtils.getImageUrl(array.getString(position) , 710 , 400)));
        String thumbnail = jsonObject.getString("thumbnail");
        if (thumbnail != null) {
            img.setImageURI(Uri.parse(thumbnail));
        }
        title_tv.setText(jsonObject.getString("title"));
        Bundle bundle2 = new Bundle();
        bundle2.putString("id", jsonObject.getString("id"));
//        bundle2.putString("unique_id", jsonObject.getString("unique_id"));
        img.setTag(bundle2);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(v);
                }
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
//        releaseImageViewResouce((SimpleDraweeView) object);
        ((ViewPager) container).removeView((View) object);
    }


}
