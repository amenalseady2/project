package com.onemena.widght;


/**
 * Created by Administrator on 2016/11/29.
 */

public class NewsDetailSettingBusEvent {

    public int likeState;
    public int likeNum;
    public int disLikeNum;
    public boolean isSaved;

    public NewsDetailSettingBusEvent(int likeState, int likeNum, int disLikeNum, boolean isSaved){
        this.likeState = likeState;
        this.likeNum = likeNum;
        this.disLikeNum = disLikeNum;
        this.isSaved=isSaved;
    }

}
