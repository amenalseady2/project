package com.onemena.home.view.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.activity.BrowerOpenNewsDetailActivity;
import com.onemena.app.activity.MainActivity;
import com.onemena.app.config.SPKey;
import com.onemena.data.eventbus.NightMode;
import com.onemena.base.BaseFragment;
import com.onemena.listener.OnActionViewClickListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.onemena.utils.CheckModeUtil;
import com.onemena.utils.LogManager;
import com.onemena.utils.SpUtil;
import com.onemena.widght.HelveBoldTextView;
import com.onemena.widght.MyWebView;
import com.onemena.widght.PopwinNewdetailSetting;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/11/3.
 */

public class OfflineNewsDetailFragment extends BaseFragment implements View.OnClickListener, OnActionViewClickListener {

    public static final String JSONSTRING="jsonString";
    private String mId;
    private TextView title_newsdetail;
    private Handler handler = new Handler();
    private ImageView back_iv;
    private ImageView menu_iv;
    private MyWebView webView;
    private LinearLayout ll_web_view;
    float downX = 0;
    float downY = 0;
    private PopwinNewdetailSetting popwinNewdetail;
    private int likeState = -1;
    private int likeNum;
    private int disLikeNum;
    private boolean isSaved;
    private long start_time;
    private long end_time;
    private String category_id;
    private String title;

    private int title_size;
    private GenericDraweeHierarchy img_detail_comHierarchy;
    private String created_by;
    private String is_follow;
    private String jsonString;
    private HelveBoldTextView txt_detail_title;
    private LinearLayout head_detail_view;
    private TextView toolbar_divider_h,detail_offline_com,detail_offline_line;

    public static OfflineNewsDetailFragment getInstance() {
        return new OfflineNewsDetailFragment();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offline_news_detaill, container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initData(View view, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        jsonString = arguments.getString(JSONSTRING);
        if (SpUtil.getInt(SPKey.TITLE_SIZE) == 0) {
            SpUtil.saveValue( SPKey.TITLE_SIZE, 22);
        }
        title_size = SpUtil.getInt( SPKey.TITLE_SIZE);
        EventBus.getDefault().register(this);//注册EventBus
        title_newsdetail = (TextView) view.findViewById(R.id.title_newsdetail);
//        webView = (MyWebView) view.findViewById(R.id.web_view);
        ll_web_view = (LinearLayout) view.findViewById(R.id.ll_web_view);
        back_iv = (ImageView) view.findViewById(R.id.back_iv);
        menu_iv = (ImageView) view.findViewById(R.id.menu_iv);
        txt_detail_title = (HelveBoldTextView) view.findViewById(R.id.txt_detail_title);
        head_detail_view = (LinearLayout) view.findViewById(R.id.head_detail_view);
        toolbar_divider_h = (TextView) view.findViewById(R.id.toolbar_divider_h);
        detail_offline_com = (TextView) view.findViewById(R.id.detail_offline_com);
        detail_offline_line = (TextView) view.findViewById(R.id.detail_offline_line);

        webView=new MyWebView(mContext);
        ll_web_view.addView(webView);
        ImageView imageView=new ImageView(mContext);
        ImageView imageView2=new ImageView(mContext);
        imageView.setImageResource(R.mipmap.offlindown_bottom_img_);
        imageView2.setImageResource(R.mipmap.offlindown_bottom_img_);
        imageView2.setVisibility(View.INVISIBLE);
        ll_web_view.addView(imageView);
        ll_web_view.addView(imageView2);

        back_iv.setOnClickListener(this);
//        menu_iv.setOnClickListener(this);
//        menu_iv.setClickable(false);

        //webview加载完成后，访问更多推荐新闻
        //webview 的头

        webView.setOnMyWebViewListener(new MyWebView.MyWebViewListener() {
            @Override
            public void onWebViewFinished(WebView view) {
                if (SpUtil.getBoolean(SPKey.MODE, false)) {

                    txt_detail_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, title_size);//标题文字大小
                    checkWebViewFontSize();
                    webView.loadUrl("javascript:" + "document.getElementsByTagName('body')[0].setAttribute('style',' text-align: right;direction: rtl;background-color:#252525;color:#707070')");
                    webView.getSettings().setCursiveFontFamily("NotoNaskhArabic-Regular.ttf");
                } else {
                    checkWebViewFontSize();
                    txt_detail_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, title_size);//标题文字大小
                    webView.loadUrl("javascript:" + "document.getElementsByTagName('body')[0].setAttribute('style',' text-align: right; direction: rtl')");
                    webView.getSettings().setCursiveFontFamily("NotoNaskhArabic-Regular.ttf");
                }
            }
        });

