package com.onemena.data.eventbus;

/**
 * Created by Administrator on 2017/3/31.
 */

public class LoginBean {

    /**
     * code : 1
     * content : {"first_name":"陈欢","last_name":1,"profile_photo":"58a139ea3a3a5.jpg","user_email":"746215017@qq.com","user_name":"chenhuan","user_token":"eyJpdiI6Ikxtam9qbkMzVlkxellVMGdiU2hwRGc9PSIsInZhbHVlIjoiUm1BWjdiSFlnR2lldUFuNzdRallDQT09IiwibWFjIjoiYjljMmUzZGEyZDFhZjA2ZGZlMmMxODE2YTI3YTkyNWQ3ZWE5OWUxNzY5NGVkNDU4ZWY0OTkyOWQxYWU2MTMxOSJ9"}
     * message : login success
     */

    private int code;
    private ContentBean content;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class ContentBean {
        /**
         * first_name : 陈欢
         * last_name : 1
         * profile_photo : 58a139ea3a3a5.jpg
         * user_email : 746215017@qq.com
         * user_name : chenhuan
         * user_token : eyJpdiI6Ikxtam9qbkMzVlkxellVMGdiU2hwRGc9PSIsInZhbHVlIjoiUm1BWjdiSFlnR2lldUFuNzdRallDQT09IiwibWFjIjoiYjljMmUzZGEyZDFhZjA2ZGZlMmMxODE2YTI3YTkyNWQ3ZWE5OWUxNzY5NGVkNDU4ZWY0OTkyOWQxYWU2MTMxOSJ9
         */

        private String first_name;
        private int last_name;
        private String profile_photo;
        private String user_email;
        private String user_name;
        private String user_token;
        private String login_from;
        private String goods_notify;

        public String getLogin_from() {
            return login_from;
        }

        public void setLogin_from(String login_from) {
            this.login_from = login_from;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public int getLast_name() {
            return last_name;
        }

        public void setLast_name(int last_name) {
            this.last_name = last_name;
        }

        public String getProfile_photo() {
            return profile_photo;
        }

        public void setProfile_photo(String profile_photo) {
            this.profile_photo = profile_photo;
        }

        public String getUser_email() {
            return user_email;
        }

        public void setUser_email(String user_email) {
            this.user_email = user_email;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_token() {
            return user_token;
        }

        public void setUser_token(String user_token) {
            this.user_token = user_token;
        }

        public String getGoods_notify() {
            return goods_notify;
        }

        public void setGoods_notify(String goods_notify) {
            this.goods_notify = goods_notify;
        }
    }
}
