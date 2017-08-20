package com.onemena.data.eventbus;

/**
 * Created by Administrator on 2016/12/5.
 */

public class NightMode {
    public boolean Mode;//true为夜间模式，false为白天模式

    public NightMode(boolean mode){
        Mode=mode;
    }

    public void setMode(boolean isNightMode){

        this.Mode = isNightMode;
    }
    public boolean getMode(){
        return Mode;
    }
}
