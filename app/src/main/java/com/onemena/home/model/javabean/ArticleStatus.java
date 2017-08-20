package com.onemena.home.model.javabean;

import com.google.gson.annotations.SerializedName;
import com.onemena.base.BaseBean;

/**
 * ArticleStatus Created by voler on 2017/6/6.
 * 说明：
 */

public class ArticleStatus extends BaseBean {

    /**
     * code : 1
     * content : {"comment_count":0,"dislike":6,"is_follow":0,"like":75,"total_view":14,"user_dislike_status":0,"user_favourite_status":0,"user_like_status":0}
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
         * comment_count : 0
         * dislike : 6
         * is_follow : 0
         * like : 75
         * total_view : 14
         * user_dislike_status : 0
         * user_favourite_status : 0
         * user_like_status : 0
         */

        private int dislike;
        @SerializedName("is_follow")
        private int isFollow;
        private int like;
        @SerializedName("total_view")
        private int totalView;
        @SerializedName("user_dislike_status")
        private int userDislikeStatus;
        @SerializedName("user_favourite_status")
        private int userFavouriteStatus;
        @SerializedName("user_like_status")
        private int userLikeStatus;


        public int getDislike() {
            return dislike;
        }

        public void setDislike(int dislike) {
            this.dislike = dislike;
        }

        public int getIsFollow() {
            return isFollow;
        }

        public void setIsFollow(int isFollow) {
            this.isFollow = isFollow;
        }

        public int getLike() {
            return like;
        }

        public void setLike(int like) {
            this.like = like;
        }

        public int getTotalView() {
            return totalView;
        }

        public void setTotalView(int totalView) {
            this.totalView = totalView;
        }

        public int getUserDislikeStatus() {
            return userDislikeStatus;
        }

        public void setUserDislikeStatus(int userDislikeStatus) {
            this.userDislikeStatus = userDislikeStatus;
        }

        public int getUserFavouriteStatus() {
            return userFavouriteStatus;
        }

        public void setUserFavouriteStatus(int userFavouriteStatus) {
            this.userFavouriteStatus = userFavouriteStatus;
        }

        public int getUserLikeStatus() {
            return userLikeStatus;
        }

        public void setUserLikeStatus(int userLikeStatus) {
            this.userLikeStatus = userLikeStatus;
        }
    }

}
