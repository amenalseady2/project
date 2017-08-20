package com.onemena.app.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.onemena.app.activity.BrowerOpenNewsDetailActivity;
import com.onemena.app.activity.MainActivity;
import com.onemena.app.config.AppConfig;
import com.onemena.app.config.Config;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.base.BaseFragment;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.data.eventbus.NightMode;
import com.onemena.home.view.adapter.NewsDetailAdapter;
import com.onemena.listener.OnActionViewClickListener;
import com.onemena.utils.SpUtil;
import com.onemena.utils.TongJiUtil;
import com.onemena.widght.HelveBoldTextView;
import com.onemena.widght.MyWebView;
import com.onemena.widght.NewsDetailSettingBusEvent;
import com.onemena.widght.NotoRegularTextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;

import de.greenrobot.event.EventBus;

import static android.widget.LinearLayout.VERTICAL;
import static com.arabsada.news.R.id.iv_close;

/**
 * Created by Administrator on 2016/11/3.
 */

public class LocalNewsDetailFragment extends BaseFragment implements View.OnClickListener, OnActionViewClickListener {

    public static final String ARTICLEDETAIL = "articleDetail";

    private String mId;
    private TextView title_newsdetail;
    private ListView listview_newsdetail;
    private NewsDetailAdapter newsDetailAdapter;
    private ImageView back_iv;
    private ImageView menu_iv;
    private MyWebView webView;
    private long start_time;
    private long end_time;
    private LinearLayout view_head;
    private FrameLayout lay_detail_center;
    private View web_view_head;
    private HelveBoldTextView txt_detail_title;
    private int title_size;
    private TextView detail_time;
    private TextView detail_com;
    private SimpleDraweeView img_detail_com;
    private NotoRegularTextView deatil_attention_btn;
    private String articleDetail;
    private TextView toolbar_divider_h;

    public static LocalNewsDetailFragment getInstance() {
        return new LocalNewsDetailFragment();
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_localnewsdetail, container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initData(View view, Bundle savedInstanceState) {

        if (SpUtil.getInt(SPKey.TITLE_SIZE) == 0) {
            SpUtil.saveValue(SPKey.TITLE_SIZE, 22);
        }
        Bundle arguments = getArguments();
//        unique_id=arguments.getString("id");
        articleDetail = arguments.getString(ARTICLEDETAIL);


        title_size = SpUtil.getInt(SPKey.TITLE_SIZE);
        EventBus.getDefault().register(this);//注册EventBus
        title_newsdetail = (TextView) view.findViewById(R.id.title_newsdetail);
        view_head = (LinearLayout) view.findViewById(R.id.head_detail_view);
        back_iv = (ImageView) view.findViewById(R.id.back_iv);
        toolbar_divider_h = (TextView) view.findViewById(R.id.toolbar_divider_h);
        menu_iv = (ImageView) view.findViewById(R.id.menu_iv);
        listview_newsdetail = (ListView) view.findViewById(R.id.listview_newsdetail);
        newsDetailAdapter = new NewsDetailAdapter(getActivity());

        lay_detail_center = (FrameLayout) view.findViewById(R.id.lay_detail_center);

        back_iv.setOnClickListener(this);
        menu_iv.setOnClickListener(this);
        menu_iv.setClickable(false);
        menu_iv.setVisibility(View.GONE);

        webView = new MyWebView(getContext());//创建自定义的webview
        web_view_head = View.inflate(mContext, R.layout.newsdetail_webview_head, null);


        txt_detail_title = (HelveBoldTextView) web_view_head.findViewById(R.id.txt_detail_title);
        detail_time = (TextView) web_view_head.findViewById(R.id.detail_time);
        detail_com = (TextView) web_view_head.findViewById(R.id.detail_com);
        img_detail_com = (SimpleDraweeView) web_view_head.findViewById(R.id.img_detail_com);
        deatil_attention_btn = (NotoRegularTextView) web_view_head.findViewById(R.id.deatil_attention_btn);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(VERTICAL);
        linearLayout.addView(web_view_head);
        linearLayout.addView(webView);

        deatil_attention_btn.setVisibility(View.GONE);

        listview_newsdetail.addHeaderView(linearLayout);
        listview_newsdetail.setAdapter(newsDetailAdapter);

        getDataFromNet(articleDetail);

        checkMode();

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

                start_time = System.currentTimeMillis();

                TongJiUtil.getInstance().putEntries(TJKey.READ,
                        MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE),
                        MyEntry.getIns(TJKey.CATEGORY_ID, AppConfig.FINE_NEWS_CATEGORY_ID),
                        MyEntry.getIns(TJKey.NEWS_TYPE, "1"),
                        MyEntry.getIns(TJKey.RESOURCE_ID, mId),
                        MyEntry.getIns(TJKey.COMMEND, "0"));
            }

        });

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
            case iv_close:
                popBackAllStack();
                break;
            case R.id.menu_iv:
