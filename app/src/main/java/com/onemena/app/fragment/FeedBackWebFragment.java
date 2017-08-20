package com.onemena.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.arabsada.news.R;
import com.onemena.base.BaseFragment;

/**
 * Created by WHF on 2016-12-10.
 */

public class FeedBackWebFragment extends BaseFragment {

    public static final String URL="url";
    private WebView web_faq;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  View.inflate(getContext(), R.layout.fragment_feedbackweb,null);
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.img_feedback_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        String url = getArguments().getString(URL);
        web_faq=(WebView) view.findViewById(R.id.web_faq);
        web_faq.loadUrl(url);
    }
}