        getDataFromNet();
        checkMode();

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.back_iv:
                if (getActivity() instanceof MainActivity) {
                    popBackStack();
                } else if (getActivity() instanceof BrowerOpenNewsDetailActivity) {
                    int backStackEntryCount = getFragmentManager().getBackStackEntryCount();
                    if (backStackEntryCount >= 1) {
                        popBackStack();
                    } else {
                        getActivity().finish();
                    }
                }
                break;
        }
    }


    @Override
    public void onBackStackChanged() {
        super.onBackStackChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    //获取文章详情信息
    private void getDataFromNet() {
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        JSONObject user_info = jsonObject.getJSONObject("user_info");
        String content = jsonObject.getString("content");
//        String content = "<p><img src=\"http://www.mysada.com/uploads/2017/01/12/5877004cb3437.jpg\"></p>";
        LogManager.i(content);
        content = getMyHtmlData(content);
        category_id = jsonObject.getString("categories");
        mId = jsonObject.getString("id");
        created_by = jsonObject.getString("created_by");
        is_follow = jsonObject.getString("is_follow");
        detail_offline_com.setText(jsonObject.getString("categories_text"));
        checkMode(detail_offline_line,detail_offline_com,category_id);

        txt_detail_title.setText(Html.fromHtml(jsonObject.getString("title")));
        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
        checkMode();
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(1500);  //设置动画时间
        alphaAnimation.setFillAfter(true);
        webView.startAnimation(alphaAnimation);
    }


    /**
     * 导入本地字库设置webview 字体
     *
     * @param content
     * @return
     */
    private String getMyHtmlData(String content) {
        String data = "<!doctype html> <html><head>" + "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width;\" user-scalable=\"0;\" maximum-scale=\"1.0;\" initial-scale=\"1.0;\">" +
                "<link type=\"text/css\"  href=\"file:///android_asset/css/webview.css\" rel=\"stylesheet\">" +
                "</head>";
        data = data + "<body >";
        data = data + "<div dir=\"auto\">"+content+"</div>";
        data = data + "</body></html>";
        return data;
    }


    public void onEventMainThread(NightMode mode) {
        checkMode();
    }

    private void checkMode() {
        boolean mode = SpUtil.getBoolean(SPKey.MODE, false);
        if (mode) {
            //夜间模式
//            img_detail_time.setImageResource(R.mipmap.details_comment_time_night);
            ll_web_view.setBackgroundColor(getResources().getColor(R.color.nightbg_252525));
            head_detail_view.setBackgroundColor(getResources().getColor(R.color.nightbg_252525));
            toolbar_divider_h.setBackgroundColor(getResources().getColor(R.color.txt_464646));
            back_iv.setImageResource(R.mipmap.details_navbar_back_night);
            menu_iv.setImageResource(R.mipmap.details_navbar_more_night);
            webView.loadUrl("javascript:" + "document.getElementsByTagName('body')[0].setAttribute('style',' text-align: right; direction: rtl;background-color:#252525;color:#707070')");

        } else {
            //白天模式
//            img_detail_time.setImageResource(R.mipmap.details_comment_time);
            ll_web_view.setBackgroundColor(getResources().getColor(R.color.white));
            head_detail_view.setBackgroundColor(getResources().getColor(R.color.white));
            toolbar_divider_h.setBackgroundColor(getResources().getColor(R.color.textcolor_d8d8d8));
            back_iv.setImageResource(R.mipmap.details_navbar_back_day);
            menu_iv.setImageResource(R.mipmap.details_navbar_more);

            webView.loadUrl("javascript:" + "document.getElementsByTagName('body')[0].setAttribute('style',' text-align: right; direction: rtl')");
        }
    }

    /**
     * 设置webview 字体
     */
    private void checkWebViewFontSize() {
        String webview_size = SpUtil.getString(SPKey.WEBVIEW_SIZE);
        switch (webview_size) {
            case "صغير":
                webView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                break;
            case "طبيعي":
                webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                break;
            case "كبير":
                webView.getSettings().setTextSize(WebSettings.TextSize.LARGER);
                break;
            case "الأكبر":
                webView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
                break;
            default:
                break;
        }
    }

    private void checkMode(TextView txt_line, TextView txt_com,String categories) {
        switch (categories){
            case "0":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_推荐,R.color.down_N_推荐,txt_com,txt_line);
                break;
            case "1":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_中东,R.color.down_N_中东,txt_com,txt_line);
                break;
            case "2":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_国际,R.color.down_N_国际,txt_com,txt_line);
                break;
            case "3":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_金融,R.color.down_N_金融,txt_com,txt_line);
                break;
            case "4":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_足球,R.color.down_N_足球,txt_com,txt_line);
                break;
            case "5":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_埃及,R.color.down_N_埃及,txt_com,txt_line);
                break;
            case "6":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_IT,R.color.down_N_IT,txt_com,txt_line);
                break;
            case "7":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_娱乐,R.color.down_N_娱乐,txt_com,txt_line);
                break;
            case "8":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_明星,R.color.down_N_明星,txt_com,txt_line);
                break;
            case "9":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_女性,R.color.down_N_女性,txt_com,txt_line);
                break;
            case "10":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_汽车,R.color.down_N_汽车,txt_com,txt_line);
                break;
            case "11":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_科学,R.color.down_N_科学,txt_com,txt_line);
                break;
            case "12":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_游戏,R.color.down_N_游戏,txt_com,txt_line);
                break;
            case "13":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_阿联酋,R.color.down_N_阿联酋,txt_com,txt_line);
                break;
            case "14":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_旅行,R.color.down_N_旅行,txt_com,txt_line);
                break;
            case "15":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_美食,R.color.down_N_美食,txt_com,txt_line);
                break;
            case "16":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_健康,R.color.down_N_健康,txt_com,txt_line);
                break;
            case "17":
                CheckModeUtil.changeModeTextColor(mContext,R.color.down_D_沙特,R.color.down_N_沙特,txt_com,txt_line);
                break;
        }
    }
}
