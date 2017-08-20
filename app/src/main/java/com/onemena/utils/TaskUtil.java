package com.onemena.utils;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.arabsada.news.R;
import com.onemena.app.config.ConfigUrls;
import com.onemena.app.config.TJKey;
import com.onemena.data.eventbus.MyEntry;
import com.onemena.listener.JsonObjectListener;
import com.onemena.service.PublicService;
import com.onemena.widght.TaskFinishDialog;

import java.util.HashMap;

/**
 * TaskUtil Created by voler on 2017/6/7.
 * 说明：
 */

public class TaskUtil {

    public static void sendTask(Context context, String taskName) {
        HashMap<String, String> params = new HashMap<>();
        params.put("event", taskName);
        String md5Code = MD5.GetMD5Code(MD5.GetMD5Code(taskName + context.getString(R.string.app_english_name)));
        params.put("secret", md5Code);
        PublicService.getInstance().postJsonObjectRequest(false, ConfigUrls.SEND_TASK, params, new JsonObjectListener() {
            @Override
            public void onJsonObject(JSONObject obj, Boolean isCacheData) throws Exception {

                JSONObject extra = obj.getJSONObject("extra");
                String alert_message = extra.getString("alert_message");
                if (StringUtils.isNotEmpty(alert_message)) {
                    TongJiUtil.getInstance().putEntries(TJKey.TASK_COM, MyEntry.getIns(TJKey.TASK_ID, extra.getString("task_id")));
                    showTaskFinish(context,alert_message);
                }
            }

            @Override
            public void onObjError() {

            }
        });
    }

    public static void showTaskFinish(Context context,String message) {
        if (TextUtils.isEmpty(message)) return;
        TaskFinishDialog taskFinishDialog = new TaskFinishDialog(context);
        taskFinishDialog.setLoadText(message);
        taskFinishDialog.show();
    }
}
