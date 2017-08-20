package com.onemena.utils;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 妈妈生活圈
 * 三尺春光驱我寒，一生戎马为长安 -- 韩经录
 * Created by voler on 2016/7/28.
 */
public class WaitUtil {

    private HashMap<String, String> status = new HashMap();
    private static WaitUtil waitUtil;


    public static WaitUtil getInstance() {
        if (waitUtil == null) {
            synchronized (WaitUtil.class) {
                if (waitUtil == null) {
                    waitUtil = new WaitUtil();
                }
            }
        }
        return waitUtil;
    }


    public boolean waitTime(final String name, final long time) {
        if (status.get(name) == null) {
            status.put(name, "1");
            waitPool(name, time);
            return true;
        }  else if ("1".equals(status.get(name))) {
            return false;
        } else if ("0".equals(status.get(name))) {
            status.put(name, "1");
            waitPool(name, time);
            return true;
        } else {
            return false;
        }
    }

    private void waitPool(final String name, final long time) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(new Runnable() {
            public void run() {
                try {
                        Thread.sleep(time);
                        status.put(name, "0");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
