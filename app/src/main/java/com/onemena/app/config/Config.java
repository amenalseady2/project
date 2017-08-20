package com.onemena.app.config;

/**
 * 手机硬件像素基本信息
 *
 * @author yangshenghui
 */
public class Config {

//	public static final String DEVELOPER_KEY = "AIzaSyBYiX6BOJh4QLXEJLeXz_FA5jYKFlzlRkY";

    public static int displayWidth = 0;
    public static int displayHeight = 0;
    public static float density = 0;
    public static int densityDpi;
    public static int widthPixels;
    public static int heightPixels;
    public static String FIREBASE_MISSING = "com.android.NOTIFICATION_RECEIVER_ARABSADA";
    public static final String TWITTER_KEY = "C7WVAZpM0eA6XSMGiXtt7JmZx";
    public static final String TWITTER_SECRET = "Jgjk7tNbRn2SluOYHQ6pJZkITggUXrokhh74vMprFkWzLOybmB";

    public static boolean imgReport;
    public static boolean speedDebug;

    //firebase 统计类型 0-新闻 1-视频
    public static String FIREBAENEWSTYPE = "0";
    public static String FIREBAEVIDEOTYPE = "1";
    public static final String APPNAME = "mysada";

    public final static String CSS_STYLE = "<style>* {font-size:16px;line-height:20px;}p {color:#FFFFFF;}</style>";
    //	public static String DEFORT_VIDEO_TITLE ="[{\"id\":\"ca657716cdd54776b16e864571745cbf\",\"name\":\"تكنولوجيا\"}, {\"id\":\"39ebf3e67301422dbf5d6fe45b77fb67\",\"name\":\"مشاهير\"}, {\"id\":\"00051bdcbd254e5aa94869750b63a380\",\"name\":\"ترفيه\"}, {\"id\":\"d77ac7f17e5b480884bff50b6938d3c3\",\"name\":\"ألعاب\"}, {\"id\":\"28d7e931c12c4276be2a109124e1c7a1\",\"name\":\"رجال\"}, {\"id\":\"a001bbd0a0ff44909f0f0b22c9924150\",\"name\":\"علوم\"}, {\"id\":\"d650d962eb9945b4a010a906ea6f4105\",\"name\":\"سيارات\"}, {\"id\":\"759c187b67a842b7a18f933b8e4a6b20\",\"name\":\"مرأة\"}, {\"id\":\"25325338e4e6449f82c676a76bf874da\",\"name\":\"طبخ\"}, {\"id\":\"8840d8561d2c4ac684d8dc2a3c0fdf21\",\"name\":\"سياحة\"}, {\"id\":\"fc8b9c46997e4ee9bd740a0eeb636b03\",\"name\":\"صحة\"}, {\"id\":\"0534c80f16054c2880c027b393a4ab76\",\"name\":\"إقتصاد\"}, {\"id\":\"a44af5843e5843b8b3bd42114fe36dd7\",\"name\":\"رياضة\"}, {\"id\":\"c4e7119f01164597b29e89f497a72191\",\"name\":\"عملة\"}]";
    public static String DEFORT_VIDEO_TITLE = "[\n" +
            "              {\n" +
            "                  \"category_id\": \"21\",\n" +
            "                  \"category_title\": \"\\u0645\\u0646\\u062a\\u0634\\u0631\\u0629\"\n" +
            "              },\n" +
            "              {\n" +
            "                  \"category_id\": \"22\",\n" +
            "                  \"category_title\": \"\\u0623\\u062e\\u0628\\u0627\\u0631\"\n" +
            "              },\n" +
            "              {\n" +
            "                  \"category_id\": \"24\",\n" +
            "                  \"category_title\": \"\\u0645\\u0634\\u0627\\u0647\\u064a\\u0631\"\n" +
            "              },\n" +
            "              {\n" +
            "                  \"category_id\": \"25\",\n" +
            "                  \"category_title\": \"\\u0645\\u0648\\u0633\\u064a\\u0642\\u0649\"\n" +
            "              },\n" +
            "              {\n" +
            "                  \"category_id\": \"26\",\n" +
            "                  \"category_title\": \"\\u062a\\u0633\\u0644\\u064a\\u0629\"\n" +
            "              },\n" +
            "              {\n" +
            "                  \"category_id\": \"23\",\n" +
            "                  \"category_title\": \"\\u0631\\u064a\\u0627\\u0636\\u0629\"\n" +
            "              },\n" +
            "              {\n" +
            "                  \"category_id\": \"29\",\n" +
            "                  \"category_title\": \"\\u0645\\u0646\\u0648\\u0639\"\n" +
            "              },\n" +
            "              {\n" +
            "                  \"category_id\": \"27\",\n" +
            "                  \"category_title\": \"\\u0623\\u0646\\u062b\\u0649\"\n" +
            "              },\n" +
            "              {\n" +
            "                  \"category_id\": \"28\",\n" +
            "                  \"category_title\": \"\\u0637\\u0628\\u062e\"\n" +
            "              },\n" +
            "              {\n" +
            "                  \"category_id\": \"31\",\n" +
            "                  \"category_title\": \"\\u0648\\u062b\\u0627\\u0626\\u0642\\u064a\"\n" +
            "              }\n" +
            "              {\n" +
            "                  \"category_id\": \"30\",\n" +
            "                  \"category_title\": \"\\u062a\\u0642\\u0646\\u064a\\u0629\"\n" +
            "              },\n" +

