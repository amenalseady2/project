package com.onemena.home.model.javabean;

import com.google.gson.annotations.SerializedName;

/**
 * ArticleRelation Created by voler on 2017/6/14.
 * 说明：
 */

public class ArticleRelation {

    /**
     * firstName : اليوم السابع
     * userPhoto : http://www.arabsada.com/uploads/profile_photos/58808a5b3efd9.png
     * img_url : http://www.arabsada.com/uploads/2017/01/27/588b20880c78f.jpg
     * dType : n
     * id : 159154
     * video_time : 0:51"
     * categories : 5
     * title : سامح شكرى يبحث مع نظيره الجزائرى الوضع بليبيا والإعداد للقمة الثلاثة
     */

    private String firstName;
    private String userPhoto;
    @SerializedName("img_url")
    private String imgUrl;
    @SerializedName("video_time")
    private String videoTime;
    private String dType;
    private String id;
    private String categories;
    private String title;


    public String getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(String videoTime) {
        this.videoTime = videoTime;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDType() {
        return dType;
    }

    public void setDType(String dType) {
        this.dType = dType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
