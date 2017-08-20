package com.onemena.listener;

/**
 * Created by Administrator on 2016/11/2.
 */

public interface VolleyStringListener {
    public void onStringSuccess(String html,Boolean isCacheData);
    public void onStringError();
}
