package com.onemena.data.eventbus;

import com.onemena.app.config.TJKey;

/**
 * Created by WHF on 2016-12-02.
 */

public class MyEntry{
    public String key;
    public String value;
    public static MyEntry getIns(@TJKey.Query String key, String value){
        return new MyEntry(key,value);
    }
    private MyEntry(String key,String value){

        this.key = key;
        this.value = value;
    }
}