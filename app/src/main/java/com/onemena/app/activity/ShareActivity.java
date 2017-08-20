package com.onemena.app.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.arabsada.news.R;
import com.onemena.base.BaseActicity;

/**
 * Created by WHF on 2016-12-27.
 */

public class ShareActivity extends BaseActicity {

    private WebView share_web;
    private ProgressBar progress;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_share);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
//        String[] split = StringUtils.split( url,"//");
        url= url.replace("http://","");
        url= url.replace("https://","");
        share_web = (WebView) findViewById(R.id.share_web);
        progress = (ProgressBar) findViewById(R.id.progress);
        initWebView(share_web,url);

    }

    private void initWebView(WebView webview, String url) {
        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webview.loadUrl("https://plus.google.com/u/0/share?url="+url);
        //设置Web视图
        webview.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progress.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && share_web.canGoBack()) {
//            share_web.goBack(); //goBack()表示返回WebView的上一页面
//            return true;
//        }
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();//结束退出程序
        }
        return false;
    }
}
