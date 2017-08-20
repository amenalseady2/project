package com.onemena.bean;

import com.google.gson.annotations.SerializedName;
import com.onemena.base.BaseBean;

/**
 * AdvBean Created by voler on 2017/5/18.
 * 说明：
 */

public class AdvBean extends BaseBean{

    private DataBean content;

    public DataBean getContent() {
        return content;
    }

    public void setContent(DataBean content) {
        this.content = content;
    }

    public static class DataBean {

        /**
         * id : 23
         * type : ad
         * android_pic : http://src.mysada.com/faqContentImages/file/jpeg/98d1ae616db4d4db03356c7772fbdab3.jpeg
         * redirect_to : http://www.baidu.com
         * end_time : 1495036740
         * status : 0
         */

        private int id;
        private String type;
        @SerializedName("android_pic")
        private String androidPic;
        @SerializedName("redirect_to")
        private String redirectTo;
        @SerializedName("end_time")
        private int endTime;
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAndroidPic() {
            return androidPic;
        }

        public void setAndroidPic(String androidPic) {
            this.androidPic = androidPic;
        }

        public String getRedirectTo() {
            return redirectTo;
        }

        public void setRedirectTo(String redirectTo) {
            this.redirectTo = redirectTo;
        }

        public int getEndTime() {
            return endTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