            "          ]";


    public static String DEFORT_NEWS_TITLE =
            "[{\"id\":\"1\",\"icon\":\"file:///android_asset/icons/column_egypt\",\"title\":\"عربي\"}, " +
                    "{\"id\":\"8\",\"icon\":\"asset:///android_asset/icons/column_international\",\"title\":\"مشاهير\"}, " +
                    "{\"id\":\"4\",\"icon\":\"file:///android_asset/icons/column_seriea\",\"title\":\"كرة قدم\"}," +
                    "{\"id\":\"7\",\"icon\":\"asset:///android_asset/icons/column_international\",\"title\":\"ترفيه\"}, " +
                    "{\"id\":\"2\",\"icon\":\"http://www.arabsada.com/uploads/2017/03/03/58b942d159715.jpg\",\"title\":\"دولي\"}, " +
                    "{\"id\":\"5\",\"icon\":\"asset:///android_asset/icons/column_international\",\"title\":\"مصر\"}, " +
                    "{\"id\":\"17\",\"icon\":\"asset:///android_asset/icons/column_international\",\"title\":\"السعودية\"}, " +
                    "{\"id\":\"6\",\"icon\":\"asset:///android_asset/icons/column_international\",\"title\":\"تكنولوجيا\"}, " +
                    "{\"id\":\"9\",\"icon\":\"asset:///android_asset/icons/column_international\",\"title\":\"حواء\"}, " +
                    "{\"id\":\"16\",\"icon\":\"asset:///android_asset/icons/column_international\",\"title\":\"صحة\"}, " +
                    "{\"id\":\"3\",\"icon\":\"file:///android_asset/icons/column_pl\",\"title\":\"إقتصاد\"}, " +
                    "{\"id\":\"15\",\"icon\":\"asset:///android_asset/icons/column_international\",\"title\":\"طبخ\"}, " +
                    "{\"id\":\"10\",\"icon\":\"asset:///android_asset/icons/column_international\",\"title\":\"سيارات\"}, " +
                    "{\"id\":\"11\",\"icon\":\"asset:///android_asset/icons/column_international\",\"title\":\"علوم\"}, " +
                    "{\"id\":\"14\",\"icon\":\"asset:///android_asset/icons/column_international\",\"title\":\"سياحة\"}, " +
                    "{\"id\":\"12\",\"icon\":\"asset:///android_asset/icons/column_international\",\"title\":\"ألعاب\"}, " +
                    "{\"id\":\"13\",\"icon\":\"file:///android_asset/icons/column_laliga\",\"title\":\"الإمارات\"}]";

    public static String speciallist="{\"categories_text\":\"مصر\"," +
            "\"total_view\":\"0\"," +
            "\"link\":\"http://www.ektesadnews.com/%d8%a7%d9%84%d8%b3%d9%8a%d8%b3%d9%8a-%d9%8a%d8%a4%d9%83%d8%af-%d8%b9%d9%84%d9%89-%d8%aa%d8%b9%d8%b2%d9%8a%d8%b2-%d8%a5%d8%ac%d8%b1%d8%a7%d8%a1%d8%a7%d8%aa-%d8%aa%d8%a3%d9%85%d9%8a%d9%86-%d8%a7%d9%84/\"," +
            "\"posttype\":\"special\",\"video_time\":\"\",\"categories\":\"5\",\"img_url\":\"\",\"id\":\"570060\"," +
            "\"share\":\"0\",\"share_url\":\"http://share.arabsada.com/share/570060\",\"tags\":[]," +
            "\"user_name\":\"ektesadnews.com\",\"created_by\":\"84487\",\"rect_thumb_meta\"" +
            ":[\"http://www.arabsada.com/uploads/2017/04/19/58f717a118402.jpg\"],\"profile_photo\":" +
            "\"http://www.arabsada.com/uploads/profile_photos/58ec8b8257f05.jpg\",\"" +
            "comment_count\":\"16\",\"like\":\"118\",\"description\":\"content\"," +
            "\"title\":\"السيسي يؤكد على تعزيز إجراءات تأمين المطارات والسائحين\",\"unique_id\"" +
            ":\"58f717a120515\",\"top\":\"true\",\"created_at\":\"2017-04-19 07:54:09\"" +
            ",\"video_url\":\"\",\"first_name\":\"اقتصاد نيوز\",\"create_time\":\"منذ 4 أيام\",\"dislike\":\"0\"}";

}
