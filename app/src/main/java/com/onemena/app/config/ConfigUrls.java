package com.onemena.app.config;

import com.arabsada.news.BuildConfig;

/**
 * Created by admin on 16/5/30.
 */
public class ConfigUrls {

    public static final boolean STATISTICS = true;//用户行为收集开关

//    public static final String HOST = "http://www.mysada.com";  //外网服务器
    public static final String HOST_STATISTICS = "http://47.91.65.74:17123/useraction/collect.do";  //用户行为收集
    public static final String HOST = BuildConfig.HOST;  //外网服务器
//    public static final String HOST ="http://api.anawin.com";  //外网服务器

    public static final String HOST_VIDEO = "http://video.mysada.com/api/v2/video/categories";  //video

    //----------------------------zhangyushui--------------------------
    //1.获取首页广告位
    public static final String VIDEO_LIST = "http://video.mysada.com/api/v2/video/list/";
    public static final String AUTO_VP = HOST + "/api/news/poll/";

    public static final String NEWS_LIST = HOST + "/api/news/list/0/20/";
    public static final String NEWS_DETAIL = HOST + "/api/news/view/";
    //更新版本
    public static final String UPDATE_URL = BuildConfig.IS_DEBUG? "http://help.test.mysada.com/uploads/jsons/checks/check_2.json":"http://help.mysada.com/uploads/jsons/checks/check_2.json";
//    public static final String UPDATE_URL = "http://192.168.3.18:8081/check.json";
//    public static final String HOST_FAQ = "http://faq.mysada.com/faq/list.json";  //FAQ
    public static final String HOST_FAQ = "http://help.mysada.com/uploads/jsons/faqLists/faqList_2.json";  //FAQ

    public static final String HOME_TITLE_URL = HOST + "/api/category";
    public static final String MORE_NEWS = HOST + "/api/news/about/";
    public static final String HOST_DU_IMG = "";

    //-----------------------------Curise.Wang---------------------------

    /*
    首页列表
     */
    public static final String NEWS_LIST_DU = HOST +"/ar_AE/api/recommend/";
    public static final String NEWS_LIST_WANG ="http://47.91.65.163:17621/recom/doc.do?appid=2&topDocKey=zxTopDoc&devid=";
//    public static final String NEWS_LIST_WANG ="http://47.91.93.147:17621/recom/doc.do?appid=2&topDocKey=zxTopDoc&devid=";
    /*
    二级标题列表
     */
    public static final String NEWS_TITLE_DU = HOST + "/ar_AE/api/article_categories";

    /*
    文章详情
     */
    public static final String NEWS_DETAIL_DU = HOST + "/ar_AE/api/article_details/";

    /*
    新闻详情页 收藏
     */
    public static final String NEWS_ADD_FAV_DU = HOST + "/ar_AE/api/add_favourite/";

    /*
    收藏或者取消收藏
     */
    public static final String NEWS_REMOVE_FAV_DU = HOST + "/ar_AE/api/remove_favourite/";
    public static final String NEWS_DEL_FAV = HOST + "/ar_AE/api/favourite";
    public static final String NEWS_ADD_FAV = HOST + "/ar_AE/api/add_favourite/1/";

    /*
    新闻列表
     */
    public static final String NEWS_ARTICLE_LIST_DU = HOST + "/ar_AE/api/article_list_v2/";
    /*
    离线下载
     */
    public static final String NEWS_OFFLINE_LIST = HOST + "/ar_AE/api/download_offline/";

    /*
    评论列表
     */
    public static final String NEWS_COMMENT_LIST_DU = HOST + "/ar_AE/api/list_comment/";

    /*
    添加评论
     */
    public static final String NEWS_ADD_COMMENT_DU = HOST + "/ar_AE/api/add_comment";

    /*
    文章点赞
     */
    public static final String NEWS_COMMENT_LIKE_DU = HOST + "/ar_AE/api/like/";

    /*
    注册
     */
    public static final String NEWS_REGISTER = HOST + "/ar_AE/api/register";

    /*
    登录
     */
    public static final String NEWS_LOGIN = HOST + "/ar_AE/api/login";
    /*
    日服务开发接口
     */
    public static final String NEWS_FB_TOKEN = HOST + "/ar_AE/api/set_fb_token";

    /*
    举报功能
     */
    public static final String NEWS_REPORT = HOST + "/ar_AE/api/report/";

