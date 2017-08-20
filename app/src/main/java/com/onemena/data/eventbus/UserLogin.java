package com.onemena.data.eventbus;

/**
 * Created by Administrator on 2016/12/22.
 */

public class UserLogin {
    public  boolean reLogin;

    public UserLogin(Boolean reLogin){
        this.reLogin=reLogin;
    }

    public void setReLogin(boolean reLogin){
        this.reLogin=reLogin;
    }


}
