package com.onemena.me.view.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.BuildConfig;
import com.arabsada.news.R;
import com.google.gson.internal.Streams;
import com.onemena.app.activity.MainActivity;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.Point;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.base.BaseFragment;
import com.onemena.data.UserManager;
import com.onemena.data.eventbus.LoginBean;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.data.eventbus.PointShow;
import com.onemena.listener.JsonObjectListener;
import com.onemena.service.PublicService;
import com.onemena.utils.NetworkUtil;
import com.onemena.utils.SpUtil;
import com.onemena.utils.TongJiUtil;
import com.onemena.widght.GoldMarketDialog;
import com.onemena.widght.HelveRomanTextView;
import com.onemena.widght.PopwinLongin;
import com.zhy.autolayout.AutoLinearLayout;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import okhttp3.internal.ws.RealWebSocket;

import static com.onemena.app.config.Point.removePointFlag;
import static com.onemena.app.config.TJKey.FACEBOOK_TYPE;

/**
 * Created by WHF on 2017-03-29.
 */
public class NewGoldMarketFragment extends BaseFragment implements View.OnClickListener {

    public static final String URL = "url";
    public static final String TITLE = "title";
    @BindView(R.id.web_gold_task)
    WebView webGoldTask;
    @BindView(R.id.lay_error_h5)
    AutoLinearLayout layErrorH5;
    @BindView(R.id.lay_loading_h5)
    AutoLinearLayout layLoadingH5;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.text_task_title)
    HelveRomanTextView textTaskTitle;
    private PopwinLongin popwinLongin;
    private String url;
    private String title;
    private String login_callbackID;

    public static NewGoldMarketFragment newInstance(String title, String url) {
        Bundle bundle = new Bundle();
        String token = UserManager.getUserObj().getString("User-Token");
        if (token != null) {
            Uri uri = Uri.parse(url);
            uri = uri.buildUpon()
                    .clearQuery()
                    .appendQueryParameter("random", String.valueOf(Math.random()))
                    .appendQueryParameter("userToken", token)
                    .build();
            url = uri.toString();
//            url = url+"?random=" + Math.random() + "&userToken=" + token;
        }
        bundle.putString(NewGoldMarketFragment.URL, url);
        bundle.putString(NewGoldMarketFragment.TITLE, title);
        NewGoldMarketFragment goldmarketfragment = new NewGoldMarketFragment();
        goldmarketfragment.setArguments(bundle);
        return goldmarketfragment;
    }

    public static NewGoldMarketFragment getInstance(String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(NewGoldMarketFragment.URL, url);
        bundle.putString(NewGoldMarketFragment.TITLE, title);
        NewGoldMarketFragment goldmarketfragment = new NewGoldMarketFragment();
        goldmarketfragment.setArguments(bundle);
        return goldmarketfragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(URL);
            title = getArguments().getString(TITLE);
        }
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gold_market, container, false);
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {

//        Bundle arguments = getArguments();
//        String url = arguments.getString(URL);
//        String title = arguments.getString(TITLE);
        textTaskTitle.setText(title);
        checkNetH5();
        EventBus.getDefault().register(this);

        removePointFlag(Point.GOLD_MARKET_FLAG);//改变红点状态
        EventBus.getDefault().post(new PointShow());

        if (0 < mContext.getSupportFragmentManager().getBackStackEntryCount()) {
            ivClose.setVisibility(View.VISIBLE);
        }

        WebSettings webSettings = webGoldTask.getSettings();
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webGoldTask.setWebChromeClient(new WebChromeClient());
        //支持javascript
        webSettings.setJavaScriptEnabled(true);

        webSettings.setUserAgentString(webSettings.getUserAgentString() + " Onemena/" + BuildConfig.VERSION_NAME);
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
                Uri uri = Uri.parse(url);
                checkNetH5();
                // 如果url的协议 = 预先约定的 js 协议
                // 就解析往下解析参数
                if (uri.getScheme().equals("app")) {
                    switch (uri.getAuthority()) {
                        case "login":
                            login_callbackID = uri.getQueryParameter("callbackID");
                            popwinLongin = new PopwinLongin(mContext, webGoldTask, NewGoldMarketFragment.this);
                            break;
                        case "navigation.push":
                            String url_push = uri.getQueryParameter("url");
                            if (ConfigUrls.GOLD_MARKET.equals(url_push)) {
                                NewGoldMarketFragment goldmarketfragment = NewGoldMarketFragment.newInstance(getString(R.string.金币商城), url_push);
                                addToBackStack(goldmarketfragment);
                            }else {
                                GoldTaskFragment goldTaskFragment = GoldTaskFragment.newInstance(getString(R.string.task_list_title), ConfigUrls.GOLD_TASK);
                                addToBackStack(goldTaskFragment);
                            }
                            break;
                        case "navigation.pop":
                            popBackStack();
                            break;
                        case "navigation.popTo":
                            String index1 = uri.getQueryParameter("index");
                            if (StringUtils.isNotEmpty(index1)) {
                                int index =Integer.parseInt(index1);
                                int backStackEntryCount = mContext.getSupportFragmentManager().getBackStackEntryCount();
                                if (backStackEntryCount>=index){
                                    mContext.getSupportFragmentManager().getBackStackEntryAt(index);
                                }
                            }
                            break;
                        case "navigation.bar":
                            String isHidden = uri.getQueryParameter("isHidden");
                            String title_s = uri.getQueryParameter("title");
                            String titleColor = uri.getQueryParameter("titleColor");
                            String backgroundColor = uri.getQueryParameter("backgroundColor");
                            textTaskTitle.setText(title_s);
                            break;
                        case "currentTheme":

                            break;
                        case "http":
                            String callbackID = uri.getQueryParameter("callbackID");
                            String request = uri.getQueryParameter("request");
                            if (StringUtils.isNotEmpty(request)) {
                                JSONObject request_obj = JSONObject.parseObject(request);
                                String urls = request_obj.getString("url");
                                String method = request_obj.getString("method");
                                JSONObject data = request_obj.getJSONObject("data");
                                if (data == null) {
                                    data = request_obj.getJSONObject("params");
                                }
                                if (data == null) {
                                    data = new JSONObject();
                                }
                                data.put("user_token", UserManager.getUserObj().getString("User-Token"));
                                JSONObject headers = request_obj.getJSONObject("headers");

                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                Set<Map.Entry<String, Object>> entries = data.entrySet();
                                for (Map.Entry<String, Object> entry : entries) {
                                    String key = entry.getKey();
                                    String value = "";
                                    if (entry.getValue() != null) {
                                        value = entry.getValue().toString();
                                    }

                                    hashMap.put(key, value);
                                }
                                httpMethod(callbackID, urls, method, hashMap);
                            }
                            break;
                        case "alert":
                            String callbackID_alert = uri.getQueryParameter("callbackID");
                            String message = uri.getQueryParameter("message");
                            if (StringUtils.isNotEmpty(message)) {
                                JSONObject message_obj = JSONObject.parseObject(message);
                                String title_alert = message_obj.getString("title");
                                String body_alert = message_obj.getString("body");
                                JSONArray actions_alert = message_obj.getJSONArray("actions");
                                ShowAlert(callbackID_alert,title_alert,body_alert,actions_alert);
                            }
                            break;
                    }
                } else {
                    return false;
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                layLoadingH5.setVisibility(View.VISIBLE);
                showProgressDialog(mContext);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                layLoadingH5.setVisibility(View.GONE);
                dismissProgressDialog();

            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                WebResourceResponse wrr = super.shouldInterceptRequest(view, request);
                String mode = "day";
                if (SpUtil.getBoolean(SPKey.MODE, false)) {
                    mode = "night";
                }
                String token = UserManager.getUserObj().getString("User-Token");
                String type = "visitor";
                if (StringUtils.isNotEmpty(token)) {
                    type = "google";
                }
                if (request.getUrl().toString().endsWith("OMApp.js")) {
                    AssetManager am = getResources().getAssets();
                    try {
                        InputStream is = am.open("js/OMApp.js");
                        String js=inputStream2String(is) + "\n" + stringForConfigureJavaScript(type,token);
                        wrr = new WebResourceResponse("application/x-javascript", "utf-8", new ByteArrayInputStream(js.getBytes()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return wrr;
            }
        });
        webGoldTask.loadUrl(url);
    }


    private String stringForConfigureJavaScript( String login_from, String token){

        String str="(function(){" +
                "omApp.currentTheme='"+ "day" +"'; " +
                "omApp.navigation.bar.title=''; " +
                "omApp.navigation.bar.titleColor='#000000'; " +
                "omApp.navigation.bar.isHidden=false; " +
                "omApp.navigation.bar.backgroundColor='#FFFFFF'; " +
                "omApp.currentUser.id=0;" +
                "omApp.currentUser.name=123;" +
                "omApp.currentUser.coin=1121;" +
                "omApp.currentUser.type='" + login_from + "';" +
                "omApp.currentUser.token='" + token + "';})()";
        return str;
    }

    public void onEventMainThread(LoginBean loginBean) {
        String login_from = loginBean.getContent().getLogin_from();
        String token = UserManager.getUserObj().getString("User-Token");
        webGoldTask.loadUrl("javascript:"+stringForConfigureJavaScript(login_from,token));
        if (StringUtils.isNotEmpty(token)) {
            webGoldTask.loadUrl("javascript:omApp.didFinishLogin('" + login_callbackID + "', true)");
        } else {
            webGoldTask.loadUrl("javascript:omApp.didFinishLogin('" + login_callbackID + "', false)");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_facebook_login:
                popwinLongin.dismiss();
                TongJiUtil.getInstance().putEntries(TJKey.UC_LOGIN,
                        MyEntry.getIns(TJKey.TYPE, FACEBOOK_TYPE));
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
        }
    }

    @OnClick({R.id.lay_error_h5, R.id.iv_back, R.id.iv_close})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.lay_error_h5:
                layErrorH5.setVisibility(View.GONE);
                webGoldTask.reload();
                checkNetH5();
                break;
            case R.id.iv_back:
                if (webGoldTask.canGoBack()) {
                    webGoldTask.goBack();
                } else {
                    popBackStack();
                }
                break;
            case R.id.iv_close:
                popBackAllStack();
                break;
        }
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


    /*
    alert 弹框
     */
    private void ShowAlert(String callbackID_alert,String title_alert, String body_alert, JSONArray actions_alert) {
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(mContext);
        normalDialog.setTitle(title_alert);
        normalDialog.setMessage(body_alert);
        if (actions_alert == null) {
            actions_alert = new JSONArray();
            actions_alert.add(" تأكيد ");
        }
        for (int j = 0; j < actions_alert.size(); j++) {
            String js_alert="javascript:omApp.didSelectAlertActionAtIndex('"+callbackID_alert+"', "+j+");";
            switch (j){
                case 0:
                    normalDialog.setPositiveButton(actions_alert.get(j).toString(),(dialogInterface, i) -> {
                        webGoldTask.loadUrl(js_alert);
                    });
                    break;
                default:
                    normalDialog.setNegativeButton(actions_alert.get(j).toString(),(dialogInterface, i) -> {
                        webGoldTask.loadUrl(js_alert);
                    });
                    break;
            }
        }
        normalDialog.show();
    }

    /*
    输入了转String
     */
    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    /*
    http 请求返回
     */
    private void httpMethod(final String callbackID, String urls, String method, HashMap<String, String> hashMap) {
        if ("GET".equals(method)) {
            PublicService.getInstance().getJsonObjectRequest(urls, hashMap, new JsonObjectListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onJsonObject(JSONObject obj, Boolean isCacheData) throws Exception {
                    String result_string = obj.toString();
                    String anEncodedString = URLEncoder.encode(result_string, "UTF-8").replaceAll("\\+", "%20");;
                    String js = "javascript:omApp.didFinishHTTPRequest('" + callbackID + "', true, '" + anEncodedString + "', 'application/json')";
                    webGoldTask.evaluateJavascript(js, s -> {

                    });
                }

                @Override
                public void onObjError() {
                    String js = "javascript:omApp.didFinishHTTPRequest('" + callbackID + "', false)";
                    webGoldTask.loadUrl(js);
                }
            });
        } else {

//       webGoldTask.loadUrl("javascript:window.onerror=function(msg,url,line){alert('msg:'+msg+'\n; url: '+url+'; line: '+line);}");
            PublicService.getInstance().postJsonObjectRequest(false, urls, hashMap, new JsonObjectListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onJsonObject(JSONObject obj, Boolean isCacheData) throws Exception {
                    String result_string = obj.toString();
                    String anEncodedString = URLEncoder.encode(result_string, "UTF-8").replaceAll("\\+", "%20");
                    String js = "javascript:omApp.didFinishHTTPRequest('" + callbackID + "', true, '" + anEncodedString + "', 'application/json')";
                    webGoldTask.evaluateJavascript(js, s -> {
                    });
                }

                @Override
                public void onObjError() {
                    String js = "javascript:omApp.didFinishHTTPRequest('" + callbackID + "', false)";
                    webGoldTask.loadUrl(js);
                }
            });
        }
    }

    private void checkNetH5() {
        if (NetworkUtil.checkNetWork(mContext)) {
            layErrorH5.setVisibility(View.GONE);
        } else {
            layErrorH5.setVisibility(View.VISIBLE);
        }
    }
}