    /*
    点赞按钮
     */
    public static final String NEWS_SET_LIKE = HOST + "/ar_AE/api/like/";

    /*
    点踩按钮
     */
    public static final String NEWS_SET_DISLIKE = HOST + "/ar_AE/api/dislike/";

    /*
    获得更多推荐的新闻
     */
    public static final String MORE_NEWS_DU = HOST + "/ar_AE/api/relate_article/";

    /*
    分享URL
     */
//    public static final String NEWS_SHARE_URL = HOST + "/articleShare/";
    /*
    视频分享URL
     */
//    public static final String VIDEO_SHARE_URL = HOST + "/videoShare/";

    /*
    视频二级目录接口
     */
    public static final String VIDEO_TITLE_DU = HOST + "/ar_AE/api/video_categories";

    /*
    视频列表接口
     */
    public static final String VIDEO_LIST_DU = HOST + "/ar_AE/api/video_list_v2/";

    /*
    新闻（视频）不感兴趣
     */
    public static final String NEWS_ADDBLOCK = HOST + "/ar_AE/api/add_block_list";

    /*
    获取关注列表
     */
    public static final String ATTENTION_MYLIST = HOST + "/ar_AE/api/myfollowlist";

    /*
    添加关注
     */
    public static final String ADD_ATTENTION = HOST + "/ar_AE/api/follow/";

    /*
    获取关注分类头
     */
    public static final String ATTENTION_TITLE_LIST = HOST + "/ar_AE/api/usergroup_category";

    /*
     获取添加关注列表
     */
    public static final String ADD_ATTENTION_LIST = HOST + "/ar_AE/api/followlist/";

    /*
    视频详情
   */
    public static final String VIDEO_DETAIL_DU = HOST + "/ar_AE/api/video_details/";

    /*
    视频赞踩收藏状态及数量
     */
    public static final String ARTICLE_SHARE_OPTION = HOST +"/ar_AE/api/article_share_option/";
    /*
    轮播图
     */
    public static final String BANNER_LIST = HOST +"/ar_AE/api/get_banner_list/";
    /*
    第三方登录
     */
    public static final String LOGIN_THIRD = HOST +"/ar_AE/api/login_third";
    /**
     * 收藏
     */
    public static final String FAVOURITE_LIST = HOST +"/ar_AE/api/list_favourites/";

    /**
     * 用户信息
     */
    public static final String USER_INFO = HOST +"/ar_AE/api/user_info/";

  /**
     * 任务埋点
     */
    public static final String SEND_TASK = HOST +"/ar_AE/api/send_task";

    /**
     * 考试
     */
    public static final String  EXAMINATION= "http://exam.anawin.com/?app=1";
    /**
     * 金币商城
     */
//    public static final String  GOLD_MARKET= "http://shop.anawin.com";
    public static final String  GOLD_MARKET= BuildConfig.IS_DEBUG?"http://3.dev.arabsada.com":"http://shop1.anawin.com";
//    public static final String  GOLD_TASK= "http://score.arabsada.com";
    public static final String  GOLD_TASK= BuildConfig.IS_DEBUG?"http://7.dev.arabsada.com/index3.html":"http://score.arabsada.com/index2.html";
//    public static final String  GOLD_TASK= BuildConfig.BUG?"http://7.dev.arabsada.com/index.html":"http://score.arabsada.com/index.html";


/**
 * 隐私策略
 */
    public static final String PRIVACY_POLICY="http://www.anawin.com/privacy-policy/";


    /*
   搜索热词
    */
    public static final String VIDEO_HOT_SEARCH = "http://47.91.65.74:17123/hotword/get.do?appid=2&wdNum=5";
    /*
    搜索
     */
//    public static final String VIDEO_SEARCH = "http://47.91.74.170:17316/search/arabic.do?appid=1&docType=video&categories=5&kword=";
//    public static final String VIDEO_SEARCH = "http://47.91.65.163:17316/search/arabic.do?appid=1&docType=video&pageSize=10&kword=";
//    public static final String ARABIC_SEARCH = "http://47.91.93.147:17316/arabic/search.do?appid=1&kword=";
    //正式
    public static final String ARABIC_SEARCH = "http://47.91.65.163:17316/arabic/search.do?appid=2&kword=";
//    public static final String ARABIC_SEARCH = "http://47.91.77.167:17316/arabic/search.do?appid=2&kword=";

}
