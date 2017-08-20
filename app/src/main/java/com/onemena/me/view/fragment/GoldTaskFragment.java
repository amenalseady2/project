package com.onemena.me.view.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.arabsada.news.R;
import com.onemena.app.activity.MainActivity;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.Point;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.data.UserManager;
import com.onemena.data.eventbus.LoginBean;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.data.eventbus.PointShow;
import com.onemena.base.BaseFragment;
import com.onemena.utils.NetworkUtil;
import com.onemena.utils.SpUtil;
import com.onemena.utils.TongJiUtil;
import com.onemena.widght.HelveRomanTextView;
import com.onemena.widght.PopwinLongin;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

import static com.onemena.app.config.Point.removePointFlag;
import static com.onemena.app.config.TJKey.FACEBOOK_TYPE;

/**
 * Created by WHF on 2017-03-29.
 */
public class GoldTaskFragment extends BaseFragment implements View.OnClickListener {

    public static final String URL="url";
    public static final String TITLE="title";

    @BindView(R.id.text_task_title)
    HelveRomanTextView textTaskTitle;
    @BindView(R.id.web_gold_task)
    WebView webGoldTask;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.lay_error_h5)
    AutoLinearLayout layErrorH5;
    @BindView(R.id.lay_loading_h5)
    AutoLinearLayout layLoadingH5;
    private PopwinLongin popwinLongin;


    public static GoldTaskFragment newInstance(String title,String url){

        Bundle bundle = new Bundle();
        bundle.putString(GoldTaskFragment.TITLE, title);

        int mode = SpUtil.getInt(SPKey.NIGHT_MODE);
        String token = UserManager.getUserObj().getString("User-Token");
        if (token == null) {
            token = "";
        }
        Uri uri=Uri.parse(url);
       uri=uri.buildUpon()
                .clearQuery()
                .appendQueryParameter("random", String.valueOf(Math.random()))
                .appendQueryParameter("userToken",token)
                .appendQueryParameter("night", String.valueOf(mode))
                .build();
        url = uri.toString();
//        url = url +"?random="+ Math.random() + "&userToken=" + token + "&night=" + mode;
        bundle.putString(GoldTaskFragment.URL, url);

        GoldTaskFragment goldTaskFragment = new GoldTaskFragment();
        goldTaskFragment.setArguments(bundle);
        return goldTaskFragment;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gold_task, container, false);
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        String url = arguments.getString(URL);
        String title = arguments.getString(TITLE);
        textTaskTitle.setText(title);
        ivBack.setOnClickListener(this);
        checkNetH5();
        EventBus.getDefault().register(this);

        removePointFlag(Point.GOLD_TASK_FLAG);//改变红点状态
        EventBus.getDefault().post(new PointShow());

        WebSettings webSettings = webGoldTask.getSettings();
        webGoldTask.setWebChromeClient(new WebChromeClient());
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //支持javascript
        webSettings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        //扩大比例的缩放
        webSettings.setUseWideViewPort(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);


        //如果不设置WebViewClient，请求会跳转系统浏览器
        webGoldTask.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
                checkNetH5();
                Uri uri = Uri.parse(url);
                // 如果url的协议 = 预先约定的 js 协议
                // 就解析往下解析参数
                if (uri.getScheme().equals("app")){

                    switch (uri.getAuthority()){
                        case "push":
                            TongJiUtil.getInstance().putEntries(TJKey.SHOP_ENT,MyEntry.getIns(TJKey.TYPE, "2"));
                            String title_push = uri.getQueryParameter("navigationBarTitle");
                            String url_push = uri.getQueryParameter("url");
                            NewGoldMarketFragment fragment_push=NewGoldMarketFragment.getInstance(title_push,url_push);
                            addToBackStack(fragment_push);
                            break;
                        case "login":
                            popwinLongin = new PopwinLongin(mContext, textTaskTitle, GoldTaskFragment.this);
                            break;

                    }
                }else {
//                    GoldTaskFragment goldTaskFragment = GoldTaskFragment.newInstance(getString(R.string.task_detail_title),url);
//                    addToBackStack(goldTaskFragment);
                    return false;
                }

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                layLoadingH5.setVisibility(View.VISIBLE);
                showProgressDialog(mContext);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                layLoadingH5.setVisibility(View.GONE);
                dismissProgressDialog();
            }
        });


//        webGoldTask.addJavascriptInterface(new Object() {
//            @JavascriptInaaterface
//            public void login() {
//                popwinLongin = new PopwinLongin(mContext, textTaskTitle, GoldTaskFragment.this);
//            }
//        }, "AndroidWebView");
        webGoldTask.loadUrl(url);
    }

    private void checkNetH5() {
        if (NetworkUtil.checkNetWork(mContext)) {
            layErrorH5.setVisibility(View.GONE);
        } else {
            layErrorH5.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.tv_facebook_login:
                popwinLongin.dismiss();
                TongJiUtil.getInstance().putEntries(TJKey.UC_LOGIN,
                        MyEntry.getIns(TJKey.TYPE,FACEBOOK_TYPE));
                ((MainActivity) mContext).signInFacebook();
                break;
            case R.id.tv_tiwwer_login:
                popwinLongin.dismiss();
                TongJiUtil.getInstance().putEntries(TJKey.UC_LOGIN,
                        MyEntry.getIns(TJKey.TYPE, TJKey.TWITTER_TYPE));
                ((MainActivity) mContext).signInTwitter();
                break;
            case R.id.tv_google_login:
                popwinLongin.dismiss();
                TongJiUtil.getInstance().putEntries(TJKey.UC_LOGIN,
                        MyEntry.getIns(TJKey.TYPE, TJKey.GOOGLE_TYPE));
                ((MainActivity) mContext).signInGoogle();
                break;
            case R.id.iv_back:
                if (webGoldTask.canGoBack()) {
                    webGoldTask.goBack();
                } else {
                    popBackStack();
                }

                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public void onEventMainThread(LoginBean loginBean) {
        int mode = SpUtil.getInt(SPKey.NIGHT_MODE);
        String token = UserManager.getUserObj().getString("User-Token");
        if (token == null) {
            token = "";
        }
        Uri uri=Uri.parse(ConfigUrls.GOLD_TASK);
        uri=uri.buildUpon()
                .clearQuery()
                .appendQueryParameter("random", String.valueOf(Math.random()))
                .appendQueryParameter("userToken",token)
                .appendQueryParameter("night", String.valueOf(mode))
                .build();
        final String url = uri.toString();
        if (isLastActiveFragment(mContext,this)) {
            webGoldTask.loadUrl(url);
        }else {
            webGoldTask.postDelayed(new Runnable() {
                @Override
                public void run() {
                    webGoldTask.loadUrl(url);
                }
            }, 500);
        }
//        String url = ConfigUrls.GOLD_TASK + "?random="+ Math.random() +"&userToken=" + token + "&night=" + mode;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webGoldTask.canGoBack()) {
                webGoldTask.goBack();
            } else {
                popBackStack();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick(R.id.lay_error_h5)
    public void onClick() {
        layErrorH5.setVisibility(View.GONE);
        webGoldTask.reload();
        checkNetH5();
    }
}