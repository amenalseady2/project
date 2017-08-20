package com.onemena.home.model.javabean;

import com.google.gson.annotations.SerializedName;
import com.onemena.base.BaseBean;

/**
 * ArticleDetails Created by voler on 2017/6/6.
 * 说明：
 */

public class ArticleDetails extends BaseBean {

    /**
     * code : 1
     * content : {"content":"11313433","created_by":"1","featured":"0","id":"387548","img_url":"","link":"","posttype":"0","profile_photo":"nophoto-male.jpg","share_url":"http://share.arabsada.com/share/387548","title":"1313","unique_id":"592f72d357764","video_time":"","video_url":""}
     * extra : {}
     * message : success
     */

    private ContentBean content;


    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }


    public static class ContentBean {
        /**
         * content : 11313433
         * created_by : 1
         * featured : 0
         * id : 387548
         * img_url :
         * link :
         * posttype : 0
         * profile_photo : nophoto-male.jpg
         * share_url : http://share.arabsada.com/share/387548
         * title : 1313
         * unique_id : 592f72d357764
         * video_time :
         * video_url :
         */


        @SerializedName("comment_count")
        private int commentCount;
        private String description;
        private String categories;
        private String content;
        @SerializedName("created_by")
        private String createdBy;
        @SerializedName("create_time")
        private String createTime;
        @SerializedName("first_name")
        private String firstName;
        private String featured;
        private String id;
        @SerializedName("img_url")
        private String imgUrl;
        private String link;
        private String posttype;
        @SerializedName("profile_photo")
        private String profilePhoto;
        @SerializedName("share_url")
        private String shareUrl;
        private String title;
        @SerializedName("unique_id")
        private String uniqueId;
        @SerializedName("video_time")
        private String videoTime;
        @SerializedName("video_url")
        private String videoUrl;

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCategories() {
            return categories;
        }

        public void setCategories(String categories) {
            this.categories = categories;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getFeatured() {
            return featured;
        }

        public void setFeatured(String featured) {
            this.featured = featured;
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

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
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

        public String getShareUrl() {
            return shareUrl;
        }

        public void setShareUrl(String shareUrl) {
            this.shareUrl = shareUrl;
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

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }
    }

}
