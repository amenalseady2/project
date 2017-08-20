package com.onemena.data.eventbus;

/**
 * Created by Administrator on 2017/3/9.
 */

public class RefreshBean {
    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    int flag;

    public RefreshBean(int flag) {
        this.flag = flag;
    }
}
