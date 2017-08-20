package com.onemena.app.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.arabsada.news.R;
import com.onemena.app.activity.BrowerOpenNewsDetailActivity;
import com.onemena.app.activity.MainActivity;
import com.onemena.base.BaseFragment;
import com.onemena.utils.StringUtils;

/**
 * Created by Administrator on 2016/12/26.
 */

public class OriginalArticleFragment extends BaseFragment implements View.OnClickListener {


    public static final String NEWSURL="newsurl";
    private WebView mywebview;
    private ProgressBar progress_bar;
    private ImageView back_iv;
    private ImageView menu_iv;
    private LinearLayout lay_error;

    private float downX;
    private float downY;
    MainActivity.MyTouchListener myTouchListener = new MainActivity.MyTouchListener() {
        @Override
        public void onTouchEvent(MotionEvent event) {
            // 处理手势事件

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float upX = event.getX();
                    float upY = event.getY();
                    if (Math.abs(upY - downY) < 100 && downX - upX > 200) {
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
                    }

                    break;
            }
        }
    };
    private CommentFragment.StateChangeListener stateChangeListener;

    public static OriginalArticleFragment getInstance(String url){
          OriginalArticleFragment fragment = new OriginalArticleFragment();
          Bundle bundle = new Bundle();
          bundle.putString(NEWSURL,url);
          fragment.setArguments(bundle);
          return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_originalarticle, container, false);
        // 将myTouchListener注册到分发列表
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
        } else if (getActivity() instanceof BrowerOpenNewsDetailActivity) {
            ((BrowerOpenNewsDetailActivity) this.getActivity()).registerMyTouchListener(myTouchListener);
        }

        return view;
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        mywebview = (WebView) view.findViewById(R.id.mywebview);
        progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);

        back_iv = (ImageView) view.findViewById(R.id.back_iv);
        menu_iv = (ImageView) view.findViewById(R.id.menu_iv);
        back_iv.setOnClickListener(this);
        menu_iv.setOnClickListener(this);

        lay_error = (LinearLayout) view.findViewById(R.id.lay_error);
        lay_error.setVisibility(View.GONE);
        initWebView(mywebview);
        Bundle arguments = getArguments();
        String newsurl = arguments.getString(NEWSURL);
        if (StringUtils.isNotEmpty(newsurl)){
            mywebview.loadUrl(newsurl);
        }

    }

    private void initWebView(WebView mywebview) {
        mywebview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                progress_bar.setVisibility(View.GONE);
                lay_error.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progress_bar.setVisibility(View.GONE);
                lay_error.setVisibility(View.GONE);
                }

            }
            );

    }

    @Override
    public void onClick(View v) {
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
            case R.id.menu_iv:
                break;
        }
    }


    public void setOnStateChangeListener(CommentFragment.StateChangeListener stateChangeListener) {

        this.stateChangeListener = stateChangeListener;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) this.getActivity()).unRegisterMyTouchListener(myTouchListener);
        } else if (getActivity() instanceof BrowerOpenNewsDetailActivity) {
            ((BrowerOpenNewsDetailActivity) this.getActivity()).unRegisterMyTouchListener(myTouchListener);
        }
        stateChangeListener.onDismiss();
    }
    public  boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {

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
        }
        return true;
    }
}
