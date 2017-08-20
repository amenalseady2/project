package com.onemena.app.config;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by Administrator on 2017/4/13.
 */

public interface TJKey {
    /**
     * 统计名
     */
    @StringDef({TASK_COM, CLICK_DISLIKE, NEWS_REPORT, NEWS_COMMENT, SAVE, NEWS_COMMENT_CLICK, RELATED, READ, CLICK_LICK, SHARE,
            DETAIL_DURATION, UC_CHECK_FAQ, UC_FEEDBACK_COPYEMAIL, FIRST_UPDATE, UPDATE, LOAD_MORE, DEP_DETAIL_DURATION,
            DEP_CLICK, NEWS_REPORT_SUBMIT, NEWS_REPORT_SUBMIT_SETTING, UC_LOGIN_GG_RESULT, PRE_START, FIRET_CHECK_NETWORK, UC_LOGIN,
            VIDEO_PLAY, UC_CLEAR, UC_FEEDBACK, TASK_ENT, REDUCE_SUBMIT, COMMENT_SUBMIT, OL_ENT, OL_DOWNLOAD_START,
            OL_DOWNLOAD_CANCEL, DEP_READ, EXAM_ENT, SHOP_ENT, VERSION_UPDATE, RECALL, FOLLOW_HEADLINES, LOCKREAD_POP, LOCKREAD_SLIDE, LOCKREAD_SWITCH,
            AD_JUMP})
    @Retention(RetentionPolicy.SOURCE)
    @interface Name {
    }

    /**
     * 参数名
     */
    @StringDef({TASK_ID, TYPE, RESOURCE_ID, CATEGORY_ID, DOCTYPE, ACTYPE, DOCID, RESOURCE_RELATED_ID, NEWS_TYPE, COMMEND, SHARE_WAY, DURATION, CATGID
            , NETWORK, METHOD, INSTANCE_TIME, CREATE_TIME, TIME2, TIME1, CATEGORY,RELRECOM,RECOM_ID,HEADLINES_ID,ID})
    @Retention(RetentionPolicy.SOURCE)
    @interface Query {
    }


    /**
     * 第三方登录type
     */
    @StringDef({TWITTER_TYPE, FACEBOOK_TYPE, GOOGLE_TYPE})
    @Retention(RetentionPolicy.SOURCE)
    @interface LoginType {
    }

    String TWITTER_TYPE = "0";
    String FACEBOOK_TYPE = "1";
    String GOOGLE_TYPE = "2";


    String EXAM_ENT = "exam_ent";
    String RELRECOM = "relRecom";
    String RECALL = "recall";
    String ID = "id";
    String VERSION_UPDATE = "version_update";
    String TASK_COM = "task_com";
    String DEP_READ = "dep_read";
    String OL_DOWNLOAD_CANCEL = "ol_download_cancel";
    String CATEGORY = "category";
    String OL_DOWNLOAD_START = "ol_download_start";
    String OL_ENT = "ol_ent";
    String COMMENT_SUBMIT = "comment_submit";
    String TIME1 = "time1";
    String REDUCE_SUBMIT = "reduce_submit";
    String TASK_ENT = "task_ent";
    String SHOP_ENT = "shop_ent";
    String UC_FEEDBACK = "uc_feedback";
    String UC_CLEAR = "uc_clear";
    String VIDEO_PLAY = "video_play";
    String UC_LOGIN = "uc_login";
    String TIME2 = "time2";
    String FIRET_CHECK_NETWORK = "firet_check_network";
    String CREATE_TIME = "create_time";
    String INSTANCE_TIME = "instance_time";
    String METHOD = "method";
    String NETWORK = "network";
    String PRE_START = "pre_start";
    String UC_LOGIN_GG_RESULT = "uc_login_gg_result";
    String NEWS_REPORT_SUBMIT_SETTING = "news_report_submit_setting";
    String NEWS_REPORT_SUBMIT = "news_report_submit";
    String DEP_CLICK = "dep_click";
    String DEP_DETAIL_DURATION = "dep_detail_duration";
    String LOAD_MORE = "load_more";
    String CATGID = "catgId";
    String UPDATE = "update";
    String FIRST_UPDATE = "first_update";
    String UC_FEEDBACK_COPYEMAIL = "uc_feedback_copyemail";
    String UC_CHECK_FAQ = "uc_check_FAQ";
    String DURATION = "duration";
    String DETAIL_DURATION = "detail_duration";
    String CLICK_DISLIKE = "click_dislike";
    String TASK_ID = "task_id";
    String TYPE = "type";
    String RESOURCE_ID = "resource_id";
    String CATEGORY_ID = "category_id";
    String DOCTYPE = "docType";
    String ACTYPE = "acType";
    String DOCID = "docId";
    String RECOM_ID = "recomId";
    String NEWS_REPORT = "news_report";
    String NEWS_COMMENT = "news_comment";
    String SAVE = "save";
    String NEWS_COMMENT_CLICK = "news_comment_click";
    String RELATED = "related";
    String RESOURCE_RELATED_ID = "resource_related_id";
    String READ = "read";
    String NEWS_TYPE = "news_type";
    String COMMEND = "commend";
    String CLICK_LICK = "click_lick";
    String SHARE = "share";
    String SHARE_WAY = "share_way";
    String FOLLOW_HEADLINES = "follow_headlines";
    String HEADLINES_ID = "headlines_id";

    String AD_JUMP = "ad_jump";
    String LOCKREAD_POP = "lockread_pop";
    String LOCKREAD_SLIDE = "lockread_slide";
    String LOCKREAD_SWITCH = "lockread_switch";
}


