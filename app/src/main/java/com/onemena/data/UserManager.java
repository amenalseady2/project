package com.onemena.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.NetworkResponse;
import com.onemena.app.NewsApplication;
import com.onemena.app.activity.MainActivity;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.Point;
import com.onemena.app.config.SPKey;
import com.onemena.app.config.TJKey;
import com.onemena.data.eventbus.LoginBean;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.data.eventbus.PointShow;
import com.onemena.listener.JsonObjectListener;
import com.onemena.service.PublicService;
import com.onemena.utils.GetPhoneInfoUtil;
import com.onemena.utils.SpUtil;
import com.onemena.utils.StringUtils;
import com.onemena.utils.TaskUtil;
import com.onemena.utils.ToastUtil;
import com.onemena.utils.TongJiUtil;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * 储存用户登录信息
 *
 * @author 张玉水
 * @Description:
 * @date 2016-1-27 下午4:05:26
 */
public class UserManager {

    private static final String SP_NAME = "sp_name";

    /**
     * 保存用户信息
     *
     * @return void
     * @Description:
     * @author 张玉水
     * @date 2016-1-31 下午3:43:20
     */
    public static void setUser(String str) {
        SharedPreferences sp = NewsApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString("user", str).commit();
    }

    /**
     * 清除用户信息
     *
     * @return void
     * @Description:
     * @author 张玉水
     * @date 2016-1-31 下午3:43:04
     */
    public static void clearUser() {
        SharedPreferences sp = NewsApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().clear().commit();

    }

    /**
     * 添加或修改属性
     *
     * @return void
     * @Description:
     * @author 张玉水
     * @date 2016-1-28 上午11:26:37
     */
    public static void addUserInfo(String key, String value) {
        SharedPreferences sp = NewsApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        JSONObject jsonObject = getUserObj();
        if (jsonObject != null) {
            jsonObject.put(key, value);
            sp.edit().putString("user", jsonObject.toJSONString()).commit();
        }
    }

    /**
     * 获取用户登录信息jsonobject
     *
     * @return JSONObject
     * @Description:
     * @author 张玉水
     * @date 2016-1-31 下午3:45:25
     */
    public static JSONObject getUserObj() {
        String userStr = getUserStr();
        JSONObject jsonObject = JSON.parseObject(userStr);
        return jsonObject;
    }

    /**
     * 获取用户登录息
     *
     * @return String
     * @Description:
     * @author 张玉水
     * @date 2016-1-31 下午3:44:29value = "{"code":1,"content":{"first_name":"","id":469,"last_name":"","profile_photo":"","user_email":"A0000059CC1213@guest","user_name":"A0000059CC1213"},"message":"Login Success"}"
     */
    public static String getUserStr() {
        SharedPreferences sp = NewsApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getString("user", "{}");
    }


    //登录
    private static void login(String name, String password) {

        final long start_time = System.currentTimeMillis();

        HashMap<String, String> map = new HashMap<>();
        map.put("username", name);
        map.put("password", password);
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.NEWS_LOGIN, map, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) {

                String jsonString = obj.toJSONString();
                LoginBean loginBean = JSONObject.parseObject(jsonString, LoginBean.class);

                JSONObject content = obj.getJSONObject("content");
                long end_time = System.currentTimeMillis();
                TongJiUtil.getInstance().putEntries(TJKey.FIRET_CHECK_NETWORK, MyEntry.getIns(TJKey.TIME1, Long.toString(end_time - start_time)));

                addUserInfo("userinfo", obj.getJSONObject("content").toJSONString());//存储用户信息

                SpUtil.saveValue(SPKey.CREATE_TIME, content.getString("first_open_time"));
                Boolean goods_notify = content.getBoolean("goods_notify");
                ToastUtil.showDevShortToast(goods_notify.toString());
                //什么时候有，由服务器控制，什么时候消失，由本地控制
                if (Point.checkPointFlag(Point.UPDATE_MARKET_FLAG) && goods_notify) {
                    Point.addPointFlag(Point.GOLD_MARKET_FLAG);
                    EventBus.getDefault().post(new PointShow());
                    Point.removePointFlag(Point.UPDATE_MARKET_FLAG);
                }

                JSONObject extra = obj.getJSONObject("extra");
                String alert_message = extra.getString("alert_message");
                if (StringUtils.isNotEmpty(alert_message)) {
                    TongJiUtil.getInstance().putEntries(TJKey.TASK_COM, MyEntry.getIns(TJKey.TASK_ID, extra.getString("task_id")));
                    MainActivity mainActivity = NewsApplication.getInstance().getMainActivity();
                    if (mainActivity != null) {
                        TaskUtil.showTaskFinish(mainActivity,alert_message);
                    }
                }

                EventBus.getDefault().post(loginBean);
            }

            @Override
            public void onObjError() {
            }

            @Override
            public void onResonse(NetworkResponse response) {
                super.onResonse(response);
            }
        });
    }

    //注册
    public static void register(boolean b) {
        final String imei = GetPhoneInfoUtil.INSTANCE.getAndroidId();
        login(imei, "password");

//        NewsApplication.getInstance().cleanSessionCookie();

//        final String imei = getUserObj().getString("imei");
//        HashMap<String, String> map = new HashMap<>();
////        map.put("user_name",name);
////        map.put("user_password",password);
//        map.put("uuid", imei);//359320050978138
//        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.NEWS_REGISTER, map, new JsonObjectListener() {
//            @Override
//            public void onJsonObject(JSONObject obj, Boolean isCacheData) {
//                login(imei, "password");
//            }
//
//            @Override
//            public void onObjError() {
////                ToastUtil.showNormalShortToast("error");
//            }
//        });
    }


}
