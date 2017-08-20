package com.onemena.home.model.javabean;

import com.google.gson.annotations.SerializedName;
import com.onemena.base.BaseBean;

import java.util.List;

/**
 * RelationArticle Created by voler on 2017/6/6.
 * 说明：
 */

public class RelationArticle extends BaseBean{

    /**
     * code : 1
     * extra : {}
     * message : success
     */

    private List<ContentBean> content;

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }


    public static class ContentBean {
        /**
         * rect_thumb_meta : ["http://src.mysada.com/sada/file/jpg/220_144_sada20174210241496310772.jpg"]
         * first_name : عناوين
         * id : 484985
         * img_url :
         * posttype : article
         * profile_photo : http://www.arabsada.com/uploads/profile_photos/58af9415e6731.jpg
         * title : الاستثمار تطلب قائمة بالشركات الحكومية المؤهلة للطرح بالبورصة
         * unique_id : 592fe3f5c6d05
         * video_time :
         */

        @SerializedName("first_name")
        private String firstName;
        private String id;
        @SerializedName("img_url")
        private String imgUrl;
        private String posttype;
        @SerializedName("profile_photo")
        private String profilePhoto;
        private String title;
        @SerializedName("unique_id")
        private String uniqueId;
        @SerializedName("video_time")
        private String videoTime;
        @SerializedName("rect_thumb_meta")
        private List<String> rectThumbMeta;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getPosttype() {
            return posttype;
        }

        public void setPosttype(String posttype) {
            this.posttype = posttype;
        }

        public String getProfilePhoto() {
            return profilePhoto;
        }

        public void setProfilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUniqueId() {
            return uniqueId;
        }

        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }

        public String getVideoTime() {
            return videoTime;
        }

        public void setVideoTime(String videoTime) {
            this.videoTime = videoTime;
        }

        public List<String> getRectThumbMeta() {
            return rectThumbMeta;
        }

        public void setRectThumbMeta(List<String> rectThumbMeta) {
            this.rectThumbMeta = rectThumbMeta;
        }
    }
}
