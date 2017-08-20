package com.onemena.http;


import com.arabsada.news.BuildConfig;
import com.onemena.bean.AdvBean;
import com.onemena.home.model.javabean.ArticleDetails;
import com.onemena.home.model.javabean.ArticleRelation;
import com.onemena.home.model.javabean.ArticleStatus;
import com.onemena.home.model.javabean.FineNewsBean;
import com.onemena.home.model.javabean.LockNewsBean;
import com.onemena.home.model.javabean.NewNewsItemBean;
import com.onemena.home.model.javabean.NewsItemBean;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * CommonService Created by voler on 2017/6/5.
 * 说明：
 */

public interface CommonService {


    @GET("ar_AE/api/article_status/{article_id}")
    Observable<ArticleStatus> getArticleStatus(@Path("article_id") String articleId);

//    @GET("ar_AE/api/relation_article/{article_id}")
//    Observable<RelationArticle> getRelationArticle(@Path("article_id") String articleId);

    @GET("http://47.91.65.163:17508/recom/reldoc.do?appid=2")
    Observable<List<ArticleRelation>> getRelationArticle(@Query("docid") String articleId);

    @GET("http://47.91.65.163:17508/recom/reldoc.do?appid=2")
    Observable<ResponseBody> getRelationVideo(@Query("docid") String articleId);

    @GET("/ar_AE/api/setting/get")
    Observable<ResponseBody> getIsShowContent();

    @Headers("Accept:application/vnd.anawin.v4+json")
    @GET("/ar_AE/api/article_list_v2/{id}/{size}")
    Observable<NewNewsItemBean> getListDataFromNet(@Path("id") String id, @Path("size") String size, @Query("show_content")String show_content);

    @Headers("Accept:application/vnd.anawin.v3+json")
    @GET("/ar_AE/api/article_details/{article_id}")
    Observable<ArticleDetails> getArticleDetails(@Path("article_id") String articleId);

    @Headers("Accept:application/vnd.anawin.v4+json")
    @GET("/ar_AE/api/article_categories")
    Observable<ResponseBody> getTitleList();



    @GET("/ar_AE/api/best/index")
    Observable<FineNewsBean> getFineNews();

    @GET("http://47.91.65.163:17621/recom/doc.do?appid=2&isNeedContent=1&"+ BuildConfig.VERSION_NAME)
    Observable<LockNewsBean> getLockData(@Query("devid")String devid);

    @GET("http://help.test.mysada.com/api/ad_start/get_v2/anawin")
    Observable<AdvBean> getSplashAdv();

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);
}
