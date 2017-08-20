package com.onemena.common.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import com.arabsada.news.R;
import com.onemena.base.BaseActicity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * WebActivity Created by voler on 2017/5/31.
 * 说明：
 */

public class WebActivity extends BaseActicity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_web);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("url");
        webview.loadUrl(url);
        ivBack.setOnClickListener(v -> finish());
    }
}
