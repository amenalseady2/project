package com.onemena.utils;

import android.text.TextUtils;

import com.onemena.app.config.Config;


/**
 * 作者：杨生辉 on 16/7/1 17:53
 */
public class ImageSizeUtils {

    /**
     * 设置图片的宽度大小
     * @param url
     * @param width
     * @param height
     * @return
     */
    public static String getImageUrl(String url , int width , int height){
        String imgUrl = "";
//        http://7xj3rj.com1.z0.glb.clouddn.com/o_1ah9qm9fih661ofd1t60bolf2sr.jpg?imageView/3/w/800/h/800
        if(!TextUtils.isEmpty(url)){
            float b = 1.45f;
            int w = width * Config.widthPixels / 750 ;
            int wEnd = (int)(w / b);
            int h = height * Config.heightPixels / 1334 ;
            int hEnd = (int)(h / b);
            imgUrl = url + "?imageView/3/w/" + wEnd + "/h/" + hEnd ;
        }
        return imgUrl;

    }

    /**
     * 设置图片的宽度大小
     * @param url
     * @param width
     * @param height
     * @return
     */
    public static String getImageUrl(String url , int width , int height,float b){
        String imgUrl = "";
//        http://7xj3rj.com1.z0.glb.clouddn.com/o_1ah9qm9fih661ofd1t60bolf2sr.jpg?imageView/3/w/800/h/800
        if(!TextUtils.isEmpty(url)){

            float w = ((float) width * (float)Config.widthPixels )/ 750.0f ;
            //if(w > ((float)Config.widthPixels * 2.0f / 3.0f)) b = 3.0f;
            int wEnd = (int) (w / b);
            float h = ((float)height * (float)Config.heightPixels)/ 1334.0f ;
            int hEnd = (int) (h / b);
            imgUrl = url + "?imageView/3/w/" + wEnd + "/h/" + hEnd ;
        }
        return imgUrl;
    }

}
