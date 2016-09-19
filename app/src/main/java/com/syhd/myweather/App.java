package com.syhd.myweather;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * @author 韩宝坤
 * @date 2016/8/19 17:29
 * @email kunyu1342@163.com
 * @description
 */
public class App extends Application {
    /**
     * 全局Context，方便引用
     */
    public static App application;
    /**
     * 初始化SP&EDIT
     */
    public SharedPreferences SP;
    public SharedPreferences.Editor EDIT;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        //初始化通用的SP&EDIT
        SP = getSharedPreferences("config", MODE_PRIVATE);
        EDIT = SP.edit();
//        MobAPI.initSDK(this, "164a1b6066c53");
    }
}
