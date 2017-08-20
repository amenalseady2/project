package com.onemena.net;

import android.os.Build;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.onemena.app.config.AppConfig;
import com.onemena.data.UserManager;
import com.onemena.service.AsyHttp;
import com.onemena.utils.GetPhoneInfoUtil;
import com.onemena.utils.LogManager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * /**
 * Created by 张玉水 on 2016/6/12.
 * <p>
 * 自定义Request，通过GSON解析json格式的response。带缓存请求功能。
 */
public class GsonRequest<T> extends Request<T> {
    private final Map<String, String> params;
    private final Response.Listener<T> listener;
    private String MY_APP_NAME = "News";
    private String MY_APP_VERSION_NAME = "FirstVersion";


    /**
     * 初始化
     *
     * @param method        请求方式
     * @param url           请求地址
     * @param params        请求参数，可以为null
     * @param listener      处理响应的监听器
     * @param errorListener 处理错误信息的监听器
     */
    public GsonRequest(int method, String url, Map<String, String> params, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.params = params;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        //TODO 默认处理，如需自定义header，可重写
        Map<String, String> headers = super.getHeaders();

        if (headers == null|| headers.size()==0) {
            headers = new HashMap<String, String>();
            headers.put("Access-Token", GetPhoneInfoUtil.INSTANCE.getAndroidId());
            headers.put("Server-Version", "2.0");
            String userToken = UserManager.getUserObj().getString("User-Token");
            boolean isNotBlank = org.apache.commons.lang3.StringUtils.isNotBlank(userToken);
            if (isNotBlank) {
                headers.put("User-Token", userToken);
            }
            headers.put("App-Version", AppConfig.VERSION_NAME);
            headers.put("Connection", "keep-alive");
            headers.put("Unit-Type", Build.MODEL);
            headers.put("Sys-Version", Build.VERSION.RELEASE+"");
//            headers.put("Charset", "UTF-8");
//        headers.put("Content-Type", "application/json");
//        headers.put("Accept-Encoding", "gzip,deflate");
//        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
//        headers.put("Connection", "keep-alive");
//        headers.put("Accept", "*/*");
//        headers.put("Cache-Control", "no-cache");
//        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//        headers.put("User-Agent", String.format("%s(Linux; Android %s; %s Build/%s)", MY_APP_NAME, Build.VERSION.RELEASE, Build.MANUFACTURER, Build.ID));
        }


        LogManager.i("session", headers.toString());
        return headers;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        //将response返回到public
        AsyHttp.ResponseListener responseListener = ((AsyHttp.RequestListener) listener).getResponseListener();
        responseListener.onResponse(response);

        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success((T) parsed, HttpHeaderParser.parseCacheHeaders(response));

    }

}