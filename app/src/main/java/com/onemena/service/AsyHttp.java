package com.onemena.service;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.onemena.app.NewsApplication;
import com.onemena.net.GsonRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 张玉水 on 2016/6/12.
 */
public class AsyHttp {
    private static final int defaultRequestCode = 9999;
    /**
     * 过滤重复请求。保存当前正在消息队列中执行的Request.key为对应的requestCode.
     */
    private static final HashMap<Integer, Request> mInFlightRequests =
            new HashMap<Integer, Request>();
    /**
     * 消息队列，全局使用一个
     */
//    private static RequestQueue mRequestQueue = Volley.newRequestQueue(NewsApplication.getInstance(),5*1024*1024*8);
    private static RequestQueue mRequestQueue = NewsApplication.getInstance().getRequestQueue();


    /**
     * 添加一个请求到请求队列
     *
     * @param requestCode 请求的唯一标识码
     * @return 返回该Request，方便链式编程
     */
    public static Request addRequest(Request<?> request, int requestCode) {
        if (mRequestQueue != null && request != null) {
            request.setTag(requestCode);
            mRequestQueue.add(request);
        }
        return mInFlightRequests.put(requestCode, request);//添加到正在处理请求中
    }

    /**
     * 取消请求
     *
     * @param requestCode 请求的唯一标识码
     */

