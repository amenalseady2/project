package com.onemena.widght;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.onemena.app.config.SPKey;
import com.onemena.utils.DensityUtils;
import com.onemena.utils.SpUtil;


/**
 * Created by Administrator on 2016/11/24.
 */

public class MyWebView extends WebView {
    private Context mContext;
    private MyWebViewListener mMyWebViewListener;

    public MyWebView(Context context) {
        this(context, null);
    }

    private MyWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(context, 350)));
//        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        setMinimumHeight(DensityUtils.dip2px(context, 350));
        initWebView();
    }


    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings({"deprecation"})
    private void initWebView() {
        setHorizontalScrollBarEnabled(false);//水平不显示滚动条
        WebSettings webSettings = getSettings();
        //设置自适应屏幕的代码
//        webSettings
//                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//
        String cacheDir = mContext.getApplicationContext()
                .getDir("webCaCheDatabase", Context.MODE_PRIVATE).getPath();
        webSettings.setAppCacheMaxSize(1024 * 1024 * 4);
        webSettings.setAppCachePath(cacheDir);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 优先使用缓存


//		webView.setBackgroundColor(Color.argb(255, 251, 247, 237));

        webSettings.setPluginState(WebSettings.PluginState.ON);

        // 启用数据库
        webSettings.setDatabaseEnabled(true);
        String dir = mContext.getApplicationContext()
                .getDir("database", Context.MODE_PRIVATE).getPath();

        // 启用地理定位
        webSettings.setGeolocationEnabled(true);
        // 设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);

        // 最重要的方法，一定要设置，这就是出不来的主要原因

        webSettings.setDomStorageEnabled(true);

        //设置webview字体
        webSettings.setSupportZoom(true);
        String webview_size = SpUtil.getString(SPKey.WEBVIEW_SIZE);
        switch (webview_size) {
            case "SMALLER":
                webSettings.setTextSize(WebSettings.TextSize.SMALLER);
                break;
            case "NORMAL":
                webSettings.setTextSize(WebSettings.TextSize.NORMAL);
                break;
            case "LARGER":
                webSettings.setTextSize(WebSettings.TextSize.LARGER);
                break;
            case "LARGEST":
                webSettings.setTextSize(WebSettings.TextSize.LARGEST);
                break;
            default:
                webSettings.setTextSize(WebSettings.TextSize.NORMAL);
                break;
        }

        setWebChromeClient(new WebChromeClient() {
            private boolean isLoad;

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 60 && !isLoad) {
                    setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    isLoad = true;
                }
                if (newProgress > 80) {
                    if (mMyWebViewListener != null) {
                        mMyWebViewListener.onWebViewFinished(view);
                        mMyWebViewListener = null;
                    }
                }
            }
        });

        setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (url.startsWith("tel:")) {
//                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri
//                            .parse(url));
//                    mContext.startActivity(intent);
//                } else {
//                    view.loadUrl(url);
//                }
                return true;// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

//              view.loadUrl("javascript:alertFromJs('Hello Js!')");

            }

        });


    }

    public String getWebViewData(String news_title,
                                 String news_date, String news_body) {

        String data = "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<script type=\"text/javascript\" src=\"file:///android_asset/js/feadin.js\">"
                + "</script>" +
                "</head>";

        data = data + "<body id='body' style='font-size:25px; padding:30px' align='right'>" +
                "<center><h2 align='right' style='font-size:30px;color:blue;'>" + news_title + "</h2></center>";
        data = data + "<p align='left' style='margin-left:10px'>"
                + "<span style='font-size:10px;'>"
                + news_date
                + "</span>"
                + "</p>";
        data = data + "<hr size='1' />";
        data = data + news_body;
        data = data + "</body>";
        return data;
    }

    public void setOnMyWebViewListener(MyWebViewListener myWebViewListener) {

        mMyWebViewListener = myWebViewListener;
    }

    public interface MyWebViewListener {
        void onWebViewFinished(WebView view);
    }
}
