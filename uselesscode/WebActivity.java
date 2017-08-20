package com.mysada.news.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends BaseActicity {
	
	private WebView webView;


	public static void actionWebActivity(Activity act , String url , String title){
		Intent intent = new Intent(act , WebActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("title", title);
		act.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		webView = new WebView(WebActivity.this);
		webView.setHorizontalScrollBarEnabled(false);//水平不显示
		webView.setVerticalScrollBarEnabled(false); //垂直不显示
		webView.setScrollbarFadingEnabled(false);
		initWebView(webView);
		webView.loadUrl(getIntent().getExtras().getString("url"));
		setContentView(webView);
	}
	
	
	/**
	 * webview基本设置
	 * @Description: 
	 * @author 杨生辉  
	 * @param webView
	 * @return void 
	 * @date 2016-1-26 下午5:08:21
	 */
	@SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings({ "deprecation"})
	private void initWebView(final WebView webView) {
		WebSettings webSettings = webView.getSettings();
		webSettings
				.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setSavePassword(true);
		webSettings.setSaveFormData(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//
		String cacheDir = getApplicationContext()
				.getDir("webCaCheDatabase", Context.MODE_PRIVATE).getPath();
		webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
		webSettings.setAppCachePath(cacheDir);
		webSettings.setAllowFileAccess(true);
		webSettings.setAppCacheEnabled(false);
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 优先使用缓存
		webSettings.setBuiltInZoomControls(true);// 支持缩放
//		webView.setBackgroundColor(Color.argb(255, 251, 247, 237));
		
		if (android.os.Build.VERSION.SDK_INT >= 11) {
            webSettings.setPluginState(PluginState.ON);
            webSettings.setDisplayZoomControls(false);// 支持缩放
        }

		// 启用数据库
		webSettings.setDatabaseEnabled(true);
		String dir = getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();

		// 启用地理定位
		webSettings.setGeolocationEnabled(true);
		// 设置定位的数据库路径
		webSettings.setGeolocationDatabasePath(dir);

		// 最重要的方法，一定要设置，这就是出不来的主要原因

		webSettings.setDomStorageEnabled(true);

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onGeolocationPermissionsShowPrompt(String origin,
					Callback callback) {
				callback.invoke(origin, true, false);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}
		});

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("tel:")) {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri
							.parse(url));
					startActivity(intent);
				} else {
					view.loadUrl(url);
				}
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
										String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
//				showProgressDialog(mContext);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
//				dismissProgressDialog();
				view.loadUrl("javascript:onPageLoad()");
			}

		});
		
		
	}

}
