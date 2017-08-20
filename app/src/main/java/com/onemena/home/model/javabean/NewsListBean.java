package com.onemena.home.model.javabean;

import java.util.List;

/**
 * 三尺春光驱我寒，一生戎马为长安
 * Created by Han on 17/3/26.
 */

public class NewsListBean {
    /**
     * code : 1
     * message : Request successfully
     * content : {"recommend":{"count":25,"percount":8,"data":[{"id":"431185","unique_id":"58d65b2cbb791","posttype":"article","title":"تفاؤل بنمو الأعمال التجارية في الإمارات خلال عام 2017","link":"http://wam.ae/ar/details/1395302604993","description":"content","img_url":"","like":"100","dislike":"0","rect_thumb_meta":[""],"total_view":"0","comment_count":"0","share":"0","video_time":"","video_url":"","created_at":"2017-03-25 12:57:32","created_by":"46254","user_name":"wam.ae","first_name":"وكالة أنباء الإمارات","profile_photo":"58b8071ad28c5.jpg","create_time":"منذ 4 ساعات","categories":"13","categories_text":"الإمارات","tags":[]}]}}
     */

    private int code;
    private String message;
    private ContentBean content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * recommend : {"count":25,"percount":8,"data":[{"id":"431185","unique_id":"58d65b2cbb791","posttype":"article","title":"تفاؤل بنمو الأعمال التجارية في الإمارات خلال عام 2017","link":"http://wam.ae/ar/details/1395302604993","description":"content","img_url":"","like":"100","dislike":"0","rect_thumb_meta":[""],"total_view":"0","comment_count":"0","share":"0","video_time":"","video_url":"","created_at":"2017-03-25 12:57:32","created_by":"46254","user_name":"wam.ae","first_name":"وكالة أنباء الإمارات","profile_photo":"58b8071ad28c5.jpg","create_time":"منذ 4 ساعات","categories":"13","categories_text":"الإمارات","tags":[]}]}
         */

        private RecommendBean recommend;

        public RecommendBean getRecommend() {
            return recommend;
        }

        public void setRecommend(RecommendBean recommend) {
            this.recommend = recommend;
        }

        public static class RecommendBean {
            /**
             * count : 25
             * percount : 8
             * data : [{"id":"431185","unique_id":"58d65b2cbb791","posttype":"article","title":"تفاؤل بنمو الأعمال التجارية في الإمارات خلال عام 2017","link":"http://wam.ae/ar/details/1395302604993","description":"content","img_url":"","like":"100","dislike":"0","rect_thumb_meta":[""],"total_view":"0","comment_count":"0","share":"0","video_time":"","video_url":"","created_at":"2017-03-25 12:57:32","created_by":"46254","user_name":"wam.ae","first_name":"وكالة أنباء الإمارات","profile_photo":"58b8071ad28c5.jpg","create_time":"منذ 4 ساعات","categories":"13","categories_text":"الإمارات","tags":[]}]
             */

            private int count;
            private int percount;
            private List<DataBean> data;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getPercount() {
                return percount;
            }

            public void setPercount(int percount) {
                this.percount = percount;
            }

            public List<DataBean> getData() {
                return data;
            }

            public void setData(List<DataBean> data) {
                this.data = data;
            }

            public static class DataBean {
                /**
                 * id : 431185
                 * unique_id : 58d65b2cbb791
                 * posttype : article
                 * title : تفاؤل بنمو الأعمال التجارية في الإمارات خلال عام 2017
                 * link : http://wam.ae/ar/details/1395302604993
                 * description : content
                 * img_url :
                 * like : 100
                 * dislike : 0
                 * rect_thumb_meta : [""]
                 * total_view : 0
                 * comment_count : 0
                 * share : 0
                 * video_time :
                 * video_url :
                 * created_at : 2017-03-25 12:57:32
                 * created_by : 46254
                 * user_name : wam.ae
                 * first_name : وكالة أنباء الإمارات
                 * profile_photo : 58b8071ad28c5.jpg
                 * create_time : منذ 4 ساعات
                 * categories : 13
                 * categories_text : الإمارات
                 * tags : []
                 */

                private String id;
                private String unique_id;
                private String posttype;
                private String title;
                private String link;
                private String description;
                private String img_url;
                private String like;
                private String dislike;
                private String total_view;
                private String comment_count;
                private String share;
                private String video_time;
                private String video_url;
                private String created_at;
                private String created_by;
                private String user_name;
                private String first_name;
                private String profile_photo;
                private String create_time;
                private String categories;
                private String categories_text;
                private List<String> rect_thumb_meta;
                private List<?> tags;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getUnique_id() {
                    return unique_id;
                }

                public void setUnique_id(String unique_id) {
                    this.unique_id = unique_id;
                }

                public String getPosttype() {
                    return posttype;
                }

                public void setPosttype(String posttype) {
                    this.posttype = posttype;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getImg_url() {
                    return img_url;
                }

                public void setImg_url(String img_url) {
                    this.img_url = img_url;
                }

                public String getLike() {
                    return like;
                }

                public void setLike(String like) {
                    this.like = like;
                }

                public String getDislike() {
                    return dislike;
                }

                public void setDislike(String dislike) {
                    this.dislike = dislike;
                }

                public String getTotal_view() {
                    return total_view;
                }

                public void setTotal_view(String total_view) {
                    this.total_view = total_view;
                }

                public String getComment_count() {
                    return comment_count;
                }

                public void setComment_count(String comment_count) {
                    this.comment_count = comment_count;
                }

                public String getShare() {
                    return share;
                }

                public void setShare(String share) {
                    this.share = share;
                }

                public String getVideo_time() {
                    return video_time;
                }

                public void setVideo_time(String video_time) {
                    this.video_time = video_time;
                }

                public String getVideo_url() {
                    return video_url;
                }

                public void setVideo_url(String video_url) {
                    this.video_url = video_url;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public String getCreated_by() {
                    return created_by;
                }

                public void setCreated_by(String created_by) {
                    this.created_by = created_by;
                }

                public String getUser_name() {
                    return user_name;
                }

                public void setUser_name(String user_name) {
                    this.user_name = user_name;
                }

                public String getFirst_name() {
                    return first_name;
                }

                public void setFirst_name(String first_name) {
                    this.first_name = first_name;
                }

                public String getProfile_photo() {
                    return profile_photo;
                }

                public void setProfile_photo(String profile_photo) {
                    this.profile_photo = profile_photo;
                }

                public String getCreate_time() {
                    return create_time;
                }

                public void setCreate_time(String create_time) {
                    this.create_time = create_time;
                }

                public String getCategories() {
                    return categories;
                }

                public void setCategories(String categories) {
                    this.categories = categories;
                }

                public String getCategories_text() {
                    return categories_text;
                }

                public void setCategories_text(String categories_text) {
                    this.categories_text = categories_text;
                }

                public List<String> getRect_thumb_meta() {
                    return rect_thumb_meta;
                }

                public void setRect_thumb_meta(List<String> rect_thumb_meta) {
                    this.rect_thumb_meta = rect_thumb_meta;
                }

                public List<?> getTags() {
                    return tags;
                }

                public void setTags(List<?> tags) {
                    this.tags = tags;
                }
            }
        }
    }
}