    public static Request cancelRequest(int requestCode) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(requestCode);//从请求队列中取消对应的任务
        }
        return mInFlightRequests.remove(requestCode);//从集合中删除对应的任务
    }


    /**
     * 从Map集合中构建一个get请求参数字符串
     *
     * @param param get请求map集合
     * @return get请求的字符串结构
     */
    private static String buildGetParam(Map<String, String> param) {

        StringBuilder buffer = new StringBuilder();
        if (param != null) {
            buffer.append("?");
            for (Map.Entry<String, String> entry : param.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
                    continue;
                }
                try {
                    buffer.append(URLEncoder.encode(key, "UTF-8"));
                    buffer.append("=");
                    buffer.append(URLEncoder.encode(value, "UTF-8"));
                    buffer.append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }
        String str = buffer.toString();
        //去掉最后的&
        if (str.length() > 1 && str.endsWith("&")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 生成公共Header头信息
     *
     * @return
     */
    private static Map<String, String> generateHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        //        appkey		身份标识，服务器端进行识别
        //        udid		    客户端硬件标识
        //        os			ios& android& WM7
        //        osversion	    5.0
        //        appversion	app发布版本
        //        sourceid
        //        ver
        //        userid		登录完之后传客户端
        //        usersession	登录标识
        //        unique		app自动激活后服务器返回标识


        return headers;
    }

    /**
     * 发送GsonRequest请求
     *
     * @param method      请求方式
     * @param url         请求地址
     * @param params      请求参数,可以为null
     * @param requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    处理响应的监听器
     * @param isCache     是否需要缓存本次响应的结果
     */

    private static void request(int method, String url, Map<String, String> params, final int requestCode, final ResponseListener listener, boolean isCache) {
//        if (mInFlightRequests.get(requestCode) == null) {
        GsonRequest request;
        if (method == Request.Method.GET) {
            request = makeRBRequest(method, url + buildGetParam(params), null, requestCode, listener, isCache);
        } else {
            request = makeRBRequest(method, url, params, requestCode, listener, isCache);
        }
        addRequest(request, requestCode);
//        } else {
//            LogManager.i("Hi guy,the request (RequestCode is " + requestCode + ")  is already in-flight , So Ignore!");
//        }
    }

    /**
     * 发送get方式的GsonRequest请求,默认缓存请求结果
     *
     * @param url      请求地址
     * @param params   GET请求参数，拼接在URL后面。可以为null
     * @param listener 处理响应的监听器
     */
    public static void get(String url, Map<String, String> params, final ResponseListener listener) {
        request(Request.Method.GET, url, params, defaultRequestCode, listener, true);
    }

    /**
     * 发送get方式的GsonRequest请求,默认缓存请求结果
     *
     * @param url         请求地址
     * @param params      GET请求参数，拼接在URL后面。可以为null
     * @param requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    处理响应的监听器
     */
    public static void get(String url, Map<String, String> params, final int requestCode, final ResponseListener listener) {
        request(Request.Method.GET, url, params, requestCode, listener, true);
    }

    /**
     * 发送get方式的GsonRequest请求
     *
     * @param url         请求地址
     * @param params      GET请求参数，拼接在URL后面。可以为null
     * @param requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    处理响应的监听器
     * @param isCache     是否需要缓存本次响应的结果,没有网络时会使用本地缓存
     */
    public static void get(String url, Map<String, String> params, final int requestCode, final ResponseListener listener, boolean isCache) {
        request(Request.Method.GET, url, params, requestCode, listener, isCache);
    }

    /**
     * 发送get方式的String请求
     *
     * @param url         请求地址
     * @param params      GET请求参数，拼接在URL后面。可以为null
     *  requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    处理响应的监听器
     *  isCache     是否需要缓存本次响应的结果,没有网络时会使用本地缓存
     */
    public static void getString(final String url, Map<String, String> params, final ResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                        listener.onGetResponseSuccess(defaultRequestCode,response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Cache.Entry entry = mRequestQueue.getCache().get(url);
                listener.onGetResponseError(defaultRequestCode, error,entry==null?"":new String(entry.data));

            }
        });
        addRequest(stringRequest, defaultRequestCode);

    }
    public static void getString(final String url, Map<String, String> params, int timeout,final ResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                        listener.onGetResponseSuccess(defaultRequestCode,response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Cache.Entry entry = mRequestQueue.getCache().get(url);
                listener.onGetResponseError(defaultRequestCode, error,entry==null?"":new String(entry.data));

            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addRequest(stringRequest, defaultRequestCode);

    }
//    private class MyStringRequest extends StringRequest {
//
//        public MyStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
//            super(method, url, listener, errorListener);
//        }
//
//        @Override
//        public void deliverError(VolleyError error) {
//            if (error instanceof NoConnectionError) {
//                Cache.Entry entry = this.getCacheEntry();
//                if(entry != null) {
//                    Response<String> response = parseNetworkResponse(new NetworkResponse(entry.data, entry.responseHeaders));
//                    deliverResponse(response.result);
//                    return;
//                }
//            }
//            super.deliverError(error);
//        }
//    }

//    private class MyJsonObjectRequest extends JsonObjectRequest {
//        public MyJsonObjectRequest(int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener)
//        {
//            super(method, url, listener, errorListener);
//        }
//
//        /*
//        * 没有网的情况也是出现了异常，然后就会调用到deliverError
//        * */
//        @Override
//        public void deliverError(VolleyError error) {
//            if(error instanceof NoConnectionError)
//            {
//                Cache.Entry entry = this.getCacheEntry();
//                if (entry != null)
//                {
//                    Response<JSONObject> response = parseNetworkResponse(new NetworkResponse(entry.data, entry.responseHeaders));
//                    deliverResponse(response.result);
//                    return ;
//                }
//            }
//            super.deliverError(error);
//        }
//    }

    /**
     * 发送post方式的GsonRequest请求，默认缓存请求结果
     *
     * @param url      请求地址
     * @param params   请求参数，可以为null
     * @param listener 处理响应的监听器
     */
    public static void post(String url, Map<String, String> params, final ResponseListener listener) {
        request(Request.Method.POST, url, params, defaultRequestCode, listener, false);
    }

    /**
     * 发送post方式的GsonRequest请求，默认缓存请求结果
     *
     * @param url         请求地址
     * @param params      请求参数，可以为null
     * @param requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    处理响应的监听器
     */
    public static void post(String url, Map<String, String> params, final int requestCode, final ResponseListener listener) {
        request(Request.Method.POST, url, params, requestCode, listener, false);//POST请求不缓存
    }


    /**
     * 初始化一个RBRequest
     *
     * @param method      请求方法
     * @param url         请求地址
     * @param params      请求参数，可以为null
     * @param requestCode 请求码 每次请求对应一个code作为改Request的唯一标识
     * @param listener    监听器用来响应结果
     * @return 返回一个RBRequest对象
     */
    private static GsonRequest makeRBRequest(int method, String url, Map<String, String> params, int requestCode, ResponseListener listener, boolean isCache) {
        RequestListener requestListener = new RequestListener(requestCode, listener,url);
        GsonRequest request = new GsonRequest(method, url, params, requestListener, requestListener);

        request.setRetryPolicy(new DefaultRetryPolicy(10000, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));//设置超时时间，重试次数，重试因子（1,1*2,2*2,4*2）等
        request.setTag(url);//以requestCode为标记，方便移除
        request.setShouldCache(true);
        return request;
    }

    /**
     * 成功获取到服务器响应结果的监听，供UI层注册
     */
    public interface ResponseListener {
        /**
         * 当成功获取到服务器响应结果的时候调用
         *
         * @param requestCode response对应的requestCode
         * @param response    返回的response
         */
        void onGetResponseSuccess(int requestCode, String response);

        /**
         * 网络请求失败，做一些释放性的操作，比如关闭对话框
         *
         * @param requestCode 请求码
         * @param error       异常详情
         */
        void onGetResponseError(int requestCode, VolleyError error,String cacheResponse);

        void onResponse(NetworkResponse response);
    }


    /**
     * RequestListener，封装了Volley错误和成功的回调监，并执行一些默认处理，同时会将事件通过OnGetResponseListener抛到UI层
     */
    public static class RequestListener implements Response.ErrorListener, Response.Listener<String> {

        private ResponseListener listener;
        private String url;
        private int requestCode;

        public RequestListener(int requestCode, ResponseListener listener, String url) {
            this.requestCode = requestCode;
            this.listener = listener;
            this.url = url;
        }


        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            Request request = mInFlightRequests.remove(requestCode);//请求错误，从正在飞的集合中删除该请求

            /*****缓存的代码***/
            Cache cache = mRequestQueue.getCache();
            Cache.Entry entry = cache.get(url);
            String data = "";
            if (entry != null) {
                try {
                    data = new String(entry.data, "UTF-8");
                    // handle data, like converting it to xml, json, bitmap etc.,
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (listener != null) {
                listener.onGetResponseError(requestCode, error,data);
            }

//            ToastUtil.showNormalShortToast("服务器请求失败");
            //请求失败，解析本地缓存
//            getCacheResponse(request);
        }


        @Override
        public void onResponse(String response) {
            mInFlightRequests.remove(requestCode);//请求成功，从正在飞的集合中删除该请求
            if (response != null) {
                //执行通用处理，如果是服务器返回的ErrorResponse，直接提示错误信息并返回,待处理。。。。。。。。。。。。。
//                if ("error".equals(response.getResponse()) && response instanceof ErrorResponse) {
//                    ErrorResponse errorResponse = (ErrorResponse) response;
//                    ToastUtil.showNormalShortToast(errorResponse.getError().getText());
//                    return;
//                }
//                LogManager.i(response);
//                ToastUtil.showNormalShortToast(response);
                if (listener != null) {
                    listener.onGetResponseSuccess(requestCode, response);
                }
            }
        }

        public ResponseListener getResponseListener(){
            return listener;
        }

    }

}

