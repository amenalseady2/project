package com.onemena.search.model.javabean;

import java.util.List;

/**
 * Created by WHF on 2017-04-08.
 */

public class SearchItemBean {

    /**
     * totalNum : 977
     * segmentWord : شاشه
     * rtList : [{"categ_name":"مصري","firstName":"الأهلي","userPhoto":"58789f95c6d67.jpg","updated_at":"منذ ساعة","img_url":"http://www.wekoora.com/uploads/2017/01/16/587d2fddc2807.jpg","dType":"n","id":"5166","title":"\"البلدوزر\" يقترب من العودة على <font color=\"#E5603D\">شاشة<\/font> الأهلي"},{"categ_name":"سعودي","updated_at":"منذ 15 ساعة","img_url":"http://www.wekoora.com/uploads/2017/03/15/58c94f3de08e6.jpg","dType":"n","id":"63721","title":"<font color=\"#E5603D\">شاشة<\/font> الجوهرة تثير سخرية الجماهير"},{"categ_name":"الإسباني","firstName":"النادي","userPhoto":"58ad610f97823.jpg","updated_at":"منذ يوم","img_url":"http://src.mysada.com/wekoora/images/20170419/640_640_7bf526214b4caebe5885f67e3b8617f6e0c8dcf5.png","dType":"n","id":"102136","title":"<font color=\"#E5603D\">شاشات<\/font> عملاقة للاجئين في اليونان لمشاهدة الكلاسيكو"},{"video_time":"0:24","categ_name":"مصر","video_url":"vQgjpLZXMxg","updated_at":"منذ ساعة","img_url":"http://www.wekoora.com/uploads/2017/01/30/588e707d1f285.jpg","tag_name":["الأهلي","الدوري المصري","رياضة"],"dType":"v","id":"18780","title":"الاهلى امام جولدى حصرياٌ على <font color=\"#E5603D\">شاشه<\/font> الاهلى"},{"video_time":"1:02","categ_name":"مصر","video_url":"ZZq4IQ1PDMY","updated_at":"منذ ساعتين","img_url":"http://www.wekoora.com/uploads/2017/03/06/58bda3c6316a8.jpg","tag_name":["الأهلي","فريق"],"dType":"v","id":"54220","title":"مباراة الاهلى امام بيدفيست على <font color=\"#E5603D\">شاشه<\/font> الاهلى حصريا"},{"video_time":"5:23","categ_name":"مصر","video_url":"WEPwkcHdVTw","updated_at":"منذ ساعتين","img_url":"http://www.wekoora.com/uploads/2017/03/11/58c437bc14099.jpg","tag_name":["الأهلي","دوري ابطال افريقيا","بيدفيست"],"dType":"v","id":"59250","title":"\"بيومى فؤاد\" محلل على <font color=\"#E5603D\">شاشه<\/font> الاهلى لمباراة بيدفيست"},{"categ_name":"الإسباني","firstName":"كووورة","userPhoto":"5878a2249691c.png","updated_at":"منذ يوم","img_url":"http://src.mysada.com/wekoora/images/20170419/640_640_272fd051032a98cb859539d36ea034b0a93d1c84.jpg","dType":"n","id":"101991","title":"<font color=\"#E5603D\">شاشات<\/font> عملاقة للاجئين باليونان لمشاهدة الكلاسيكو الإسباني"},{"categ_name":"الإيطالي","firstName":"كووورة","userPhoto":"5878a2249691c.png","updated_at":"منذ يوم","img_url":"http://src.mysada.com/wekoora/images/20170430/640_640_8f01e5007bcd0818f600a13bd40a025d20b75a0b.jpg","dType":"n","id":"114768","title":"قصة حب مارادونا ونابولي على <font color=\"#E5603D\">شاشات<\/font> السينما"},{"categ_name":"عالمي","firstName":"بوابة الفجر","userPhoto":"5878a0e5f05f6.png","updated_at":"منذ يوم","img_url":"http://src.mysada.com/wekoora/images/20170419/640_640_f2e0adc3b2968fa79e3f73b5629ad5f281f0cb49.jpg","dType":"n","id":"102167","title":"<font color=\"#E5603D\">شاشات<\/font> عملاقة للاجئين في اليونان لمشاهدة الكلاسيكو الإسباني"},{"categ_name":"عالمي","firstName":"كورة","userPhoto":"5878a49d8108f.png","updated_at":"منذ يوم","img_url":"http://src.mysada.com/wekoora/images/20170413/640_640_4d41b8ff11c7a878d3950f638437499a8c6dac7e.jpg","dType":"n","id":"99914","title":"<font color=\"#E5603D\">شاشات<\/font> عملاقة وموسيقى في استقبال جماهير أتلتيكو في ليستر"}]
     */

    private int totalNum;
    private String searchId;
    private String segmentWord;
    private List<RtListBean> rtList;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getSegmentWord() {
        return segmentWord;
    }

    public void setSegmentWord(String segmentWord) {
        this.segmentWord = segmentWord;
    }

    public List<RtListBean> getRtList() {
        return rtList;
    }

    public void setRtList(List<RtListBean> rtList) {
        this.rtList = rtList;
    }

    public static class RtListBean {
        /**
         * firstName : الأهلي
         * userPhoto : 58789f95c6d67.jpg
         * img_url : http://www.wekoora.com/uploads/2017/01/16/587d2fddc2807.jpg
         * dType : n
         * id : 5166
         * title : "البلدوزر" يقترب من العودة على <font color="#E5603D">شاشة</font> الأهلي
         * video_time : 0:24
         */

        private String firstName;
        private String userPhoto;
        private String img_url;
        private String dType;
        private String id;
        private String title;
        private String video_time;
        private String searchId;


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


        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVideo_time() {
            return video_time;
        }

        public void setVideo_time(String video_time) {
            this.video_time = video_time;
        }

        public String getSearchId() {
            return searchId;
        }

        public void setSearchId(String searchId) {
            this.searchId = searchId;
        }
    }
}
