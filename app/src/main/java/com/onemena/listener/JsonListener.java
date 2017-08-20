package com.onemena.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Voler on 2016/7/31.
 */
public interface JsonListener {
    public void onJsonObject(JSONObject obj,Boolean isCacheData) throws Exception;
    public void onJsonArray(int skip, JSONArray array);


    public void onJsonArrayError();
    public void onObjError();
    public void onNetError();
}