//                popwinNewdetail = new PopwinNewdetailSetting(getActivity(), mId, this);//tool的popwin
//                popwinNewdetail.show(v, webView, txt_detail_title);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (start_time != 0 && iscount) {
            end_time = System.currentTimeMillis();//news_id
            TongJiUtil.getInstance().putEntries(TJKey.DETAIL_DURATION,
                    MyEntry.getIns(TJKey.CATEGORY_ID, AppConfig.FINE_NEWS_CATEGORY_ID),
                    MyEntry.getIns(TJKey.TYPE, Config.FIREBAENEWSTYPE),
                    MyEntry.getIns(TJKey.RESOURCE_ID, mId),
                    MyEntry.getIns(TJKey.COMMEND, "0"),
                    MyEntry.getIns(TJKey.DURATION, Long.toString((end_time - start_time) / 1000)));
            iscount = false;
        }

    }


    //EventBus接收消息
    public void onEventMainThread(NewsDetailSettingBusEvent event) {

    }

    //获取文章详情信息
    private void getDataFromNet(String articleDetail) {

        JSONObject jsonObject = JSONObject.parseObject(articleDetail);
        changeLocalPicture(jsonObject);
        String first_name = jsonObject.getString("first_name");
        String content = jsonObject.getString("content");
        content = getMyHtmlData(content);
        mId = jsonObject.getString("id");
        txt_detail_title.setText(jsonObject.getString("title"));
        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(1500);  //设置动画时间
        alphaAnimation.setFillAfter(true);
        webView.startAnimation(alphaAnimation);
        detail_time.setVisibility(View.GONE);
        //settingPOP中的初始化数据
//         if (StringUtils.isNotEmpty(jsonObject.getString("create_time"))) {
//             detail_time.setText(jsonObject.getString("create_time"));
//         } else {
//             detail_time.setText(jsonObject.getString("format_date"));
//         }
        detail_com.setText(first_name);

        img_detail_com.setVisibility(View.GONE);

        menu_iv.setClickable(true);
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
        data = data + "<div dir=\"auto\">" + content + "</div>";
        data = data + "</body></html>";
        return data;
    }


    public void onEventMainThread(NightMode mode) {
        checkMode();
    }

    private void checkMode() {
        boolean mode = SpUtil.getBoolean(SPKey.MODE, false);
        newsDetailAdapter.notifyDataSetChanged();
        if (mode) {
            //夜间模式
            web_view_head.setBackgroundColor(getResources().getColor(R.color.nightbg_252525));
//            img_detail_time.setImageResource(R.mipmap.details_comment_time_night);
            back_iv.setImageResource(R.mipmap.details_navbar_back_night);
            view_head.setBackgroundColor(getResources().getColor(R.color.nightbg_252525));
            lay_detail_center.setBackgroundColor(getResources().getColor(R.color.nightbg_252525));
            menu_iv.setImageResource(R.mipmap.details_navbar_more_night);
            toolbar_divider_h.setBackgroundColor(Color.parseColor("#464646"));
            detail_com.setTextColor(getResources().getColor(R.color.nighttxt_707070));
            txt_detail_title.setTextColor(getResources().getColor(R.color.nighttxt_707070));
            webView.loadUrl("javascript:" + "document.getElementsByTagName('body')[0].setAttribute('style',' text-align: right; direction: rtl;background-color:#252525;color:#707070')");

        } else {
            //白天模式
//            img_detail_time.setImageResource(R.mipmap.details_comment_time);
            back_iv.setImageResource(R.mipmap.details_navbar_back_day);
            web_view_head.setBackgroundColor(getResources().getColor(R.color.white));
            view_head.setBackgroundColor(getResources().getColor(R.color.white));
            lay_detail_center.setBackgroundColor(getResources().getColor(R.color.white));
            menu_iv.setImageResource(R.mipmap.details_navbar_more);
            toolbar_divider_h.setBackgroundColor(Color.parseColor("#D8D8D8"));
            detail_com.setTextColor(getResources().getColor(R.color.textcolor_a1a6bb));
            txt_detail_title.setTextColor(getResources().getColor(R.color.black));
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

    private void changeLocalPicture(JSONObject jsonObject) {
        String body = jsonObject.getString("content");
        if (body == null) {
            body = jsonObject.getJSONObject("articleDetail").getString("content");
        }

        Document doc = Jsoup.parse(body);
        Elements pngs = doc.select("img[src]");
        for (Element element : pngs) {
            String imgUrl = element.attr("src");
            String filePath = getLocationImagePath(imgUrl);
//                String uriPath = Uri.fromFile(new File(filePath)).toString();
            element.attr("src", filePath);
        }
        body = doc.toString();
        jsonObject.put("content", body);//存

    }

    private String getLocationImagePath(String imgUrl) {
        String[] split = imgUrl.split("/");
        String filePath = "";
        if (split != null) {
            filePath = split[split.length - 1];
        } else {
            return "";
        }
        String dirPath = "file:///android_asset/localpicture";
        return dirPath + File.separator + filePath;
//        return Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + filePath;
    }

}
